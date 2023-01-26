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
            Connection con = CapeGate.instance.sqlPool.getConnection();

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
