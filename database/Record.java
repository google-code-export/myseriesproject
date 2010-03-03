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

/**
 *
 * @author lordovol
 */
public class Record {

  private Connection conn;
  public static Statement stmt;

  Record() {
    try {
      this.conn = DBConnection.conn;
      Record.stmt = conn.createStatement();
    } catch (SQLException ex) {
      MySeries.logger.log(Level.SEVERE, "Could not create the connection to the database", ex);
    }

  }

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
      return ai;
    } finally {
      if (rs != null) {
        rs.close();
      }

    }
  }
}
