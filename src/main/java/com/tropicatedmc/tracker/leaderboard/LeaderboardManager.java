package com.tropicatedmc.tracker.leaderboard;

import com.tropicatedmc.tracker.Tracker;
import org.bukkit.Bukkit;

import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class LeaderboardManager {
    private Tracker plugin = Tracker.getInstance();

    private LinkedHashMap<String, Integer> kills = new LinkedHashMap<>();

    private LinkedHashMap<String, Integer> blocks = new LinkedHashMap<>();

    private LinkedHashMap<String, Integer> rebirths = new LinkedHashMap<>();

    public void updateKills(LinkedHashMap<String, Integer> refresh) {
        this.kills = refresh;
    }

    public void updateBlocks(LinkedHashMap<String, Integer> refresh) {
        this.blocks = refresh;
    }

    public void updateRebirths(LinkedHashMap<String, Integer> refresh) {
        this.rebirths = refresh;
    }
    public void updateLeaderboards() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> updateKills(plugin.getSqlSetterGetter().getTopKills()));
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> updateBlocks(plugin.getSqlSetterGetter().getTopBlocks()));
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> updateRebirths(plugin.getSqlSetterGetter().getTopRebirths()));
    }
    public Integer getStatsByPosition(String stat, int pos) {
        switch(stat.toLowerCase()) {
            case "kills":
                return decideStat(kills, pos);

            case "blocks":
                return decideStat(blocks, pos);

            case "rebirths":
                return decideStat(rebirths, pos);

            default:
                return decideStat(blocks, pos);
        }
    }
    public String getNameByStatsPosition(String stat, int pos) {
        switch(stat.toLowerCase()) {
            case "kills":
                return decideName(kills, pos);

            case "blocks":
                return decideName(blocks, pos);

            case "rebirths":
                return decideName(rebirths, pos);

            default:
                return decideName(blocks, pos);
        }
    }
    public String decideName(LinkedHashMap<String, Integer> statistic, int pos) {
        int total = statistic.size() -1;
        AtomicReference name = new AtomicReference("None");
        if(pos > total) {
            return "None";
        }
        AtomicInteger position = new AtomicInteger(0);
        statistic.forEach((player, amount) -> {
            position.getAndIncrement();
            if(position.get() == pos) {
                name.set(player);
            }
        });
        return (String) name.get();
    }
    public Integer decideStat(LinkedHashMap<String, Integer> statistic, int pos) {
        int total = statistic.size() -1;
        AtomicInteger am = new AtomicInteger(0);
        if(pos > total) {
            return 0;
        }
        AtomicInteger position = new AtomicInteger(0);
        statistic.forEach((player, amount) -> {
            position.getAndIncrement();
            if(position.get() == pos) {
                am.set(amount);
            }
        });
        return am.get();
    }
}
