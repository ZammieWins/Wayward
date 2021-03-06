package net.wayward_realms.waywardskills.skill;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.combat.Combatant;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.skills.SkillBase;
import net.wayward_realms.waywardlib.skills.SkillType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;

public class ItemSkill extends SkillBase {

    public ItemSkill() {
        setName("Item");
        setCoolDown(0);
        setType(SkillType.SUPPORT_PERFORM);
    }

    @Override
    public boolean use(Player player) {
        return false;
    }

    public boolean use(Fight fight, Character attacking, Character defending, ItemStack weapon) {
        Player attackingPlayer = attacking.getPlayer().getPlayer();
        Player defendingPlayer = defending.getPlayer().getPlayer();
        if (weapon.getType().isEdible() || weapon.getType() == Material.POTION) {
            switch (weapon.getType()) {
                case GOLDEN_APPLE:
                    attackingPlayer.getInventory().removeItem(weapon);
                    defending.setHealth(Math.min(defending.getHealth() + 10, defending.getMaxHealth()));
                    defendingPlayer.setHealth(defending.getHealth());
                    fight.sendMessage(ChatColor.YELLOW + (attacking.isNameHidden() ? ChatColor.MAGIC + attacking.getName() + ChatColor.RESET : attacking.getName()) + ChatColor.YELLOW + " fed " + (defending.isNameHidden() ? ChatColor.MAGIC + defending.getName() + ChatColor.RESET : defending.getName()) + ChatColor.YELLOW + " a golden carrot, healing 5 HP.");
                    return true;
                case POTION:
                    if (weapon.hasItemMeta()) {
                        if (weapon.getItemMeta().hasDisplayName()) {
                            if (weapon.getItemMeta().getDisplayName().equalsIgnoreCase("Masheek")) {
                                attackingPlayer.getInventory().removeItem(weapon);
                                defending.setMana(Math.min(defending.getMana() + 5, defending.getMaxMana()));
                                fight.sendMessage(ChatColor.YELLOW + (attacking.isNameHidden() ? ChatColor.MAGIC + attacking.getName() + ChatColor.RESET : attacking.getName()) + ChatColor.YELLOW + " used a bottle of Masheek on " + (defending.isNameHidden() ? ChatColor.MAGIC + defending.getName() + ChatColor.RESET : defending.getName()) + ChatColor.YELLOW + ", replenishing 5 mana.");
                                return true;
                            }
                        }
                    }
                    attackingPlayer.getInventory().removeItem(weapon);
                    Potion potion = Potion.fromItemStack(weapon);
                    potion.apply(defendingPlayer);
                    fight.sendMessage(ChatColor.YELLOW + (attacking.isNameHidden() ? ChatColor.MAGIC + attacking.getName() + ChatColor.RESET : attacking.getName()) + ChatColor.YELLOW + " used a potion on " + (defending.isNameHidden() ? ChatColor.MAGIC + defending.getName() + ChatColor.RESET : defending.getName()));
                    return true;
                default:
                    fight.sendMessage(ChatColor.YELLOW + (attacking.isNameHidden() ? ChatColor.MAGIC + attacking.getName() + ChatColor.RESET : attacking.getName()) + ChatColor.YELLOW + " fed something to " + (defending.isNameHidden() ? ChatColor.MAGIC + defending.getName() + ChatColor.RESET : defending.getName()) + ChatColor.YELLOW + " but it had no effect.");
                    return true;
            }
        } else {
            fight.sendMessage(ChatColor.YELLOW + (attacking.isNameHidden() ? ChatColor.MAGIC + attacking.getName() + ChatColor.RESET : attacking.getName()) + ChatColor.YELLOW + " attempted to use a " + weapon.getType().toString().toLowerCase().replace('_', ' ') + " on " + (defending.isNameHidden() ? ChatColor.MAGIC + defending.getName() + ChatColor.RESET : defending.getName()) + ChatColor.YELLOW + " but it didn't seem to do anything.");
            return true;
        }
    }

    @Override
    public ItemStack getIcon() {
        ItemStack icon = new ItemStack(Material.POTION);
        ItemMeta iconMeta = icon.getItemMeta();
        iconMeta.setDisplayName("Item");
        icon.setItemMeta(iconMeta);
        return icon;
    }

    @Override
    public boolean canUse(net.wayward_realms.waywardlib.character.Character character) {
        return true;
    }

    @Override
    public boolean canUse(Combatant combatant) {
        return true;
    }

    @Override
    public boolean canUse(OfflinePlayer player) {
        return true;
    }

    @Override
    public String getDescription() {
        return "Uses an item in battle";
    }

}
