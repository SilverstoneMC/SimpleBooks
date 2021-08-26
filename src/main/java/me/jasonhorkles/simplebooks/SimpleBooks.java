package me.jasonhorkles.simplebooks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings({"unused", "ConstantConditions"})
public class SimpleBooks extends JavaPlugin implements CommandExecutor {

    public static DataManager data;

    @Override
    public void onEnable() {
        data = new DataManager(this);

        data.saveDefaultConfig();

        getCommand("loadbook").setTabCompleter(new TabComplete());
        getCommand("forcebook").setTabCompleter(new TabComplete());
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        switch (cmd.getName().toLowerCase()) {
            case "savebook" -> {
                if (!(sender instanceof Player player)) {
                    sender.sendMessage(ChatColor.RED + "Sorry, but only players can do that.");
                    return true;
                }

                if (args.length > 0) {
                    data.getConfig().set(args[0], player.getInventory().getItemInMainHand().getItemMeta());
                    data.saveConfig();
                } else {
                    return false;
                }
            }

            case "loadbook" -> {
                ItemStack item = new ItemStack(Material.WRITTEN_BOOK, 1);

                if (args.length > 0) {
                    item.setItemMeta((ItemMeta) data.getConfig().get(args[0]));

                    if (args.length == 1) {
                        if (!(sender instanceof Player player)) {
                            sender.sendMessage(ChatColor.RED + "Please specify a player.");
                            return true;
                        }

                        player.getInventory().addItem(item);
                    } else {
                        Player player = Bukkit.getPlayer(args[1]);
                        if (player == null) {
                            sender.sendMessage(ChatColor.RED + "Please provide an online player!");
                            return true;
                        }

                        player.getInventory().addItem(item);
                    }

                } else {
                    return false;
                }
            }

            case "forcebook" -> {
                ItemStack item = new ItemStack(Material.WRITTEN_BOOK, 1);

                if (args.length > 1) {
                    item.setItemMeta((ItemMeta) data.getConfig().get(args[0]));

                    Player player = Bukkit.getPlayer(args[1]);
                    if (player == null) {
                        sender.sendMessage(ChatColor.RED + "Please provide an online player!");
                        return true;
                    }

                    player.openBook(item);

                } else {
                    return false;
                }
            }

            case "listbooks" -> {
                sender.sendMessage(ChatColor.DARK_GREEN + "Available books:");
                for (String book : data.getConfig().getKeys(false)) {
                    sender.sendMessage(ChatColor.DARK_AQUA + book);
                }
            }
        }
        return true;
    }
}
