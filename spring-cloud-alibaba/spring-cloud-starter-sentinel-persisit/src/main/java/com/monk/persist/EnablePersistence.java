package com.monk.persist;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(PersistImportSelector.class)
public @interface EnablePersistence {

    DataSourceEnum type() default DataSourceEnum.Nacos;

}
