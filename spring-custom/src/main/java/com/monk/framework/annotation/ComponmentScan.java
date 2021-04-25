package com.monk.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName ComponmentScan
 * @Description: TODO
 * @Author Monk
 * @Date 2020/10/27
 * @Version V1.0
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ComponmentScan {

    String value() default "";
}
