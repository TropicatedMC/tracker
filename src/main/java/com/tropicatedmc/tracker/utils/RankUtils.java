package com.tropicatedmc.tracker.utils;

import com.tropicatedmc.tracker.Tracker;
import com.tropicatedmc.tracker.storage.GPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class RankUtils {
    Tracker plugin = Tracker.getInstance();

    public String returnDisplay(UUID uuid) {
        AtomicReference<String> colour = new AtomicReference<>(plugin.getConfig().getString("DISPLAYS.0.name"));
        GPlayer gPlayer = GPlayer.getPlayerData(plugin, uuid);
        String last = plugin.getConfig().getString("LAST_DISPLAY");
        if(gPlayer.getRank() >= plugin.getConfig().getInt("DISPLAYS."+last+".rank")) {
            return plugin.getConfig().getString("DISPLAYS."+last+".name");
        }
        colour.set(plugin.getConfig().getString("DISPLAYS.0.name"));
        plugin.getConfig().getConfigurationSection("DISPLAYS").getKeys(false).forEach(rank -> {
            int required = plugin.getConfig().getInt("DISPLAYS."+rank+".rank");

            if(gPlayer.getRank() >= required) {
                colour.set(plugin.getConfig().getString("DISPLAYS."+rank+".name"));
            }
        });
        return colour.get();
    }
    public String getFormattedDisplay(GPlayer gPlayer) {
        if(gPlayer.getRank() < 1000) {
            return Tracker.colourize("&8["+returnDisplay(gPlayer.getUUID())+gPlayer.getRank()+"&8]");
        }
        return Tracker.colourize("&8[&5"+gPlayer.getRebirth()+"&8]");
    }
}
