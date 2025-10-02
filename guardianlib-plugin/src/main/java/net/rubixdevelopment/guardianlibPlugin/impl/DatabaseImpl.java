package net.rubixdevelopment.guardianlibPlugin.impl;

/*
 Created and owned by FloorNectarine
 Created on 01/10/2025 at time 18:10
*/

import com.zaxxer.hikari.HikariDataSource;
import net.rubixdevelopment.guardianlibApi.interfaces.Database;
import net.rubixdevelopment.guardianlibApi.interfaces.Query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseImpl implements Database {
    private final HikariDataSource ds;
    public DatabaseImpl(HikariDataSource ds) { this.ds = ds; }
    @Override public Connection connection() throws SQLException { return ds.getConnection(); }
    @Override public PreparedStatement build(Query q) throws SQLException { return q.build(connection()); }
    @Override public Query sql() { return new QueryImpl(); }
}
