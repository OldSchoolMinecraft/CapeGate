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
            try
            {
                String code = Utils.generateCode();
                PreparedStatement stmt = Utils.prepareStatement("IF EXISTS(SELECT * FROM codes WHERE username = ?) UPDATE codes SET code = ? WHERE username = ? ELSE INSERT INTO codes (username, code) VALEUS (?, ?)", "sssss", ply.getName(), code, ply.getName(), ply.getName(), code);
                assert stmt != null;
                stmt.execute();
                ply.sendMessage(ChatColor.GRAY + "Your code is: " + ChatColor.GREEN + code);
            } catch (Exception ex) {
                ex.printStackTrace();
                sender.sendMessage(ChatColor.RED + "Something went wrong (CC-SQL-1). If this problem persists, please contact an administrator.");
            }
        });
    }
}
