package com.monk.framework;

import com.monk.framework.annotation.*;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName CustomApplicationContext
 * @Description: TODO
 * @Author Monk
 * @Date 2020/10/27
 * @Version V1.0
 **/
public class CustomApplicationContext {

    private static final Logger logger = LoggerFactory.getLogger(CustomApplicationContext.class);

    // Bean定义缓存Map
    private static final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<String, BeanDefinition>();

    // 单例Bean缓存Map
    private static final Map<String, Object> singletonObjects = new HashMap<String, Object>();

    private Class configClass;

    public CustomApplicationContext(Class configClass){
        this.configClass = configClass;

        // 扫描bean
        scan(configClass);

        // 创建非懒加载的bean
        createNonLazySingleton();
    }

    private void createNonLazySingleton() {
        for (String beanName : beanDefinitionMap.keySet()) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if(beanDefinition.getScope().equals(ScopeType.SINGLETON.getValue()) && !beanDefinition.isLazy()){
                Object bean = createBean(beanDefinition, beanName);
                singletonObjects.put(beanName, bean);
            }
        }
    }

    private Object createBean(BeanDefinition beanDefinition, String beanName) {
        Class beanClass = beanDefinition.getBeanClass();
        try {
            Object instance = beanClass.getDeclaredConstructor().newInstance();

            // 给属性赋值
            for (Field field : beanClass.getDeclaredFields()) {
                if(field.isAnnotationPresent(Autowired.class)){
                    Object bean = getBean(field.getName());
                    field.setAccessible(true);
                    field.set(instance, bean);
                }
            }

            if(instance instanceof BeanNameAware){
                ((BeanNameAware) instance).setName(beanName);
            }



            return instance;
        } catch (InstantiationException e) {
            logger.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            logger.error(e.getMessage(), e);
        } catch (NoSuchMethodException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    private void scan(Class configClass) {
        if(configClass.isAnnotationPresent(ComponmentScan.class)){
            ComponmentScan componmentScan = (ComponmentScan)configClass.getAnnotation(ComponmentScan.class);
            String scanPath = componmentScan.value();
            logger.info("scapath:{}", scanPath);

            ClassLoader classLoader = CustomApplicationContext.class.getClassLoader();
            URL resource = classLoader.getResource(scanPath.replace(".", "/"));
            File file = new File(resource.getFile());
            for(File temp : file.listFiles()){
                logger.info("file:{}", temp);
                if(temp.getName().endsWith(".class")){
                    String className = temp.getAbsolutePath();
                    className = className.substring(className.indexOf("com"), className.indexOf(".class"));
                    className = className.replace(File.separator, ".");
                    logger.info("className:{}", className);
                    Class clazz = null;
                    try {
                        clazz = classLoader.loadClass(className);
                        if(clazz.isAnnotationPresent(Componment.class)){
                            BeanDefinition beanDefinition = new BeanDefinition();
                            beanDefinition.setBeanClass(clazz);

                            //  获取component配置的beanName
                            Componment componment = (Componment)clazz.getAnnotation(Componment.class);
                            String beanName = componment.value();
                            if(StringUtils.isBlank(beanName)){
                                beanName = clazz.getSimpleName().toLowerCase();
                            }

                            if(clazz.isAnnotationPresent(Lazy.class)){
                                beanDefinition.setLazy(true);
                            }

                            if(clazz.isAnnotationPresent(Scope.class)){
                                Scope scope = (Scope) clazz.getAnnotation(Scope.class);
                                String scopeVal = scope.value();
                                beanDefinition.setScope(scopeVal);
                            }else{
                                // 默认单例
                                beanDefinition.setScope("singleton");
                            }
                            beanDefinitionMap.put(beanName, beanDefinition);
                        }
                    } catch (ClassNotFoundException e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }
        }
    }


    public Object getBean(String beanName){
        if(StringUtils.isBlank(beanName)){
            throw new RuntimeException("beanNmae不允许为空");
        }
        if(!beanDefinitionMap.containsKey(beanName)){
            throw new NullPointerException("bean不存在");
        }
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if(beanDefinition.getScope().equals(ScopeType.SINGLETON.getValue())){
            Object bean = singletonObjects.get(beanName);
            if(null ==  bean){
                bean = createBean(beanDefinition, beanName);
                singletonObjects.put(beanName, bean);
            }
            return bean;
        }else if(beanDefinition.getScope().equals(ScopeType.PROTOTYPE.getValue())){
            Object bean = createBean(beanDefinition, beanName);
            return bean;
        }
        return null;
    }
}
