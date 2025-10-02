package net.rubixdevelopment.guardianlibApi.interfaces;

/*
 Created and owned by FloorNectarine
 Created on 01/10/2025 at time 17:16
*/

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface Database {
    Connection connection() throws SQLException;
    PreparedStatement build(Query query) throws SQLException;
    Query sql(); // builder start
}
