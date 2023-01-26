package com.oldschoolminecraft.cg;

import com.oldschoolminecraft.cg.handlers.CommandHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ru.tehkode.permissions.commands.CommandsManager;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CapeGate extends JavaPlugin
{
    public static CapeGate instance;

    public PLConfig config;
    public MySQLConnectionPool sqlPool;
    private CommandsManager commandsManager;

    public void onEnable()
    {
        instance = this;
        config = new PLConfig();

        try
        {
            String url = "jdbc:mysql://" + config.getString("mysql.host") + ":" + config.getInt("mysql.port", 3306) + "/" + config.getString("mysql.database");
            sqlPool = new MySQLConnectionPool(url, config.getString("mysql.user"), config.getString("mysql.pass"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        commandsManager = new CommandsManager(this);
        commandsManager.register(new CommandHandler(this));

        System.out.println("CapeGate enabled.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (!command.getName().equalsIgnoreCase("changecloak")) return false;

        if (!(sender instanceof Player))
        {
            sender.sendMessage(ChatColor.RED + "You must be a player to do this!");
            return true;
        }

        Player ply = (Player) sender;

        Bukkit.getScheduler().scheduleAsyncDelayedTask(this, () ->
        {
            String code = Utils.generateCode();

            try (PreparedStatement stmt = Utils.prepareStatement("INSERT INTO codes (username, code) VALUES (?, ?) ON DUPLICATE KEY UPDATE code = ?", "sss", ply.getName(), code, code))
            {
                stmt.execute();
                ply.sendMessage(ChatColor.GRAY + "Your code is: " + ChatColor.GREEN + code);
            } catch (SQLException ex) {
                ply.sendMessage(ChatColor.RED + "SQL Error: " + ex.getMessage());
            }
        });

        return true;
    }

    public void onDisable()
    {
        System.out.println("CapeGate disabled.");
    }
}
