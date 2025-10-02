package net.rubixdevelopment.guardianlibPlugin.impl;

/*
 Created and owned by FloorNectarine
 Created on 01/10/2025 at time 18:17
*/

import net.rubixdevelopment.guardianlibApi.interfaces.Query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class QueryImpl implements Query {
    enum Kind { SELECT, INSERT, UPDATE, DELETE }
    private Kind k = Kind.SELECT;
    private String table;
    private final List<String> sel = new ArrayList<>();
    private final LinkedHashMap<String,Object> vals = new LinkedHashMap<>();
    private final LinkedHashMap<String,Object> sets = new LinkedHashMap<>();
    private final List<String> where = new ArrayList<>();
    private final List<Object> params = new ArrayList<>();
    private String order;
    private Integer limit;

    @Override public Query select(String... cols){ k=Kind.SELECT; sel.addAll(Arrays.asList(cols.length==0?new String[]{"*"}:cols)); return this; }
    @Override public Query insertInto(String t){ k=Kind.INSERT; table=t; return this; }
    @Override public Query update(String t){ k=Kind.UPDATE; table=t; return this; }
    @Override public Query deleteFrom(String t){ k=Kind.DELETE; table=t; return this; }
    @Override public Query from(String t){ table=t; return this; }
    @Override public Query values(String c, Object v){ vals.put(c,v); return this; }
    @Override public Query set(String c, Object v){ sets.put(c,v); return this; }
    @Override public Query where(String c, String op, Object v){ where.add(c+" "+op+" ?"); params.add(v); return this; }
    @Override public Query andRaw(String raw, Object... vs){ where.add(raw); params.addAll(Arrays.asList(vs)); return this; }
    @Override public Query orderBy(String o){ order=o; return this; }
    @Override public Query limit(int n){ limit=n; return this; }

    @Override public PreparedStatement build(Connection c) throws SQLException {
        String sql;
        List<Object> bind = new ArrayList<>();
        switch (k) {
            case SELECT -> {
                sql = "SELECT "+(sel.isEmpty()?"*":String.join(",", sel))+" FROM "+table
                        +(where.isEmpty()?"":" WHERE "+String.join(" AND ", where))
                        +(order==null?"":" ORDER BY "+order)
                        +(limit==null?"":" LIMIT "+limit);
                bind.addAll(params);
            }
            case INSERT -> {
                String cols = String.join(",", vals.keySet());
                String qs = String.join(",", Collections.nCopies(vals.size(), "?"));
                sql = "INSERT INTO "+table+" ("+cols+") VALUES ("+qs+")";
                bind.addAll(vals.values());
            }
            case UPDATE -> {
                String setsSql = String.join(",", sets.keySet().stream().map(k->k+"=?").toList());
                sql = "UPDATE "+table+" SET "+setsSql
                        +(where.isEmpty()?"":" WHERE "+String.join(" AND ", where))
                        +(limit==null?"":" LIMIT "+limit);
                bind.addAll(sets.values()); bind.addAll(params);
            }
            case DELETE -> {
                sql = "DELETE FROM "+table
                        +(where.isEmpty()?"":" WHERE "+String.join(" AND ", where))
                        +(limit==null?"":" LIMIT "+limit);
                bind.addAll(params);
            }
            default -> throw new SQLException("unknown");
        }
        PreparedStatement ps = (k==Kind.INSERT) ? c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS) : c.prepareStatement(sql);
        for (int i=0;i<bind.size();i++) ps.setObject(i+1, bind.get(i));
        return ps;
    }
}
