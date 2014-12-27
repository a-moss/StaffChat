package com.mydeblob.staffchat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class StaffChat extends JavaPlugin implements Listener, CommandExecutor{

	public static Economy econ = null;
	public static Permission perms = null;
	private static ArrayList<String> inChat = new ArrayList<String>();
	private final static String prefix = "&6[&bTemplar&aCraft&6] ";

	public void onEnable() {
		getCommand("sc").setExecutor(this);
		getCommand("sc").setAliases(Arrays.asList("staffchat", "tc", "ac", "adminchat"));
		getServer().getPluginManager().registerEvents(this, this);
		Bukkit.getServer().getLogger().log(Level.INFO, "[Staffchat] Successfully enabled!");
	}

	public void onDisable() {

	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("sc")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("This command can only be performed by players!");
				return true;
			}
			Player p = (Player) sender;
			if(p.hasPermission("templar.staff")){
				if(args.length > 0){
					String[] toSend = new String[args.length * 2];
					int counter = 0;
					for(int i = 1; i <= args.length; i++){
						toSend[counter] = args[i-1];
						counter++;
						toSend[counter] = " ";
						counter++;
					}
					StringBuilder builder = new StringBuilder();
					for(String s : toSend) {
					    builder.append(s);
					}
					
					for(Player pp:Bukkit.getOnlinePlayers()){
						if(pp.hasPermission("templar.staff")){
							pp.sendMessage(ChatColor.BLUE + "[StaffChat] " + ChatColor.RED +  p.getName() + ": " + ChatColor.LIGHT_PURPLE + builder.toString());
						}
					}
					return true;
				}else{
					if(inChat.contains(p.getName())){
						inChat.remove(p.getName());
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + ChatColor.GREEN + "Successfully changed to public chat!");
					}else{
						inChat.add(p.getName());
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + ChatColor.GREEN + "Successfully changed to staff chat!");
					}
					return true;
				}
			}else{
				p.sendMessage(ChatColor.RED + "Error: You do not have permission!");
				return true;
			}
		}
		return false;
	}


	@EventHandler
	public void onChat(AsyncPlayerChatEvent e){
		Player p = e.getPlayer();
		String msg = e.getMessage();
		if(inChat.contains(p.getName())){
			e.setCancelled(true);
			for(Player pp:Bukkit.getOnlinePlayers()){
				if(pp.hasPermission("templar.staff")){
					pp.sendMessage(ChatColor.BLUE + "[StaffChat] " + ChatColor.RED +  p.getName() + ": " + ChatColor.LIGHT_PURPLE + msg);
				}
			}
		}
	}


}
