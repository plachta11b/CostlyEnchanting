package cz.plachta11b.costlyenchanting;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;

class Events implements Listener
{

	private final CostlyEnchanting plugin;

	Events(final CostlyEnchanting plugin) {
		this.plugin = plugin;
	}

	// this function is called before enchanting cost taken from player
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onSignChange(EnchantItemEvent event) {

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

		String round = this.plugin.config.getRoundMethod();
		float percentage = this.plugin.config.getPercentageCost();
		int newEnchantCost = enchantCost;

		if (round.equalsIgnoreCase("floor")) {
			newEnchantCost = (int) Math.floor(enchantCost * (percentage / 100));
		} else if (round.equalsIgnoreCase("round")) {
			newEnchantCost = Math.round(enchantCost * (percentage / 100));
		} else {
			newEnchantCost = (int) Math.ceil(enchantCost * (percentage / 100));
		}

		int enchanterLevelAfter = enchanterLevelBefore - newEnchantCost;

		// do not set negative levels
		if ((enchanterLevelAfter + bugCompensationLevel) > 0) {
			enchanter.setLevel(enchanterLevelAfter + bugCompensationLevel);
		} else {
			enchanter.setLevel(0);
		}

		enchanter.updateInventory();
	}
}
