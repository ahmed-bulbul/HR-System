package com.hrsystem.employee.audit;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Auditable {
    String entity();
    String action();
    String description() default "";
}

