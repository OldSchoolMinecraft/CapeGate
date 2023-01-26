package com.oldschoolminecraft.cg.handlers;

import com.oldschoolminecraft.cg.CapeGate;
import com.oldschoolminecraft.cg.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import ru.tehkode.permissions.commands.Command;
import ru.tehkode.permissions.commands.CommandListener;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class CommandHandler implements CommandListener
{
    private CapeGate plugin;

    public CommandHandler(CapeGate plugin)
    {
        this.plugin = plugin;
    }

    @Command(name = "changecloak", syntax = "", description = "Get a cape code to use for uploading a cloak on the website.", permission = "capegate.use")
    public void onChangeCloak(final Plugin plugin, final CommandSender sender, final Map<String, String> args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(ChatColor.RED + "You must be a player to do this!");
            return;
        }

        Player ply = (Player) sender;

        Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, () ->
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
    }
}
