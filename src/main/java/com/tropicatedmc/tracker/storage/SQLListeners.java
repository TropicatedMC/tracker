package com.tropicatedmc.tracker.storage;

import com.tropicatedmc.tracker.Tracker;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class SQLListeners implements Listener {
    private Tracker plugin = Tracker.getInstance();

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();
        plugin.getSqlSetterGetter().createPlayer(uuid, event.getName());
        GPlayer playerData = GPlayer.getPlayerData(plugin, uuid);
    }
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            UUID uuid = event.getPlayer().getUniqueId();
            GPlayer playerData = GPlayer.getPlayerData(plugin, uuid);
            playerData.uploadPlayerData(plugin);
            GPlayer.removePlayerData(uuid);
        });
    }
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerLoginEvent(PlayerLoginEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> plugin.getSqlSetterGetter().updatePlayerName(event.getPlayer()));
    }
    @EventHandler
    public void onKill(EntityDeathEvent e) {
        if(e.getEntityType() != EntityType.PLAYER) return;
        if(e.getEntity().getKiller() == null) return;
        if(e.getEntity().getKiller().getType() != EntityType.PLAYER) return;
        Player killer = e.getEntity().getKiller();
        GPlayer gPlayer;
        gPlayer = GPlayer.getPlayerData(plugin, killer.getUniqueId());
        gPlayer.addKill();
        gPlayer.incrementStreak(1);
        Player killed = (Player) e.getEntity();
        gPlayer = GPlayer.getPlayerData(plugin, killed.getUniqueId());
        gPlayer.addDeath();
        gPlayer.setKillstreak(0);
    }
}
