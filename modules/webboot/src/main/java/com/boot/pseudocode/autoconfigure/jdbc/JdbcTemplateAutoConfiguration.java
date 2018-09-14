package com.boot.pseudocode.autoconfigure.jdbc;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
@ConditionalOnClass({DataSource.class, JdbcTemplate.class})
@ConditionalOnSingleCandidate(DataSource.class)
@AutoConfigureAfter({DataSourceAutoConfiguration.class})
public class JdbcTemplateAutoConfiguration
{
    private final DataSource dataSource;

    public JdbcTemplateAutoConfiguration(DataSource dataSource)
    {
        this.dataSource = dataSource; }
    @Bean
    @Primary
    @ConditionalOnMissingBean({JdbcOperations.class})
    public JdbcTemplate jdbcTemplate() { return new JdbcTemplate(this.dataSource); }

    @Bean
    @Primary
    @ConditionalOnMissingBean({NamedParameterJdbcOperations.class})
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() { return new NamedParameterJdbcTemplate(this.dataSource);
    }
}
