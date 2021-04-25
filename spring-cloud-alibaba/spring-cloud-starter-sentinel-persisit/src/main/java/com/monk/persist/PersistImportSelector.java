package com.monk.persist;

import com.monk.config.NacosConfiguration;
import com.monk.config.ZookeeperConfiguration;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.util.function.Predicate;

/**
 * @ClassName PersistImportSelector
 * @Description: TODO
 * @Author Monk
 * @Date 2021/4/13
 * @Version V1.0
 **/
public class PersistImportSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {

        Class<?> annotationType = EnablePersistence.class;
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(
                annotationType.getName(), false));
        DataSourceEnum dataSourceEnum = attributes.getEnum("type");
        if (DataSourceEnum.Zookeeper.equals(dataSourceEnum)) {
            return new String[] { ZookeeperConfiguration.class.getName() };
        } else {
            return new String[] { NacosConfiguration.class.getName() };
        }
    }

    @Override
    public Predicate<String> getExclusionFilter() {
        return null;
    }
}
