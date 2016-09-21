package cz.plachta11b.costlyenchanting;

import java.util.logging.Level;
import java.util.regex.Pattern;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.permissions.PermissionAttachmentInfo;

class Events implements Listener
{

	private final CostlyEnchanting plugin;

	Events(final CostlyEnchanting plugin) {
		this.plugin = plugin;
	}

	// this function is called before enchanting cost taken from player
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void EnchantItemEvent(EnchantItemEvent event) {

		Player enchanter = event.getEnchanter();

		int enchantCost = event.getExpLevelCost();

		int enchanterLevelBefore = enchanter.getLevel();

		// this not work properly - probably bug in bukkit code
		// still cost 1 to 3 levels
		event.setExpLevelCost(0);

		/* hack enchanting 1.8 cost*/
		int bugCompensationLevel;

		if (enchantCost <= 10) {
			// give back 1 level
			bugCompensationLevel = 1;
		} else if (enchantCost <= 20) {
			// give back 2 level
			bugCompensationLevel = 2;
		} else {
			// give back 3 level
			bugCompensationLevel = 3;
		}
		/* end of hack */

		// look for permission
		double playerPercentage = getPercentageLevelCost(enchanter);

		boolean takeAllLevels = false;

		String round = this.plugin.config.getRoundMethod();
		int percentage = this.plugin.config.getPercentageCost();
		int newEnchantCost = enchantCost;

		if (playerPercentage != Double.POSITIVE_INFINITY) {
			if (playerPercentage != Double.MAX_VALUE) {
				percentage = (int) playerPercentage;
				this.plugin.printMessage("Configure cost by permission: " + percentage + "%", true);
			} else {
				takeAllLevels = true;
				this.plugin.printMessage("Take all levels from player (set by permission)", true);
			}
		} else {
			this.plugin.printMessage("Player have no permission for cost configuration", true);
		}

		if (round.equalsIgnoreCase("floor")) {
			newEnchantCost = (int) Math.floor(enchantCost * (percentage / 100.0));
		} else if (round.equalsIgnoreCase("round")) {
			newEnchantCost = (int) Math.round(enchantCost * (percentage / 100.0));
		} else {
			newEnchantCost = (int) Math.ceil(enchantCost * (percentage / 100.0));
		}

		this.plugin.printMessage("Player level: " + enchanterLevelBefore + " cost: " + newEnchantCost, true);
		int enchanterLevelAfter = enchanterLevelBefore - newEnchantCost;

		if ((enchanterLevelAfter + bugCompensationLevel) < 0) {
			event.setCancelled(true);
			enchanter.sendMessage("§4You need: " + newEnchantCost + " levels for this enchant");;
			return;
		}

		// do not set negative levels
		if ((enchanterLevelAfter + bugCompensationLevel) > 0 && takeAllLevels == false) {
			enchanter.setLevel(enchanterLevelAfter + bugCompensationLevel);
		} else {
			enchanter.setLevel(0);
		}

		enchanter.updateInventory();
	}
	

	public double getPercentageLevelCost(Player player) {
	
		double minimumPercentageCost = Double.POSITIVE_INFINITY;
	
		for (PermissionAttachmentInfo permission: player.getEffectivePermissions()) {

			String permissionString = permission.getPermission();

			if (permissionString.startsWith("costlyenchanting.enchant.cost.")) {
				String[] value = permissionString.split(Pattern.quote("."));

				if (value[3].equalsIgnoreCase("all")) {
					minimumPercentageCost = Math.min(minimumPercentageCost, Double.MAX_VALUE);
				} else {
					int percentageCostPermission = Integer.parseInt(value[3]);

					minimumPercentageCost = Math.min(percentageCostPermission, minimumPercentageCost);
				}
			}
		}

		return minimumPercentageCost;
	}
}
