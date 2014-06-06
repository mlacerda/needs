package com.softb.system.repository.config;

import javax.sql.DataSource;

import liquibase.integration.spring.SpringLiquibase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = { 
		"com.softb.system.security.repository"})
@EntityScan(basePackages = { 
		"com.softb.system.security.model"})
public class RepositoryConfig  {

    private final Logger logger = LoggerFactory.getLogger(RepositoryConfig.class);
    
    @Bean
    public SpringLiquibase liquibase(DataSource datasource) {
        logger.debug("Configuring Liquibase");
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(datasource);
        liquibase.setChangeLog("classpath:config/liquibase/master.xml");
        liquibase.setContexts("development, production");
        return liquibase;
    }
}

