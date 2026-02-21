package com.boot.pseudocode.autoconfigure.condition;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.condition.ConditionEvaluationReport;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.MethodMetadata;

public abstract class SpringBootCondition implements Condition {

    private final Log logger = LogFactory.getLog(getClass());

    @Override
    public final boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String classOrMethodName = getClassOrMethodName(metadata);
        try {
            ConditionOutcome outcome = getMatchOutcome(context, metadata);
            recordEvaluation(context, classOrMethodName, outcome);
            return outcome.isMatch();
        } catch (NoClassDefFoundError ex) {
            throw new IllegalStateException("Could not evaluate condition on " + classOrMethodName + " due to " + ex.getMessage() , ex);
        } catch (RuntimeException ex) {
            throw new IllegalStateException("Error processing condition on " + getName(metadata), ex);
        }
    }

    private String getName(AnnotatedTypeMetadata metadata) {
        if (metadata instanceof AnnotationMetadata) {
            return ((AnnotationMetadata) metadata).getClassName();
        }
        if (metadata instanceof MethodMetadata) {
            MethodMetadata methodMetadata = (MethodMetadata) metadata;
            return methodMetadata.getDeclaringClassName() + "." + methodMetadata.getMethodName();
        }
        return metadata.toString();
    }

    private static String getClassOrMethodName(AnnotatedTypeMetadata metadata) {
        if (metadata instanceof ClassMetadata) {
            ClassMetadata classMetadata = (ClassMetadata) metadata;
            return classMetadata.getClassName();
        }
        MethodMetadata methodMetadata = (MethodMetadata) metadata;
        return methodMetadata.getDeclaringClassName() + "#" + methodMetadata.getMethodName();
    }

    private void recordEvaluation(ConditionContext context, String classOrMethodName, ConditionOutcome outcome) {
        if (context.getBeanFactory() != null) {
            //ConditionEvaluationReport.get(context.getBeanFactory()).recordConditionEvaluation(classOrMethodName, this, outcome);
        }
    }

    public abstract ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata);

    protected final boolean anyMatches(ConditionContext context, AnnotatedTypeMetadata metadata, Condition... conditions) {
        for (Condition condition : conditions) {
            if (matches(context, metadata, condition)) {
                return true;
            }
        }
        return false;
    }

    protected final boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata, Condition condition) {
        if (condition instanceof SpringBootCondition) {
            return ((SpringBootCondition) condition).getMatchOutcome(context, metadata).isMatch();
        }
        return condition.matches(context, metadata);
    }

}
