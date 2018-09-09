package com.hibernate.pseudocode.validator;


import javax.validation.Configuration;

public abstract interface HibernateValidatorConfiguration extends Configuration<HibernateValidatorConfiguration>
{
    public static final String FAIL_FAST = "hibernate.validator.fail_fast";

    @Deprecated
    public static final String CONSTRAINT_MAPPING_CONTRIBUTOR = "hibernate.validator.constraint_mapping_contributor";


    public abstract HibernateValidatorConfiguration failFast(boolean paramBoolean);

    public abstract HibernateValidatorConfiguration externalClassLoader(ClassLoader paramClassLoader);

    public abstract HibernateValidatorConfiguration allowOverridingMethodAlterParameterConstraint(boolean paramBoolean);

    public abstract HibernateValidatorConfiguration allowMultipleCascadedValidationOnReturnValues(boolean paramBoolean);

    public abstract HibernateValidatorConfiguration allowParallelMethodsDefineParameterConstraints(boolean paramBoolean);
}
