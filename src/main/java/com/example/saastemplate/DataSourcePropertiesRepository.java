package com.example.saastemplate;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.stereotype.Repository;

@Repository
public interface DataSourcePropertiesRepository {

    DataSourceProperties getPropertiesForTenant(String tenant);


}
