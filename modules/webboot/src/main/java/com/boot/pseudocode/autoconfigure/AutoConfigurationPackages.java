package com.boot.pseudocode.autoconfigure;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.annotation.DeterminableImports;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.*;

public class AutoConfigurationPackages {

    private static final String BEAN = AutoConfigurationPackages.class.getName();

    public static void register(BeanDefinitionRegistry registry, String[] packageNames)
    {
        if (registry.containsBeanDefinition(BEAN)) {
            BeanDefinition beanDefinition = registry.getBeanDefinition(BEAN);
            ConstructorArgumentValues constructorArguments = beanDefinition.getConstructorArgumentValues();
            constructorArguments.addIndexedArgumentValue(0, addBasePackages(constructorArguments, packageNames));
        }
        else
        {
            GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
            beanDefinition.setBeanClass(BasePackages.class);
            beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0, packageNames);
            beanDefinition.setRole(2);
            registry.registerBeanDefinition(BEAN, beanDefinition);
        }
    }

    private static String[] addBasePackages(ConstructorArgumentValues constructorArguments, String[] packageNames)
    {
        String[] existing = (String[])constructorArguments.getIndexedArgumentValue(0, String.class).getValue();
        Set merged = new LinkedHashSet();
        merged.addAll(Arrays.asList(existing));
        merged.addAll(Arrays.asList(packageNames));
        return (String[])merged.toArray(new String[merged.size()]);
    }

    static final class BasePackages
    {
        private final List<String> packages;

        private boolean loggedBasePackageInfo;

        BasePackages(String[] names)
        {
            List packages = new ArrayList();
            for (String name : names) {
                if (StringUtils.hasText(name)) {
                    packages.add(name);
                }
            }
            this.packages = packages;
        }

        public List<String> get() {
            //...
            return null;
        }
    }

    private static final class PackageImport
    {
        private final String packageName;

        PackageImport(AnnotationMetadata metadata)
        {
            this.packageName = ClassUtils.getPackageName(metadata.getClassName());
        }

        public String getPackageName() {
            return this.packageName;
        }
    }

    static class Registrar implements ImportBeanDefinitionRegistrar, DeterminableImports
    {
        public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry)
        {
            AutoConfigurationPackages.register(registry, new String[] { new AutoConfigurationPackages.PackageImport(metadata).getPackageName() });
        }

        public Set<Object> determineImports(AnnotationMetadata metadata)
        {
            //return Collections.singleton(new AutoConfigurationPackages.PackageImport(metadata));
            return null;
        }
    }
}
