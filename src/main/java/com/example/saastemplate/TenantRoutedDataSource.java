package com.example.saastemplate;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@RequiredArgsConstructor
public class TenantRoutedDataSource extends AbstractRoutingDataSource {

    private final MultiTennantDatasourceLookup datasourceLookup;

    @Override
    public Object determineCurrentLookupKey() {
        return TenantContextHolder.getTenant();
    }

    @Override
    public DataSource determineTargetDataSource() {
        return datasourceLookup.getDataSource((String) determineCurrentLookupKey());
    }

}
