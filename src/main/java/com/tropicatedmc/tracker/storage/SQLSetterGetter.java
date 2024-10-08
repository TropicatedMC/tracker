package com.tropicatedmc.tracker.storage;

import com.tropicatedmc.tracker.Tracker;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.UUID;

public class SQLSetterGetter {
    private Tracker plugin = Tracker.getInstance();
    private SQLManager sqlManager = plugin.getSqlManager();

    public boolean playerExists(UUID uuid) {
        try {
            PreparedStatement statement = sqlManager.getConnection().prepareStatement("SELECT * FROM tracker WHERE uuid=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void updatePlayerName(Player player) {
        if(!playerExists(player.getUniqueId())) { return; }
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement selectPlayer = sqlManager.getConnection().prepareStatement("SELECT * FROM tracker WHERE uuid = ?;");
                selectPlayer.setString(1, player.getUniqueId().toString());
                ResultSet playerResult = selectPlayer.executeQuery();
                if (playerResult.next() && !playerResult.getString("player").equals(player.getName())) {
                    PreparedStatement updateName = sqlManager.getConnection().prepareStatement("UPDATE tracker SET player = ? WHERE uuid = ?;");
                    updateName.setString(1, player.getName());
                    updateName.setString(2, player.getUniqueId().toString());
                    updateName.executeUpdate();
                }
                playerResult.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
    public void createTable() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin.getInstance(), () -> {
            try {
                PreparedStatement statement = sqlManager.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `tracker` (uuid VARCHAR(36) PRIMARY KEY, player VARCHAR(16), kills INT(11), deaths INT(11), killstreak INT(11), rank INT(11), rebirth INT(11), milestones INT(11), bosskills INT(11), blocks INT(11));");
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
    public void createPlayer(final UUID uuid, String player) {
        if (!sqlManager.isConnected() && !sqlManager.connect()) {
            plugin.getLogger().severe("Can't establish a database connection!");
            return;
        }
        if (playerExists(uuid)) {
            return;
        }
        try {
            PreparedStatement insert = sqlManager.getConnection()
                    .prepareStatement("INSERT INTO tracker (uuid,player,kills,deaths,killstreak,rank,rebirth,milestones,bosskills,blocks) VALUES (?,?,?,?,?,?,?,?,?,?)");
            insert.setString(1, uuid.toString());
            insert.setString(2, player);
            insert.setInt(3, 0);
            insert.setInt(4, 0);
            insert.setInt(5, 0);
            insert.setInt(6, 0);
            insert.setInt(7, 0);
            insert.setInt(8, 0);
            insert.setInt(9, 0);
            insert.setInt(10, 0);
            insert.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public GPlayer getGPlayerData(UUID uuid) {
        if (uuid == null) return null;

        if (!sqlManager.isConnected() && !sqlManager.connect()) {
            plugin.getLogger().severe("Can't establish a database connection!");
            return null;
        }

        try (PreparedStatement statement = sqlManager.getConnection().prepareStatement("SELECT * FROM tracker WHERE uuid=?")) {
            statement.setString(1, uuid.toString());

            ResultSet rs = statement.executeQuery();
            GPlayer gPlayer = null;

            if (rs.next()) {
                String stringUUID = rs.getString("uuid");
                UUID uuidDB = (stringUUID == null ? null : UUID.fromString(stringUUID));
                String name = rs.getString("player");
                int kills = rs.getInt("kills");
                int deaths = rs.getInt("deaths");
                int killstreak = rs.getInt("killstreak");
                int rank = rs.getInt("rank");
                int rebirth = rs.getInt("rebirth");
                int milestones = rs.getInt("milestones");
                int bosskills = rs.getInt("bosskills");
                int blocks = rs.getInt("blocks");

                gPlayer = new GPlayer(uuidDB, name, kills, deaths, killstreak, rank, rebirth, milestones, bosskills, blocks);

                rs.close();
                return gPlayer;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
    public void updateGPlayerData(GPlayer gPlayer) {
        if (!sqlManager.isConnected() && !sqlManager.connect()) {
            plugin.getLogger().severe("Can't establish a database connection!");
            return;
        }

        // Insert if not exists, update if exists
        final String UPDATE_DATA = "INSERT INTO `tracker` (uuid, player, kills, deaths, killstreak, rank, rebirth, milestones, bosskills, blocks) VALUES (?, ?, ?, ?, ?, ?, ?,?,?,?) ON DUPLICATE KEY " +
                "UPDATE player = ?, kills = ?, deaths = ?, killstreak = ?, rank = ?, rebirth = ?, milestones = ?, bosskills = ?, blocks = ?;";

        try (PreparedStatement statement = sqlManager.getConnection().prepareStatement(UPDATE_DATA)) {
            int i = 1;

            // Setting insert variables
            statement.setString(i++, (gPlayer.getUUID() == null ? null : gPlayer.getUUID().toString()));
            statement.setString(i++, gPlayer.getName());
            statement.setInt(i++, gPlayer.getKills());
            statement.setInt(i++, gPlayer.getDeaths());
            statement.setInt(i++, gPlayer.getKillstreak());
            statement.setInt(i++, gPlayer.getRank());
            statement.setInt(i++, gPlayer.getRebirth());
            statement.setInt(i++, gPlayer.getMilestones());
            statement.setInt(i++, gPlayer.getBossKills());
            statement.setInt(i++, gPlayer.getBlocks());

            // Setting update variables
            statement.setString(i++, gPlayer.getName());
            statement.setInt(i++, gPlayer.getKills());
            statement.setInt(i++, gPlayer.getDeaths());
            statement.setInt(i++, gPlayer.getKillstreak());
            statement.setInt(i++, gPlayer.getRank());
            statement.setInt(i++, gPlayer.getRebirth());
            statement.setInt(i++, gPlayer.getMilestones());
            statement.setInt(i++, gPlayer.getBossKills());
            statement.setInt(i, gPlayer.getBlocks());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public LinkedHashMap<String, Integer> getTopKills() {
        try {
            PreparedStatement statement = sqlManager.getConnection().prepareStatement
                    ("SELECT player, kills FROM tracker GROUP BY player ORDER BY `kills` DESC LIMIT 10");
            ResultSet results = statement.executeQuery();
            LinkedHashMap<String, Integer> players = new LinkedHashMap<>();
            while (results != null && results.next()) {
                players.put(results.getString("player"), results.getInt("kills"));
            }
            return players;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public LinkedHashMap<String, Integer> getTopBlocks() {
        try {
            PreparedStatement statement = sqlManager.getConnection().prepareStatement
                    ("SELECT player, blocks FROM tracker GROUP BY player ORDER BY `blocks` DESC LIMIT 10");
            ResultSet results = statement.executeQuery();
            LinkedHashMap<String, Integer> players = new LinkedHashMap<>();
            while (results != null && results.next()) {
                players.put(results.getString("player"), results.getInt("blocks"));
            }
            return players;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public LinkedHashMap<String, Integer> getTopRebirths() {
        try {
            PreparedStatement statement = sqlManager.getConnection().prepareStatement
                    ("SELECT player, rebirth FROM tracker GROUP BY player ORDER BY `rebirth` DESC LIMIT 10");
            ResultSet results = statement.executeQuery();
            LinkedHashMap<String, Integer> players = new LinkedHashMap<>();
            while (results != null && results.next()) {
                players.put(results.getString("player"), results.getInt("rebirth"));
            }
            return players;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public LinkedHashMap<String, Integer> getTopDeaths() {
        try {
            PreparedStatement statement = sqlManager.getConnection().prepareStatement
                    ("SELECT player, deaths FROM tracker GROUP BY player ORDER BY `deaths` DESC LIMIT 10");
            ResultSet results = statement.executeQuery();
            LinkedHashMap<String, Integer> players = new LinkedHashMap<>();
            while (results != null && results.next()) {
                players.put(results.getString("player"), results.getInt("deaths"));
            }
            return players;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public LinkedHashMap<String, Integer> getTopBossKills() {
        try {
            PreparedStatement statement = sqlManager.getConnection().prepareStatement
                    ("SELECT player, bosskills FROM tracker GROUP BY player ORDER BY `bosskills` DESC LIMIT 10");
            ResultSet results = statement.executeQuery();
            LinkedHashMap<String, Integer> players = new LinkedHashMap<>();
            while (results != null && results.next()) {
                players.put(results.getString("player"), results.getInt("bosskills"));
            }
            return players;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
