package com.spring.pseudocode.core.core.annotation;

import com.spring.pseudocode.core.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class SynthesizingMethodParameter extends MethodParameter {

    public SynthesizingMethodParameter(Method method, int parameterIndex) {

        super(method, parameterIndex);
    }

    protected <A extends Annotation> A adaptAnnotation(A annotation) {
        return AnnotationUtils.synthesizeAnnotation(annotation, this.getAnnotatedElement());
    }

    protected Annotation[] adaptAnnotationArray(Annotation[] annotations) {
        return AnnotationUtils.synthesizeAnnotationArray(annotations, this.getAnnotatedElement());
    }
}
