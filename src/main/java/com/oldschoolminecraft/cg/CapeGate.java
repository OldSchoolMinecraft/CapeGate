package com.oldschoolminecraft.cg;

import com.oldschoolminecraft.cg.handlers.CommandHandler;
import org.bukkit.plugin.java.JavaPlugin;
import ru.tehkode.permissions.commands.CommandsManager;

public class CapeGate extends JavaPlugin
{
    public static CapeGate instance;

    private CommandsManager commandsManager;

    public void onEnable()
    {
        instance = this;

        commandsManager = new CommandsManager(this);
        commandsManager.register(new CommandHandler(this));

        System.out.println("CapeGate enabled.");
    }

    public void onDisable()
    {
        System.out.println("CapeGate disabled.");
    }
}
