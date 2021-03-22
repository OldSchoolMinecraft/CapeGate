package com.oldschoolminecraft.cg;

import org.bukkit.util.config.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Random;

public class Utils
{
    public static String generateCode()
    {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }

    public static PreparedStatement prepareStatement(String statement, String types, Object... values)
    {
        try
        {
            Configuration config = CapeGate.instance.getConfiguration();
            String sql_host = config.getString("sql_host", "localhost");
            String sql_username = config.getString("sql_username", "root");
            String sql_password = config.getString("sql_password", "");

            Connection con = DriverManager.getConnection(String.format("jdbc:mysql://%s/capegate?user=%s&password=%s", sql_host, sql_username, sql_password));

            PreparedStatement stmt = con.prepareStatement(statement);

            if (types.length() != values.length)
                throw new RuntimeException("Types and values do not match (do you have more values than types?)");

            for (int i = 0; i < values.length; i++)
            {
                char type = types.toLowerCase().charAt(i);
                switch (type)
                {
                    case 's':
                        stmt.setString(i + 1, (String) values[i]);
                        break;
                    case 'i':
                        stmt.setInt(i + 1, (int) values[i]);
                        break;
                    case 'b':
                        stmt.setBoolean(i + 1, (boolean) values[i]);
                        break;
                }
            }

            return stmt;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
