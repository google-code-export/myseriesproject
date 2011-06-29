/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import Exceptions.DatabaseException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import myComponents.MyUsefulFunctions;
import tools.MySeriesLogger;

/**
 * The base class for SQLite Records
 * @author lordovol
 */
public class Record {

  private static boolean DBerror(Exception ex) {
    return ex.getMessage().equals("cannot commit transaction - SQL statements in progress")
        || ex.getMessage().equals("database locked");
  }

  /**
   * The default constructor
   */
  public Record() {
  }

  /**
   * Executes a query
   * @param sql The query to execute
   * @return either (1) the row count for SQL Data Manipulation Language (DML) statements
   *         or (2) 0 for SQL statements that return nothing
   * @throws SQLException
   */
  public static ResultSet query(Statement stmt, String sql) throws SQLException {
    ResultSet rs = stmt.executeQuery(sql);
    return rs;
  }

  /**
   * execute an update query
   * @param sql
   * @return the id of the inserted record or -1
   * @throws java.sql.SQLException
   */
//  public synchronized static int queryUpdate(Statement stmt, String sql) throws SQLException {
//    ResultSet rs = null;
//    try {
//      MySeriesLogger.logger.log(Level.INFO, "Running update query {0}", sql);
//      // System.out.println("save " + sql);
//      stmt.executeUpdate(sql);
//      rs = stmt.executeQuery("SELECT last_insert_rowid() AS id");
//      int ai;
//      if (rs.next()) {
//        ai = rs.getInt("id");
//        MySeriesLogger.logger.log(Level.FINE, "Row with id {0} was inserted", ai);
//      } else {
//        MySeriesLogger.logger.log(Level.FINE, "Row was updated or deleted");
//        ai = -1;
//      }
//      rs.close();
//      return ai;
//    } catch (SQLException ex) {
//      if (DBerror(ex)) {
//        return queryUpdate(stmt, sql);
//      } else {
//        // System.out.println("fail " + sql);
//        throw ex;
//      }
//    } finally {
//      if (rs != null) {
//        rs.close();
//      }
//
//    }
//  }

  protected synchronized int save(String table, String[] columns, String[] values, String whereClause, String[] whereValues) throws SQLException, DatabaseException {
    int ai = -1;
    Statement stmt = DBConnection.conn.createStatement();
    ResultSet rs = null;
    String sql;
    try {

      if (whereClause == null) {
        sql = "INSERT INTO `" + table
            + "`(" + joinSqlColumns(columns)
            + ") VALUES (" + joinSqlValues(values) + ")";
        stmt.executeUpdate(sql);
        rs = stmt.executeQuery("SELECT last_insert_rowid() AS id");
        if (rs.next()) {
          ai = rs.getInt("id");
          MySeriesLogger.logger.log(Level.FINE, "Row with id {0} was inserted", ai);
        }
      } else {
        sql = "UPDATE `" + table + "` SET "
            + createColumnUpdate(columns, values)
            + " WHERE " + createWhereClause(whereClause, whereValues);

        stmt.executeUpdate(sql);
      }
    } finally {
      if (rs != null) {
        rs.close();
      }
      stmt.close();
    }
    return ai;
  }

  protected synchronized void delete(String table, String whereClause,String[] whereValues) throws SQLException{
      Statement stmt = DBConnection.conn.createStatement();
      try{
      String sql = "DELETE FROM `" + table + "` " + "WHERE " + createWhereClause(whereClause, whereValues);
      stmt.executeUpdate(sql);
    } finally {
      stmt.close();
    }
  }

  protected static ResultSet query(String table, String[] columns, String whereClause, String[] whereValues, String group,String having, String order, String limit) throws SQLException{
    Statement stmt = DBConnection.conn.createStatement();
    String cols = columns == null ? "*" : joinSqlColumns(columns);
    String where = whereClause==null ? "" : " WHERE " + createWhereClause(whereClause, whereValues);
    group = group == null ? "" : " GROUP BY " +group;
    having = having == null ? "" : " HAVING " +having;
    order = order == null ? "" : " ORDER BY " + order;
    limit  = limit == null ? "" : " LIMIT " + limit;
    String sql = "SELECT " + cols + " FROM " + table + where + group + having + order + limit;
    return stmt.executeQuery(sql);
  }


  private static String createColumnUpdate(String[] columns, String[] values) throws DatabaseException {
    if (columns.length != values.length) {
      throw new DatabaseException("Columns and Values arrays have different lengths");
    }
    int n = columns.length;
    String query = "";
    for (int i = 0; i < n; i++) {
      if (i < n - 1) {
        query += "`" + columns[i] + "`='" + values[i] + "',";
      } else {
        query += "`" + columns[i] + "`='" + values[i] + "'";
      }
    }
    return query;
  }

  private static String createWhereClause(String whereClause, String[] whereValues) {
    int n = whereValues.length;
    for (int i = 0; i < n; i++) {
      whereClause = whereClause.replaceFirst("\\?", "'" + whereValues[i] + "'");
    }
    return whereClause;
  }

  /**
   * Joins values to build SQL query
   * @param values The values
   * @return 
   */
  private static String joinSqlValues(String[] values) {
    String query = "";
    int n = values.length;
    for (int i = 0; i < n; i++) {
      if (i < n - 1) {
        query += "'" + values[i] + "',";
      } else {
        query += "'" + values[i] + "'";
      }
    }
    return query;
  }

  /**
   * Joins columns for sql query
   * @param columns The columns
   * @return 
   */
  private static String joinSqlColumns(String[] columns) {
    String query = "";
    int n = columns.length;
    for (int i = 0; i < n; i++) {
      if (i < n - 1) {
        query += "`" + columns[i] + "`,";
      } else {
        query += "`" + columns[i] + "`";
      }
    }
    return query;
  }


}
