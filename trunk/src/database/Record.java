/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import myseries.MySeries;
import myseries.episodes.NextEpisodes;

/**
 * The base class for SQLite Records
 * @author lordovol
 */
public class Record {

  private Connection conn;
  public static Statement stmt;

  /**
   * The default constructor
   */
  Record() {
    try {
      this.conn = DBConnection.conn;
      Record.stmt = conn.createStatement();
    } catch (SQLException ex) {
      MySeries.logger.log(Level.SEVERE, "Could not create the connection to the database", ex);
    }

  }

  /**
   * Executes a query
   * @param sql The query to execute
   * @return either (1) the row count for SQL Data Manipulation Language (DML) statements
   *         or (2) 0 for SQL statements that return nothing
   * @throws SQLException
   */
  public static ResultSet query(String sql) throws SQLException {
    return stmt.executeQuery(sql);
  }

  /**
   * execute an update query
   * @param sql
   * @return the id of the inserted record or -1
   * @throws java.sql.SQLException
   */
  public static int queryUpdate(String sql) throws SQLException {
    ResultSet rs = null;
    try {
      stmt.executeUpdate(sql);
      rs = stmt.executeQuery("SELECT last_insert_rowid() AS id");
      int ai;
      if (rs.next()) {
        ai = rs.getInt("id");
      } else {
        ai = -1;
      }
      rs.close();
      NextEpisodes.update();
      return ai;
    } finally {
      if (rs != null) {
        rs.close();
      }

    }
  }
}
