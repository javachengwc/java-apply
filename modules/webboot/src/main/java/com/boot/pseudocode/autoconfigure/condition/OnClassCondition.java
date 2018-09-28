package com.boot.pseudocode.autoconfigure.condition;

import com.boot.pseudocode.autoconfigure.AutoConfigurationImportFilter;
import com.boot.pseudocode.autoconfigure.AutoConfigurationMetadata;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionEvaluationReport;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.MultiValueMap;

import java.security.AccessControlException;
import java.util.*;

@Order(Ordered.HIGHEST_PRECEDENCE)
class OnClassCondition extends SpringBootCondition implements AutoConfigurationImportFilter, BeanFactoryAware, BeanClassLoaderAware {

    private BeanFactory beanFactory;

    private ClassLoader beanClassLoader;

    @Override
    public boolean[] match(String[] autoConfigurationClasses, AutoConfigurationMetadata autoConfigurationMetadata) {
        ConditionEvaluationReport report = getConditionEvaluationReport();
        ConditionOutcome[] outcomes = getOutcomes(autoConfigurationClasses, autoConfigurationMetadata);
        boolean[] match = new boolean[outcomes.length];
        for (int i = 0; i < outcomes.length; i++) {
            match[i] = (outcomes[i] == null || outcomes[i].isMatch());
            if (!match[i] && outcomes[i] != null) {
                if (report != null) {
                    //report.recordConditionEvaluation(autoConfigurationClasses[i], this, outcomes[i]);
                }
            }
        }
        return match;
    }

    private ConditionEvaluationReport getConditionEvaluationReport() {
        if (this.beanFactory != null && this.beanFactory instanceof ConfigurableBeanFactory) {
            return ConditionEvaluationReport.get((ConfigurableListableBeanFactory) this.beanFactory);
        }
        return null;
    }

    private ConditionOutcome[] getOutcomes(String[] autoConfigurationClasses,AutoConfigurationMetadata autoConfigurationMetadata) {
        int split = autoConfigurationClasses.length / 2;
        OutcomesResolver firstHalfResolver = createOutcomesResolver(autoConfigurationClasses, 0, split, autoConfigurationMetadata);
        OutcomesResolver secondHalfResolver = new StandardOutcomesResolver(autoConfigurationClasses, split, autoConfigurationClasses.length,
                autoConfigurationMetadata, this.beanClassLoader);
        ConditionOutcome[] secondHalf = secondHalfResolver.resolveOutcomes();
        ConditionOutcome[] firstHalf = firstHalfResolver.resolveOutcomes();
        ConditionOutcome[] outcomes = new ConditionOutcome[autoConfigurationClasses.length];
        System.arraycopy(firstHalf, 0, outcomes, 0, firstHalf.length);
        System.arraycopy(secondHalf, 0, outcomes, split, secondHalf.length);
        return outcomes;
    }

