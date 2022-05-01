package io.hexlet.typoreporter.domain.account;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.EnumType;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class AuthProviderPgEnum extends EnumType<AuthProvider> {

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {
        st.setObject(index, value instanceof AuthProvider aps ? aps.name() : null, Types.OTHER);
    }
}
