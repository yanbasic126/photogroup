package com.photogroup.model.group;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GroupByType {

    public boolean primaryKey() default false;

    public boolean index() default false;

    public boolean groupBy() default true;
}
