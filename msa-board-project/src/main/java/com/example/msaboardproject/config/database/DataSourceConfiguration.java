package com.example.msaboardproject.config.database;

import com.example.msaboardproject.database.RoutingDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class DataSourceConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create()
                .build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.slave")
    public DataSource slaveDataSource() {
        return DataSourceBuilder.create()
                .build();
    }

    @Bean
    public DataSource routingDataSource(
            DataSource masterDataSource,
            DataSource slaveDataSource
    ) {
        Map<Object, Object> dataSources = new HashMap<>();
        dataSources.put("master", masterDataSource);
        dataSources.put("slave", slaveDataSource);

        RoutingDataSource routingDataSource = new RoutingDataSource(List.of("slave"));
        routingDataSource.setDefaultTargetDataSource(dataSources.get("master"));
        routingDataSource.setTargetDataSources(dataSources);

        return routingDataSource;
    }

    @Primary
    @Bean
    public DataSource dataSource() {
        return new LazyConnectionDataSourceProxy(routingDataSource(masterDataSource(), slaveDataSource()));
    }

}
