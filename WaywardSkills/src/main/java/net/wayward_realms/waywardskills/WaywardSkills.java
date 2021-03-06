package net.wayward_realms.waywardskills;

import net.wayward_realms.waywardlib.skills.Skill;
import net.wayward_realms.waywardlib.skills.SkillsPlugin;
import net.wayward_realms.waywardlib.skills.Spell;
import net.wayward_realms.waywardlib.util.file.filter.YamlFileFilter;
import net.wayward_realms.waywardskills.skill.SkillManager;
import net.wayward_realms.waywardskills.spell.SpellManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class WaywardSkills extends JavaPlugin implements SkillsPlugin {

    private SpellManager spellManager;
    private SkillManager skillManager;

    @Override
    public void onEnable() {
        spellManager = new SpellManager(this);
        skillManager = new SkillManager(this);
        registerListeners(new PlayerInteractListener(this), new EntityDamageByEntityListener(this), new EntityTargetListener(this), new ProjectileHitListener(), new InventoryClickListener());
        getCommand("skill").setExecutor(new SkillCommand(this));
        getCommand("spell").setExecutor(new SpellCommand(this));
        getCommand("bindskill").setExecutor(new BindSkillCommand(this));
        getCommand("bindspell").setExecutor(new BindSpellCommand(this));
        getCommand("skillinfo").setExecutor(new SkillInfoCommand(this));
        getCommand("spellinfo").setExecutor(new SpellInfoCommand(this));
        setupMageArmourChecker();
    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    @Override
    public Spell getSpell(String name) {
        return spellManager.getSpell(name);
    }

    @Override
    public void addSpell(Spell spell) {
        spellManager.addSpell(spell);
    }

    @Override
    public void removeSpell(Spell spell) {
        spellManager.removeSpell(spell);
    }

    @Override
    public Collection<? extends Spell> getSpells() {
        return spellManager.getSpells();
    }

    @Override
    public Skill getSkill(String name) {
        return skillManager.getSkill(name);
    }

    @Override
    public void addSkill(Skill skill) {
        skillManager.addSkill(skill);
    }

    @Override
    public void removeSkill(Skill skill) {
        skillManager.removeSkill(skill);
    }

    @Override
    public Collection<? extends Skill> getSkills() {
        return skillManager.getSkills();
    }

    @SuppressWarnings("resource")
    @Override
    public Skill loadSkill(File file) {
        try {
            JarFile jarFile;
            jarFile = new JarFile(file);
            Enumeration<JarEntry> entries = jarFile.entries();
            String mainClass = null;
            while (entries.hasMoreElements()) {
                JarEntry element = entries.nextElement();
                if (element.getName().equalsIgnoreCase("skill.yml")) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(jarFile.getInputStream(element)));
                    mainClass = reader.readLine();
                    if (mainClass != null) {
                        mainClass = mainClass.substring(6);
                    }
                    break;
                }
            }
            if (mainClass != null) {
                ClassLoader loader = URLClassLoader.newInstance(new URL[]{file.toURI().toURL()}, this.getClass().getClassLoader());
                Class<?> clazz = Class.forName(mainClass, true, loader);
                for (Class<?> subclazz : clazz.getClasses()) {
                    Class.forName(subclazz.getName(), true, loader);
                }
                Class<? extends Skill> skillClass = clazz.asSubclass(Skill.class);
                Skill skill = skillClass.newInstance();
                if (skill instanceof Spell) {
                    addSpell((Spell) skill);
                } else {
                    addSkill(skill);
                }
                return skill;
            } else {
                getLogger().severe("Main class not found for skill: " + file.getName());
            }
        } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @Override
    public Skill loadSkill(Class<? extends Skill> clazz) {
        try {
            Skill skill = clazz.newInstance();
            if (skill instanceof Spell) {
                addSpell((Spell) skill);
            } else {
                addSkill(skill);
            }
            return skill;
        } catch (InstantiationException | IllegalAccessException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @Override
    public String getPrefix() {
        return "";
    }

    @Override
    public void loadState() {
        File skillDirectory = new File(getDataFolder(), "skills");
        if (skillDirectory.exists()) {
            for (File file : skillDirectory.listFiles(new YamlFileFilter())) {
                loadSkill(file);
            }
        }
    }

    @Override
    public void saveState() {

    }

    public SkillManager getSkillManager() {
        return skillManager;
    }

    public SpellManager getSpellManager() {
        return spellManager;
    }

    public void setupMageArmourChecker() {
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                for (Player player : getServer().getOnlinePlayers()) {
                    ItemStack helmet = player.getInventory().getHelmet();
                    if (helmet != null) {
                        if (helmet.hasItemMeta()) {
                            if (helmet.getItemMeta().hasDisplayName()) {
                                if (helmet.getItemMeta().getDisplayName().equals("Mage armour helmet")) {
                                    if (helmet.getItemMeta().hasLore()) {
                                        try {
                                            if (System.currentTimeMillis() - Long.parseLong(helmet.getItemMeta().getLore().get(0)) >= 45000L) {
                                                player.getInventory().setHelmet(null);
                                            }
                                        } catch (NumberFormatException ignore) {
                                            ItemMeta helmetMeta = helmet.getItemMeta();
                                            List<String> lore = new ArrayList<>();
                                            lore.add("" + System.currentTimeMillis());
                                            helmetMeta.setLore(lore);
                                            helmet.setItemMeta(helmetMeta);
                                        }
                                    } else {
                                        ItemMeta helmetMeta = helmet.getItemMeta();
                                        List<String> lore = new ArrayList<>();
                                        lore.add("" + System.currentTimeMillis());
                                        helmetMeta.setLore(lore);
                                        helmet.setItemMeta(helmetMeta);
                                    }
                                }
                            }
                        }
                    }
                    ItemStack chestplate = player.getInventory().getChestplate();
                    if (chestplate != null) {
                        if (chestplate.hasItemMeta()) {
                            if (chestplate.getItemMeta().hasDisplayName()) {
                                if (chestplate.getItemMeta().getDisplayName().equals("Mage armour chestplate")) {
                                    if (chestplate.getItemMeta().hasLore()) {
                                        try {
                                            if (System.currentTimeMillis() - Long.parseLong(chestplate.getItemMeta().getLore().get(0)) >= 45000L) {
                                                player.getInventory().setChestplate(null);
                                            }
                                        } catch (NumberFormatException ignore) {
                                            ItemMeta chestplateMeta = chestplate.getItemMeta();
                                            List<String> lore = new ArrayList<>();
                                            lore.add("" + System.currentTimeMillis());
                                            chestplateMeta.setLore(lore);
                                            chestplate.setItemMeta(chestplateMeta);
                                        }
                                    } else {
                                        ItemMeta chestplateMeta = chestplate.getItemMeta();
                                        List<String> lore = new ArrayList<>();
                                        lore.add("" + System.currentTimeMillis());
                                        chestplateMeta.setLore(lore);
                                        chestplate.setItemMeta(chestplateMeta);
                                    }
                                }
                            }
                        }
                    }
                    ItemStack leggings = player.getInventory().getLeggings();
                    if (leggings != null) {
                        if (leggings.hasItemMeta()) {
                            if (leggings.getItemMeta().hasDisplayName()) {
                                if (leggings.getItemMeta().getDisplayName().equals("Mage armour leggings")) {
                                    if (leggings.getItemMeta().hasLore()) {
                                        try {
                                            if (System.currentTimeMillis() - Long.parseLong(leggings.getItemMeta().getLore().get(0)) >= 45000L) {
                                                player.getInventory().setLeggings(null);
                                            }
                                        } catch (NumberFormatException ignore) {
                                            ItemMeta leggingsMeta = leggings.getItemMeta();
                                            List<String> lore = new ArrayList<>();
                                            lore.add("" + System.currentTimeMillis());
                                            leggingsMeta.setLore(lore);
                                            leggings.setItemMeta(leggingsMeta);
                                        }
                                    } else {
                                        ItemMeta leggingsMeta = leggings.getItemMeta();
                                        List<String> lore = new ArrayList<>();
                                        lore.add("" + System.currentTimeMillis());
                                        leggingsMeta.setLore(lore);
                                        leggings.setItemMeta(leggingsMeta);
                                    }
                                }
                            }
                        }
                    }
                    ItemStack boots = player.getInventory().getBoots();
                    if (boots != null) {
                        if (boots.hasItemMeta()) {
                            if (boots.getItemMeta().hasDisplayName()) {
                                if (boots.getItemMeta().getDisplayName().equals("Mage armour boots")) {
                                    if (boots.getItemMeta().hasLore()) {
                                        try {
                                            if (System.currentTimeMillis() - Long.parseLong(boots.getItemMeta().getLore().get(0)) >= 45000L) {
                                                player.getInventory().setBoots(null);
                                            }
                                        } catch (NumberFormatException ignore) {
                                            ItemMeta bootsMeta = boots.getItemMeta();
                                            List<String> lore = new ArrayList<>();
                                            lore.add("" + System.currentTimeMillis());
                                            bootsMeta.setLore(lore);
                                            boots.setItemMeta(bootsMeta);
                                        }
                                    } else {
                                        ItemMeta bootsMeta = boots.getItemMeta();
                                        List<String> lore = new ArrayList<>();
                                        lore.add("" + System.currentTimeMillis());
                                        bootsMeta.setLore(lore);
                                        boots.setItemMeta(bootsMeta);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }, 100L, 100L);
    }

}
