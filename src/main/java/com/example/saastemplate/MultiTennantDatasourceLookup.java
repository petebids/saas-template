package com.example.saastemplate;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.datasource.lookup.DataSourceLookup;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class MultiTennantDatasourceLookup implements DataSourceLookup {

    private final DataSourcePropertiesRepository dataSourcePropertiesRepository;
    private final ConcurrentHashMap<String, DataSource> dataSourceByName = new ConcurrentHashMap<>();

    @Override
    public DataSource getDataSource(String dataSourceName) throws DataSourceLookupFailureException {
        return dataSourceByName.computeIfAbsent(dataSourceName, dsn -> {

            final DataSourceProperties props = dataSourcePropertiesRepository.getPropertiesForTenant(dsn);
            final DataSourceBuilder builder = DataSourceBuilder.create();

            builder.url(props.getUrl());
            builder.password(props.getPassword());
            builder.username(props.getUsername());

            return builder.build();
        });
    }
}
