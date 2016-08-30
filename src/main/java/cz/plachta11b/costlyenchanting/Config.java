package cz.plachta11b.costlyenchanting;

import java.io.File;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;

class PluginConfig {

	private Configuration configuration;

	private final File configurationFile;

	PluginConfig(final CostlyEnchanting plugin) {

		plugin.saveDefaultConfig();
		plugin.saveConfig();

		configuration = plugin.getConfig();
		configurationFile = new File(plugin.getDataFolder(), "config.yml");
	}

	public boolean reload() {
		configuration = YamlConfiguration.loadConfiguration(configurationFile);

		return true;
	}

	public int getPercentageCost() {
		return this.configuration.getInt("enchant.cost");
	}

	public String getRoundMethod() {
		return this.configuration.getString("enchant.round");
	}
	
    public boolean isDebuggingAllowed() {
        return this.configuration.getBoolean("debug");
    }
}