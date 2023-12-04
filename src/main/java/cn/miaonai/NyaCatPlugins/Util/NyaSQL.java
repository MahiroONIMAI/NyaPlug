package cn.miaonai.NyaCatPlugins.Util;

import cn.miaonai.NyaCatPlugins.NyaCatPlugins;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class NyaSQL {
    static  String host = NyaCatPlugins.host;
    static String database = NyaCatPlugins.database;
    static String password = NyaCatPlugins.password;
    static String username = NyaCatPlugins.username;
    static int port = NyaCatPlugins.port;
    public static Connection connection;


    public static void openConnection() throws SQLException, ClassNotFoundException {
        synchronized (NyaSQL.class) {
            if (connection == null || connection.isClosed()) {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    connection = DriverManager.getConnection(
                            "jdbc:mysql://" + host + ":" + port + "/" + database,
                            username,
                            password
                    );
                } catch (SQLException | ClassNotFoundException e) {
                    // 处理异常或根据需要重新抛出
                    e.printStackTrace();
                }
            }
        }
    }

    // 在每次执行后调用这个方法来关闭连接
    public static void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

}
