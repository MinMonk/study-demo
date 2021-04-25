package com.monk.framework;

/**
 * @ClassName BeanDefinition
 * @Description: TODO
 * @Author Monk
 * @Date 2020/10/27
 * @Version V1.0
 **/
public class BeanDefinition {

    private String scope;
    private boolean isLazy;
    private Class beanClass;

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public boolean isLazy() {
        return isLazy;
    }

    public void setLazy(boolean lazy) {
        isLazy = lazy;
    }

    public Class getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    public String toString() {
        return "BeanDefinition{" +
                "scope='" + scope + '\'' +
                ", isLazy=" + isLazy +
                ", beanClass=" + beanClass +
                '}';
    }
}
