package com.tropicatedmc.tracker.storage;

import com.tropicatedmc.tracker.Tracker;
import org.bukkit.Bukkit;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GPlayer {
    private final UUID uuid;
    private String name;
    private int deaths = 0;
    private int kills = 0;

    private int killstreak = 0;

    private int rank = 0;

    private int rebirth = 0;

    private int milestones = 0;

    private int bosskills = 0;

    private int blocks = 0;
    private final static Map<UUID, GPlayer> players = new HashMap<>();

    public GPlayer(UUID uuid, String name, int kills, int deaths, int killstreak, int rank, int rebirth, int milestones, int bosskills, int blocks) {
        this.uuid = uuid;
        this.name = name;
        setKills(kills);
        setDeaths(deaths);
        setKillstreak(killstreak);
        setRank(rank);
        setRebirth(rebirth);
        setMilestones(milestones);
        setBossKills(bosskills);
        setBlocks(blocks);
        players.put(uuid, this);
    }
    public UUID getUUID() { return uuid; }
    public String getName() { return name; }
    public int getKills() { return kills; }
    public int getDeaths() { return deaths; }
    public int getKillstreak() { return killstreak;}
    public int getRank() { return rank; }
    public int getRebirth() { return rebirth; }
    public int getMilestones() { return milestones; }
    public int getBossKills() { return bosskills; }

    public int getBlocks() { return blocks; }

    public String getBlocksFormatted() { return Tracker.getInstance().formatNumber(blocks); }

    public double getKDR() {
        if(getDeaths() == 0) {
            return (double) kills;
        }
        if(getKills() == 0) {
            return 0;
        }
        return (double)kills/(double)deaths; }
    public String getKDRString() {
        if (getDeaths() == 0) {
            return kills + "";
        }
        if (getKills() == 0) {
            return "0";
        }
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format((double) kills / (double) deaths);
    }
    public void setKills(int kills) {
        this.kills = kills;
    }
    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }
    public void setKillstreak(int killstreak) { this.killstreak = killstreak; }
    public void setRank(int rank) { this.rank = rank; }
    public void setRebirth(int rebirth) { this.rebirth = rebirth; }
    public void setMilestones(int milestones) { this.milestones = milestones; }
    public void setBossKills(int bossKills) { this.bosskills = bossKills; }

    public void setBlocks(int blocks) { this.blocks = blocks; }
    public void addKill() { this.kills++; }
    public void addDeath() { this.deaths++; }

    public void addKills(int amount) { this.kills += amount; }
    public void addDeaths(int amount) { this.deaths += amount; }
    public void incrementStreak(int amount) { this.killstreak += amount; }
    public void clearStreak() { setKillstreak(0); }
    public void addRebirth(int amount) { this.rebirth += amount; }
    public void addRank(int amount) { this.rank += amount; }
    public void addMilestone(int amount) { this.milestones += amount;}
    public void addBossKills(int amount) { this.bosskills += amount;}
    public void addBlocks(int amount) { this.blocks += amount; }


    public static GPlayer getPlayerData(Tracker plugin, UUID uuid) {
        if (!players.containsKey(uuid)) {
            GPlayer gPlayer = plugin.getSqlSetterGetter().getGPlayerData(uuid);
        }
        return players.get(uuid);
    }
    public void uploadPlayerData(Tracker plugin) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> plugin.getSqlSetterGetter().updateGPlayerData(this));
    }
    public void uploadPlayerDataUrgent(Tracker plugin) {
        getAllPlayerData().forEach((uuid, gPlayer) -> plugin.getSqlSetterGetter().updateGPlayerData(gPlayer));
    }
    public static Map<UUID, GPlayer> getAllPlayerData() {
        return players;
    }
    public static void removePlayerData(UUID uuid) { players.remove(uuid); }
}
