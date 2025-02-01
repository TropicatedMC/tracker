package com.tropicatedmc.tracker.utils;

import com.edwardbelt.edprison.EdPrison;
import com.edwardbelt.edprison.storage.obj.EdPrisonPlayer;
import com.tropicatedmc.tracker.Tracker;
import com.tropicatedmc.tracker.storage.GPlayer;
import com.viaversion.viaversion.api.Via;
import org.bukkit.entity.Player;

public class CommandUtils {
    Tracker plugin = Tracker.getInstance();

    public void setStatistic(GPlayer gPlayer, String statistic, int value) {
        switch (statistic.toLowerCase()) {
            case "kills":
                gPlayer.setKills(value);
                return;

                case "deaths":
                    gPlayer.setDeaths(value);
                    return;

            case "killstreak":
                gPlayer.setKillstreak(value);
                return;

            case "blocks":
                gPlayer.setBlocks(value);
                return;

            case "rank":
                gPlayer.setRank(value);
                EdPrison.getInstance().getApi().getLevelApi().setLevel(gPlayer.getUUID(), "rankup", value);
                return;

            case "rebirth":
                gPlayer.setRebirth(value);
                return;

            case "bosskills":
                gPlayer.setBossKills(value);
                return;

            case "milestones":
                gPlayer.setMilestones(value);
                return;

            default:
                plugin.log("Error occurred whilst assigning statistic "+statistic+" to "+gPlayer.getName()+" (VALUE = "+value+")");
                return;
        }
    }
    public void addStatistic(GPlayer gPlayer, String statistic, int value) {
        switch (statistic.toLowerCase()) {
            case "kills":
                gPlayer.addKills(value);
                return;

            case "deaths":
                gPlayer.addDeaths(value);
                return;

            case "killstreak":
                gPlayer.incrementStreak(value);
                return;

            case "blocks":
                gPlayer.addBlocks(value);
                return;

            case "rank":
                gPlayer.addRank(value);
                return;

            case "rebirth":
                gPlayer.addRebirth(value);
                return;

            case "bosskills":
                gPlayer.addBossKills(value);
                return;

            case "milestones":
                gPlayer.addMilestone(value);
                return;

            default:
                plugin.log("Error occurred whilst assigning statistic "+statistic+" to "+gPlayer.getName()+" (VALUE = "+value+")");
                return;
        }
    }
    public String returnEmoji(Player player) {
        if(Via.getAPI().getPlayerVersion(player) < 50) {
            return plugin.getConfig().getString("SYMBOL.LEGACY");
        }
        return plugin.getConfig().getString("SYMBOL.NOT_LEGACY");
    }
}
