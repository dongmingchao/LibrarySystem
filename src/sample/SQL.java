package sample;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class SQL {
    // JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:8889/LibrarySystem";

    // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "node";
    static final String PASS = "cxz,./123";

    public static HashMap<String,ArrayList<String>> select(String sql){
        Connection conn = null;
        Statement stmt = null;
        HashMap<String,ArrayList<String>> res = null;
        try{
            // 注册 JDBC 驱动
            Class.forName("com.mysql.jdbc.Driver");

            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            // 执行查询
            System.out.println(" 实例化Statement对...");
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData data = rs.getMetaData();
            res = new HashMap<>();
            // 展开结果集数据库
            for (int i = 1; i <= data.getColumnCount(); i++) {
                ArrayList<String> e = new ArrayList<>();
                e.add(data.getColumnClassName(i));
                res.put(data.getColumnName(i),e);
            }
            while (rs.next()){
                for (int i = 1; i <= data.getColumnCount(); i++) {
                    res.get(data.getColumnName(i)).add(rs.getString(data.getColumnName(i)));
                }
            }
//            res.entrySet().forEach(System.out::println);
            // 完成后关闭
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException ignored){
            }// 什么都不做
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        return res;
    }
    public static int update(String sql, Object[] o){
        Connection conn = null;
        Statement stmt = null;
        int res = -1;
        try{
            // 注册 JDBC 驱动
            Class.forName("com.mysql.jdbc.Driver");

            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            // 执行查询
            System.out.println(" 实例化Statement对...");
            stmt = conn.prepareStatement(sql);
            for (int i = 0; i < o.length; i++) {
                if (o[i] instanceof Integer) ((PreparedStatement) stmt).setInt(i+1, ((int) o[i]));
                if (o[i] instanceof String) ((PreparedStatement) stmt).setString(i+1, (String) o[i]);
            }
            res =  ((PreparedStatement) stmt).executeUpdate();
            //executeUpdate(sql)的返回值是更新的条数（更新的记录数量）。返回值是一个整数。
            //=============================
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException ignored){
            }// 什么都不做
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        return res;
    }
    public static int update(String sql){
        Connection conn = null;
        Statement stmt = null;
        int res = -1;
        try{
            // 注册 JDBC 驱动
            Class.forName("com.mysql.jdbc.Driver");

            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            // 执行查询
            System.out.println(" 实例化Statement对...");
            stmt = conn.prepareStatement(sql);
            res =  ((PreparedStatement) stmt).executeUpdate();
            //executeUpdate(sql)的返回值是更新的条数（更新的记录数量）。返回值是一个整数。
            //=============================
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException ignored){
            }// 什么都不做
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        return res;
    }

    public static void main(String[] args) {
//        int res = update("");
//        System.out.println(res);
    }
}
