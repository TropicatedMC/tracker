package com.tropicatedmc.tracker.command;

import com.tropicatedmc.tracker.Tracker;
import com.tropicatedmc.tracker.storage.GPlayer;
import com.tropicatedmc.tracker.utils.Trackers;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class addstatCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        Tracker plugin = Tracker.getInstance();
        if(!sender.hasPermission("tracker.addstat")) {
            sender.sendMessage(plugin.formatMsg("MESSAGES.NO_PERMISSION"));
            return true;
        }
        if (args.length == 3) {
            UUID uuid = Bukkit.getOfflinePlayer(args[0]).getUniqueId();
            String statistic = args[1];

            if (!plugin.getSqlSetterGetter().playerExists(uuid)) {
                sender.sendMessage(plugin.formatMsg("MESSAGES.TARGET_NOT_FOUND"));
                return true;
            }

            if (Trackers.findByValue(statistic.toLowerCase()) == null) {
                sender.sendMessage(Tracker.colourize("&cInvalid statistic provided, Available Types: kills, deaths, killstreak, rank, rebirth, milestones, blocks, bosskills"));
                return true;
            }
            try {
                Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage(Tracker.colourize("&cInvalid input provided, please try again using an integer."));
                return true;
            }
            int value = Integer.parseInt(args[2]);

            //inputs validated

            GPlayer gPlayer = GPlayer.getPlayerData(plugin, uuid);
            plugin.getCommandUtils().addStatistic(gPlayer, statistic, value);
            sender.sendMessage(Tracker.formatMsg("MESSAGES.STATISTIC_ADDED")
                    .replace("%target%", gPlayer.getName())
                    .replace("%statistic%", statistic.toUpperCase())
                    .replace("%value%", value+""));

            return true;
        }
        sender.sendMessage(Tracker.colourize("&cUsage: /addstatistic <player> <statistic> <value>"));
        return true;
    }
}
