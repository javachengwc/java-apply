package com.spring.pseudocode.context.context.annotation;

import org.springframework.core.type.AnnotationMetadata;

public interface ImportSelector {

    String[] selectImports(AnnotationMetadata annotationMetadata);
}
