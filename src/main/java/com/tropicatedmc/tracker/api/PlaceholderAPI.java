package com.tropicatedmc.tracker.api;

import com.tropicatedmc.tracker.Tracker;
import org.bukkit.Bukkit;

public class PlaceholderAPI {
    public void setupPapi() {
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            Tracker.getInstance().log("PlaceholderAPI not found, placeholders will not be enabled.");
            return;
        }
        new Placeholders(Tracker.getInstance()).register();
        Tracker.getInstance().log("Hooked into PlaceholderAPI successfully");
    }
}
