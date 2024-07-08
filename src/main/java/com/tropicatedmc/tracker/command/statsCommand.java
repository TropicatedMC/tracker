package com.tropicatedmc.tracker.command;

import com.tropicatedmc.tracker.Tracker;
import com.tropicatedmc.tracker.storage.GPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class statsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) { return true; }
        Player p = (Player)sender;
        Tracker plugin = Tracker.getInstance();

        if(!p.hasPermission("tracker.stats")) {
            p.sendMessage(plugin.formatMsg("MESSAGES.NO_PERMISSION"));
            return true;
        }
        if(args.length == 0) {
            GPlayer gPlayer = GPlayer.getPlayerData(plugin, p.getUniqueId());
            plugin.getConfig().getStringList("MESSAGES.STATS").forEach(line -> {
                line = line.replace("%player%", p.getName())
                        .replace("%kills%", gPlayer.getKills()+"")
                        .replace("%deaths%", gPlayer.getDeaths()+"")
                        .replace("%kdr%", gPlayer.getKDRString())
                        .replace("%killstreak%", gPlayer.getKillstreak()+"")
                        .replace("%rank%", gPlayer.getRank()+"")
                        .replace("%rebirth%", gPlayer.getRebirth()+"")
                        .replace("%milestones%", gPlayer.getMilestones()+"")
                        .replace("%blocks%", gPlayer.getBlocksFormatted()+"")
                        .replace("%bosskills%", gPlayer.getBossKills()+"");
                p.sendMessage(plugin.colourize(line));
            });
            return true;
        }
        if(args.length == 1) {
            if(!p.hasPermission("tracker.stats.other")) {
                p.sendMessage(plugin.formatMsg("MESSAGES.NO_PERMISSION"));
                return true;
            }

            if(Bukkit.getPlayer(args[0]) == null) {
                p.sendMessage(plugin.formatMsg("MESSAGES.TARGET_NOT_FOUND"));
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);

            GPlayer gPlayer = GPlayer.getPlayerData(plugin.getInstance(), target.getUniqueId());
            plugin.getConfig().getStringList("MESSAGES.STATS").forEach(line -> {
                line = line.replace("%player%", target.getName())
                        .replace("%kills%", gPlayer.getKills()+"")
                        .replace("%deaths%", gPlayer.getDeaths()+"")
                        .replace("%kdr%", gPlayer.getKDRString())
                        .replace("%killstreak%", gPlayer.getKillstreak()+"")
                        .replace("%rank%", gPlayer.getRank()+"")
                        .replace("%rebirth%", gPlayer.getRebirth()+"")
                        .replace("%milestones%", gPlayer.getMilestones()+"")
                        .replace("%blocks%", gPlayer.getBlocksFormatted()+"")
                        .replace("%bosskills%", gPlayer.getBossKills()+"");
                p.sendMessage(plugin.colourize(line));
            });
            return true;
        }
        p.sendMessage(plugin.colourize("&cUsage: /stats <player>"));
        return true;
    }
}
