package net.rubixdevelopment.guardianlibApi.interfaces;

/*
 Created and owned by FloorNectarine
 Created on 01/10/2025 at time 17:17
*/

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface Query {
    Query select(String... cols);
    Query insertInto(String table);
    Query update(String table);
    Query deleteFrom(String table);
    Query from(String table);
    Query values(String col, Object val);          // voor insert
    Query set(String col, Object val);             // voor update
    Query where(String col, String op, Object val);
    Query andRaw(String raw, Object... vals);
    Query orderBy(String order);
    Query limit(int n);
    PreparedStatement build(Connection c) throws SQLException;
}
