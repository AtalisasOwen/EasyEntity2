package com.atalisas.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Controller注解
 * 找到Service中的所有public方法
 * 方法返回值ResponseEntity，参数（加注解），Entity转EntityDto， 方法名沿用
 * List特殊处理
 * URL：Get， Post，url对方法签名进行解析（get，put，post，delete）
 *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface AutoController {
    Class value();
}
