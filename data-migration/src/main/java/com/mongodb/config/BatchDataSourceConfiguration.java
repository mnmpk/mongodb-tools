package com.mongodb.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.mongodb.batch.repository", entityManagerFactoryRef = "batchEntityManagerFactory", transactionManagerRef = "batchTransactionManager")
public class BatchDataSourceConfiguration {
	@Autowired
	private Environment env;

	public static final String BATCH = "batch";

	@Bean(name = "batchDataSource")
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource-batch")
	public DataSource batchDataSource() {
		return DataSourceBuilder.create().url(env.getProperty("spring.datasource-batch.url"))
				.driverClassName(env.getProperty("spring.datasource-batch.driver-class-name"))
				.username(env.getProperty("spring.datasource-batch.username"))
				.password(env.getProperty("spring.datasource-batch.password")).build();
	}

	@Bean(name = "batchEntityManagerFactory")
	@Primary
	public LocalContainerEntityManagerFactoryBean batchEntityManagerFactory(EntityManagerFactoryBuilder builder) {
		Map<String, Object> properties = new HashMap<String, Object>();
		return builder.dataSource(batchDataSource()).packages("com.mongodb.entity").persistenceUnit(BATCH)
				.properties(properties).build();
	}

	@Bean(name = "batchTransactionManager")
	@Primary
	public PlatformTransactionManager batchTransactionManager() {
		JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
		jpaTransactionManager.setDataSource(batchDataSource());
		jpaTransactionManager.setPersistenceUnitName(BATCH);
		return jpaTransactionManager;
	}
}
