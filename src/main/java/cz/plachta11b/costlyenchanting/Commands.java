package cz.plachta11b.costlyenchanting;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

class PluginCommands implements CommandExecutor {

	private final CostlyEnchanting plugin;

	public PluginCommands (CostlyEnchanting plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (sender.hasPermission("CostlyEnchanting.reload") || sender.isOp()) {
			sender.sendMessage(ChatColor.GREEN + "Reloading config...");

            if (plugin.reload()) {
            	sender.sendMessage(ChatColor.GREEN + "Config reloaded!");
            } else {
            	sender.sendMessage(ChatColor.RED + "An error occurred while reloading config!");
            }
        } else {
        	sender.sendMessage(ChatColor.RED + "You do not have permission to use /cereload (node: CostlyEnchanting.reload)");
        }

		return true;
	}
}