package io.hexlet.typoreporter.test;

import org.dbunit.dataset.datatype.DataType;
import org.dbunit.dataset.datatype.DataTypeException;
import org.dbunit.ext.postgresql.PostgresqlDataTypeFactory;
import org.springframework.context.annotation.Configuration;

import static java.sql.Types.OTHER;

@Configuration
public class DBUnitEnumPostgres extends PostgresqlDataTypeFactory {

    @Override
    public DataType createDataType(int sqlType, String sqlTypeName) throws DataTypeException {
        return super.createDataType(isEnumType(sqlTypeName) ? OTHER : sqlType, sqlTypeName);
    }

    @Override
    public boolean isEnumType(String sqlTypeName) {
        return "typo_status".equalsIgnoreCase(sqlTypeName) || "auth_provider".equalsIgnoreCase(sqlTypeName);
    }
}
