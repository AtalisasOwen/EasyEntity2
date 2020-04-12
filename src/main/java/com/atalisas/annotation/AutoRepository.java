package com.atalisas.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface AutoRepository {
    RepositoryStrategy strategy() default RepositoryStrategy.JPA_WITH_SPECIFICATION;
}
