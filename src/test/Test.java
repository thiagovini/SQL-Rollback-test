package test;

import db.DB;
import db.DbException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Test {

    public static void main(String[] args) throws SQLException {


        Connection connection = null;
        Statement statement = null;

        /*Start connection*/
        try{
            connection = DB.getConnection();
            statement = connection.createStatement();
            System.out.println("Connection successful");
        } catch (DbException e){
            throw new DbException(e.getMessage());
        }

        /*Start test with rollback*/
        try{
            connection.setAutoCommit(false);
            int rows1 = statement.executeUpdate("UPDATE seller SET BaseSalary = 2090 where DepartmentId = 1");

            /*Generating fake error*/
            int x = 1;
            if(x < 2){
                throw new SQLException("Fake error");
            }


            int rows2 = statement.executeUpdate("UPDATE seller SET BaseSalary = 3090 WHERE DepartmentId = 2");
            connection.commit();
        } catch (SQLException e){
            try {
                connection.rollback();
                throw new DbException("Transaction rolled back! Caused by: " + e.getMessage());
            } catch (SQLException e1){
                throw new DbException("Error trying to rollback! Caused by: " + e1.getMessage());
            }
        }

        /*Close connection and statement*/
        finally {
            DB.closeConnection();
            DB.closeStatement(statement);
        }
    }

}
