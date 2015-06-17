package com.trinary.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface OWASPValidation {
	String pattern() default "";
	String storedPattern() default "";
	OWASPValidationType type() default OWASPValidationType.WHITELIST;
}