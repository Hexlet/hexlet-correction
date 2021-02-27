package io.hexlet.typoreporter.domain.typo;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.EnumType;

import java.sql.*;

public class TypoStatusPgEnum extends EnumType<TypoStatus> {

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {
        st.setObject(index, value instanceof TypoStatus ts ? ts.name() : null, Types.OTHER);
    }
}
