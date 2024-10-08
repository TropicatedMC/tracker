package com.tropicatedmc.tracker.command;

import com.tropicatedmc.tracker.Tracker;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.ViaAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public class trackerCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        commandSender.sendMessage(Tracker.colourize("&f[&e+&f] &fRunning &eTracker v"+Tracker.getInstance().getDescription().getVersion()+" &fby &e&lFrxq15"));
        return true;
    }
}
