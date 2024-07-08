package com.tropicatedmc.tracker.command;

import com.tropicatedmc.tracker.Tracker;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class trackerCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        commandSender.sendMessage(Tracker.colourize("&f[&c+&f] &fRunning &cTracker v"+Tracker.getInstance().getDescription().getVersion()+" &fby &c&lFrxq15"));
        return true;
    }
}
