package com.oldschoolminecraft.cg;

import org.bukkit.util.config.Configuration;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class PLConfig extends Configuration
{
    public PLConfig()
    {
        super(new File("plugins/CapeGate/config.yml"));
        this.reload();
    }

    public void reload()
    {
        this.load();
        this.write();
        this.save();
    }

    private void write()
    {
        generateConfigOption("mysql.host", "localhost");
        generateConfigOption("mysql.port", 3306);
        generateConfigOption("mysql.user", "root");
        generateConfigOption("mysql.pass", "changeme");
        generateConfigOption("mysql.database", "capegate");
    }

    private void generateConfigOption(String key, Object defaultValue)
    {
        if (this.getProperty(key) == null) this.setProperty(key, defaultValue);
        final Object value = this.getProperty(key);
        this.removeProperty(key);
        this.setProperty(key, value);
    }

    public Object getConfigOption(String key)
    {
        return this.getProperty(key);
    }

    public Object getConfigOption(String key, Object defaultValue)
    {
        Object value = getConfigOption(key);
        if (value == null) value = defaultValue;
        return value;
    }

    public List<String> getConfigList(String key)
    {
        return Arrays.asList(String.valueOf(getConfigOption(key, "")).trim().split(","));
    }
}
