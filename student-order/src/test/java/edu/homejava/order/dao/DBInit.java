package edu.homejava.order.dao;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

public class DBInit {
    public static void CreateTables() throws Exception {
        // Получаем ссылку на файл через Uniform Resource Locator.
        URL url1 = DbAccessObj_ImplTest.class.getClassLoader().getResource("student_project.sql");
        URL url2 = DbAccessObj_ImplTest.class.getClassLoader().getResource("student_data.sql");

        List<String> str1 = Files.readAllLines(Paths.get(url1.toURI()));
        String sql1 = str1.stream().collect(Collectors.joining());

        List<String> str2 = Files.readAllLines(Paths.get(url2.toURI()));
        String sql2 = str2.stream().collect(Collectors.joining());

        try(Connection con = ConnectionBuilder.getConnection();
        Statement stmt = con.createStatement()) {
        //stmt.executeUpdate(sql1);
        //stmt.executeUpdate(sql2);
        }
    }
}

