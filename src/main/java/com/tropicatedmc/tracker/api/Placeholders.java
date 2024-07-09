package com.tropicatedmc.tracker.api;

import com.tropicatedmc.tracker.Tracker;
import com.tropicatedmc.tracker.storage.GPlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class Placeholders extends PlaceholderExpansion {
    private Tracker plugin;

    public Placeholders(Tracker plugin) {
        this.plugin = plugin;
    }

    public boolean persist() {
        return true;
    }

    public boolean canRegister() {
        return true;
    }

    public String getAuthor() {
        return "Frxq15";
    }

    public String getIdentifier() {
        return "tracker";
    }

    public String getVersion() {
        return this.plugin.getDescription().getVersion();
    }

    public String onPlaceholderRequest(final Player player, final String placeholder) {
        if (player == null) {
            return "Invalid Player";
        }
        String pos;
        GPlayer playerData = GPlayer.getPlayerData(plugin, player.getUniqueId());

        if(placeholder.startsWith("topkills_kills_")){
            pos = placeholder.replace("topkills_kills_", "");
            int p = Integer.parseInt(pos);
            return String.valueOf(plugin.getLeaderboardManager().getStatsByPosition("kills", p));
        }
        if(placeholder.startsWith("topkills_name_")){
            pos = placeholder.replace("topkills_name_", "");
            int p = Integer.parseInt(pos);
            return String.valueOf(plugin.getLeaderboardManager().getNameByStatsPosition("kills", p));
        }
        if(placeholder.startsWith("topblocksmined_blocks_formatted_")){
            pos = placeholder.replace("topblocksmined_blocks_formatted_", "");
            int p = Integer.parseInt(pos);
            return plugin.formatNumber(plugin.getLeaderboardManager().getStatsByPosition("blocks", p));
        }
        if(placeholder.startsWith("topblocksmined_blocks_")){
            pos = placeholder.replace("topblocksmined_blocks_", "");
            int p = Integer.parseInt(pos);
            return String.valueOf(plugin.getLeaderboardManager().getStatsByPosition("blocks", p));
        }
        if(placeholder.startsWith("topblocksmined_name_")){
            pos = placeholder.replace("topblocksmined_name_", "");
            int p = Integer.parseInt(pos);
            return String.valueOf(plugin.getLeaderboardManager().getNameByStatsPosition("blocks", p));
        }
        if(placeholder.startsWith("toprebirths_rebirths_")){
            pos = placeholder.replace("toprebirths_rebirths_", "");
            int p = Integer.parseInt(pos);
            return String.valueOf(plugin.getLeaderboardManager().getStatsByPosition("rebirths", p));
        }
        if(placeholder.startsWith("toprebirths_name_")){
            pos = placeholder.replace("toprebirths_name_", "");
            int p = Integer.parseInt(pos);
            return String.valueOf(plugin.getLeaderboardManager().getNameByStatsPosition("rebirths", p));
        }

        switch(placeholder.toLowerCase()) {
            case "kills":
                return String.valueOf(playerData.getKills());

            case "deaths":
                return String.valueOf(playerData.getDeaths());

            case "rank":
                return String.valueOf(playerData.getRank());

            case "rank_colour":
                return Tracker.colourize(plugin.getRankUtils().returnDisplay(playerData.getUUID())+playerData.getRank());

            case "rank_chat":
                return Tracker.colourize(plugin.getRankUtils().getFormattedDisplay(playerData));

            case "rank_scoreboard":
                return Tracker.colourize(plugin.getRankUtils().getScoreboardDisplay(playerData));

            case "rank_titlebar":
                return Tracker.colourize(plugin.getRankUtils().getScoreboardDisplay(playerData));

            case "blocks":
                return String.valueOf(playerData.getBlocks());

            case "blocksf":
                return playerData.getBlocksFormatted();

            case "rebirths":
                return String.valueOf(playerData.getRebirth());

            case "killstreak":
                return String.valueOf(playerData.getKillstreak());

            case "milestones":
                return String.valueOf(playerData.getMilestones());

            case "bosskills":
                return String.valueOf(playerData.getBossKills());

            case "kdr":
                DecimalFormat df = new DecimalFormat("#.##");
                if(playerData.getKDR() < 0.1) {
                    return String.valueOf(0);
                }
                return df.format(playerData.getKDR())+"";

        }
        return null;
    }
}
