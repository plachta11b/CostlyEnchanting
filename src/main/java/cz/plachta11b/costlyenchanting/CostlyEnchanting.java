package cz.plachta11b.costlyenchanting;

import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

public class CostlyEnchanting extends JavaPlugin {

    public PluginConfig config;
    public PluginCommands commands;

    @Override
    public void onEnable() {
        this.config = new PluginConfig(this);
        this.commands = new PluginCommands(this);

        if (this.config.isDebuggingAllowed() == true) {
            String version = this.getDescription().getVersion();
            this.getLogger().log(Level.INFO, "Debugging enabled by config on version: " + version);
        }

        this.getCommand("cereload").setExecutor(this.commands);

		this.getServer().getPluginManager().registerEvents(new Events(this), this);
    }
    
    public boolean reload() {

        if (this.config.reload()) {
            return true;
        }

        return false;
    }
}