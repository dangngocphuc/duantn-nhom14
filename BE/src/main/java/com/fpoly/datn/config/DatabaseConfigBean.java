package com.fpoly.datn.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DatabaseConfigBean {
	@Value("${spring.datasource.driver-class-name}")
	private String dsDriverClassName;
	@Value("${spring.datasource.url}")
	private String dsUrl;
	@Value("${spring.datasource.username}")
	private String username;
	@Value("${spring.datasource.password}")
	private String password;
	
	@Bean(name = "dataSource")
	public DataSource primaryDataSource() throws Exception  {
		DataSourceBuilder builder=DataSourceBuilder.create();
		builder.driverClassName(dsDriverClassName);
		builder.url(dsUrl);
		builder.username(username);
		builder.password(password);
		return builder.build();
	}

	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}
	
}
