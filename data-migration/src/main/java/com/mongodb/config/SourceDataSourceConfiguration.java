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
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.mongodb.mysql.repository", entityManagerFactoryRef = "sourceEntityManagerFactory", transactionManagerRef = "sourceTransactionManager")
public class SourceDataSourceConfiguration {
	@Autowired
	private Environment env;

	public static final String SOURCE = "source";

	@Bean(name = "sourceDataSource")
	@ConfigurationProperties(prefix = "spring.datasource-source")
	public DataSource sourceDataSource() {
		return DataSourceBuilder.create().url(env.getProperty("spring.datasource-source.url"))
				.driverClassName(env.getProperty("spring.datasource-source.driver-class-name"))
				.username(env.getProperty("spring.datasource-source.username"))
				.password(env.getProperty("spring.datasource-source.password")).build();
	}

	@Bean(name = "sourceEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean sourceEntityManagerFactory(EntityManagerFactoryBuilder builder) {
		Map<String, Object> properties = new HashMap<String, Object>();
		return builder.dataSource(sourceDataSource()).packages("com.mongodb.mysql").persistenceUnit(SOURCE)
				.properties(properties).build();
	}

	@Bean(name = "sourceTransactionManager")
	public PlatformTransactionManager sourceTransactionManager() {
		JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
		jpaTransactionManager.setDataSource(sourceDataSource());
		jpaTransactionManager.setPersistenceUnitName(SOURCE);
		return jpaTransactionManager;
	}
}
