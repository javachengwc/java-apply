package demo;

import java.sql.*;

/**
 * Phoenix访问hbase的例子
 * 前置条件:
 * 需要把hbase所有节点的ip,hostname添加到运行程序机器的hosts中
 */
public class SimplePhoenix {

    public static void main(String[] args) throws Exception {
        Statement stmt = null;
        ResultSet rset = null;

        String driver = "org.apache.phoenix.jdbc.PhoenixDriver";
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection con = DriverManager.getConnection("jdbc:phoenix:192.168.27.129:2181");
        //stmt = con.createStatement();

        //stmt.executeUpdate("create table jtest (id integer primary key, content varchar)");
        //stmt.executeUpdate("upsert into jtest values (1,'hello')");
        //stmt.executeUpdate("upsert into jtest values (2,'world')");
        //con.commit();

        PreparedStatement statement = con.prepareStatement("select * from jtest");
        rset = statement.executeQuery();
        while (rset.next()) {
            System.out.println(rset.getInt("id")+" "+rset.getString("content"));
        }
        statement.close();
        con.close();
    }
}
