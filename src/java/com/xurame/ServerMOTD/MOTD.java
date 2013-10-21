package com.xurame.ServerMOTD;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.xurame.Metrics.Metrics;

public class MOTD extends JavaPlugin implements Listener, CommandExecutor {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		String motd = getConfig().getString("motd.ingame");
		motd = motd.replaceAll("&", "§");
		p.sendMessage(motd);
	}

	@EventHandler
	public void onServerPing(ServerListPingEvent e) {
		String motd = getConfig().getString("motd.server");
		motd = motd.replaceAll("&", "\u00A7");
		e.setMotd(motd);
	}

	public void onEnable() {
		getConfig().options().copyDefaults(true);
		saveDefaultConfig();
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e) {
			// Failed to submit the stats :-(
		}
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("motd")) {
			if (!sender.hasPermission("motd.check")) {
				sender.sendMessage(ChatColor.RED
						+ "You are not permitted to do this!");
				return true;
			}
			String motd = getConfig().getString("motd.ingame");
			motd = motd.replaceAll("&", "§");
			String server = getConfig().getString("motd.server");
			server = server.replaceAll("&", "§");
			sender.sendMessage(ChatColor.GREEN + "In-Game MOTD: " +  motd);
			sender.sendMessage(ChatColor.GREEN + "Server MOTD: " + server);
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("setmotd")) {
			if (!sender.hasPermission("motd.set")) {
				sender.sendMessage(ChatColor.RED
						+ "You are not permitted to do this!");
				return true;
			}
			if (args.length == 0) {
				sender.sendMessage(ChatColor.RED + "Please specify a message!");
				return true;
			}
			StringBuilder str = new StringBuilder();
			for (int i = 0; i < args.length; i++) {
				str.append(args[i] + " ");
			}
			String motd = str.toString();
			getConfig().set("motd.ingame", motd);
			saveConfig();
			String motd1 = getConfig().getString("motd.ingame");
			motd1 = motd1.replaceAll("&", "§");
			sender.sendMessage(ChatColor.GREEN + "MOTD set to: " + motd1);
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("setservermotd")) {
			if (!sender.hasPermission("motd.set")) {
				sender.sendMessage(ChatColor.RED
						+ "You are not permitted to do this!");
				return true;
			}
			if (args.length == 0) {
				sender.sendMessage(ChatColor.RED + "Please specify a message!");
				return true;
			}
			StringBuilder str = new StringBuilder();
			for (int i = 0; i < args.length; i++) {
				str.append(args[i] + " ");
			}
			String motd = str.toString();
			getConfig().set("motd.server", motd);
			saveConfig();
			String server = getConfig().getString("motd.server");
			server = server.replaceAll("&", "§");
			sender.sendMessage(ChatColor.GREEN + "MOTD set to: " + server);
			return true;
		}
		return true;
	}
}