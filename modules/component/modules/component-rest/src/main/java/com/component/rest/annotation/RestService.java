package com.component.rest.annotation;

import java.lang.annotation.*;

@Inherited()
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface RestService {
}