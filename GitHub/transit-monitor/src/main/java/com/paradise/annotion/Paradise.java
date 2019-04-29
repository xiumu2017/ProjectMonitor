package com.paradise.annotion;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Paradise {
    public String name() default "no name";

    public boolean repeat() default false;
}