    private OutcomesResolver createOutcomesResolver(String[] autoConfigurationClasses,int start, int end,
            AutoConfigurationMetadata autoConfigurationMetadata) {
        OutcomesResolver outcomesResolver = new StandardOutcomesResolver(autoConfigurationClasses, start, end,
            autoConfigurationMetadata, this.beanClassLoader);
        try {
            return new ThreadedOutcomesResolver(outcomesResolver);
        }
        catch (AccessControlException ex) {
            return outcomesResolver;
        }
    }

    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        ClassLoader classLoader = context.getClassLoader();
        ConditionMessage matchMessage = ConditionMessage.empty();
        List<String> onClasses = getCandidates(metadata, ConditionalOnClass.class);
        if (onClasses != null) {
            List<String> missing = getMatches(onClasses, OnClassCondition.MatchType.MISSING, classLoader);
            if (!missing.isEmpty()) {
                return ConditionOutcome.noMatch(ConditionMessage.forCondition(ConditionalOnClass.class)
                    .didNotFind("required class", "required classes")
                    .items(org.springframework.boot.autoconfigure.condition.ConditionMessage.Style.QUOTE, missing));
            }
            matchMessage = matchMessage.andCondition(ConditionalOnClass.class).found("required class", "required classes")
                .items(ConditionMessage.Style.QUOTE, getMatches(onClasses, OnClassCondition.MatchType.PRESENT, classLoader));
        }
        List<String> onMissingClasses = getCandidates(metadata, ConditionalOnMissingClass.class);
        if (onMissingClasses != null) {
            List<String> present = getMatches(onMissingClasses, OnClassCondition.MatchType.PRESENT, classLoader);
            if (!present.isEmpty()) {
                return ConditionOutcome.noMatch(ConditionMessage.forCondition(ConditionalOnMissingClass.class)
                        .found("unwanted class", "unwanted classes")
                        .items(ConditionMessage.Style.QUOTE, present));
            }
            matchMessage = matchMessage.andCondition(ConditionalOnMissingClass.class)
                    .didNotFind("unwanted class", "unwanted classes")
                    .items(ConditionMessage.Style.QUOTE, getMatches(onMissingClasses, OnClassCondition.MatchType.MISSING, classLoader));
        }
        return ConditionOutcome.match(matchMessage);
    }

    private List<String> getCandidates(AnnotatedTypeMetadata metadata, Class<?> annotationType) {
        MultiValueMap<String, Object> attributes = metadata.getAllAnnotationAttributes(annotationType.getName(), true);
        List<String> candidates = new ArrayList<String>();
        if (attributes == null) {
            return Collections.emptyList();
        }
        addAll(candidates, attributes.get("value"));
        addAll(candidates, attributes.get("name"));
        return candidates;
    }

    private void addAll(List<String> list, List<Object> itemsToAdd) {
        if (itemsToAdd != null) {
            for (Object item : itemsToAdd) {
                Collections.addAll(list, (String[]) item);
            }
        }
    }

    private List<String> getMatches(Collection<String> candidates,OnClassCondition.MatchType matchType,
                                    ClassLoader classLoader) {
        List<String> matches = new ArrayList<String>(candidates.size());
        for (String candidate : candidates) {
            if (matchType.matches(candidate, classLoader)) {
                matches.add(candidate);
            }
        }
        return matches;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader = classLoader;
    }

    private enum MatchType {
        PRESENT {
            @Override
            public boolean matches(String className, ClassLoader classLoader) {
                return isPresent(className, classLoader);
            }
        },
        MISSING {
            @Override
            public boolean matches(String className, ClassLoader classLoader) {
                return !isPresent(className, classLoader);
            }
        };

        private static boolean isPresent(String className, ClassLoader classLoader) {
            if (classLoader == null) {
                classLoader = ClassUtils.getDefaultClassLoader();
            }
            try {
                forName(className, classLoader);
                return true;
            }
            catch (Throwable ex) {
                return false;
            }
        }

        private static Class<?> forName(String className, ClassLoader classLoader) throws ClassNotFoundException {
            if (classLoader != null) {
                return classLoader.loadClass(className);
            }
            return Class.forName(className);
        }

        public abstract boolean matches(String className, ClassLoader classLoader);

    }

    private interface OutcomesResolver {
        ConditionOutcome[] resolveOutcomes();
    }

    private static final class ThreadedOutcomesResolver implements OutcomesResolver {

        private final Thread thread;

        private volatile ConditionOutcome[] outcomes;

        private ThreadedOutcomesResolver(final OutcomesResolver outcomesResolver) {
            this.thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    ThreadedOutcomesResolver.this.outcomes = outcomesResolver.resolveOutcomes();
                }
            });
            this.thread.start();
        }

        @Override
        public ConditionOutcome[] resolveOutcomes() {
            try {
                this.thread.join();
            }
            catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            return this.outcomes;
        }
    }

    private final class StandardOutcomesResolver implements OutcomesResolver {

        private final String[] autoConfigurationClasses;

        private final int start;

        private final int end;

        private final AutoConfigurationMetadata autoConfigurationMetadata;

        private final ClassLoader beanClassLoader;

        private StandardOutcomesResolver(String[] autoConfigurationClasses, int start,
             int end, AutoConfigurationMetadata autoConfigurationMetadata,
             ClassLoader beanClassLoader) {
            this.autoConfigurationClasses = autoConfigurationClasses;
            this.start = start;
            this.end = end;
            this.autoConfigurationMetadata = autoConfigurationMetadata;
            this.beanClassLoader = beanClassLoader;
        }

        @Override
        public ConditionOutcome[] resolveOutcomes() {
            return getOutcomes(this.autoConfigurationClasses, this.start, this.end, this.autoConfigurationMetadata);
        }

        private ConditionOutcome[] getOutcomes(final String[] autoConfigurationClasses,int start, int end,
                                               AutoConfigurationMetadata autoConfigurationMetadata) {
            ConditionOutcome[] outcomes = new ConditionOutcome[end - start];
            for (int i = start; i < end; i++) {
                String autoConfigurationClass = autoConfigurationClasses[i];
                Set<String> candidates = autoConfigurationMetadata
                        .getSet(autoConfigurationClass, "ConditionalOnClass");
                if (candidates != null) {
                    outcomes[i - start] = getOutcome(candidates);
                }
            }
            return outcomes;
        }

        private ConditionOutcome getOutcome(Set<String> candidates) {
            try {
                List<String> missing = getMatches(candidates, OnClassCondition.MatchType.MISSING, this.beanClassLoader);
                if (!missing.isEmpty()) {
                    return ConditionOutcome.noMatch(
                        ConditionMessage.forCondition(ConditionalOnClass.class)
                            .didNotFind("required class", "required classes")
                            .items(ConditionMessage.Style.QUOTE, missing));
                }
            }
            catch (Exception ex) {
            }
            return null;
        }

    }

}

