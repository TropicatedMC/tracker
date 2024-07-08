package com.tropicatedmc.tracker;

import com.tropicatedmc.tracker.api.PlaceholderAPI;
import com.tropicatedmc.tracker.command.addstatCommand;
import com.tropicatedmc.tracker.command.setstatCommand;
import com.tropicatedmc.tracker.command.statsCommand;
import com.tropicatedmc.tracker.command.trackerCommand;
import com.tropicatedmc.tracker.leaderboard.LeaderboardManager;
import com.tropicatedmc.tracker.storage.GPlayer;
import com.tropicatedmc.tracker.storage.SQLListeners;
import com.tropicatedmc.tracker.storage.SQLManager;
import com.tropicatedmc.tracker.storage.SQLSetterGetter;
import com.tropicatedmc.tracker.utils.CommandUtils;
import com.tropicatedmc.tracker.utils.RankUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.DecimalFormat;

public final class Tracker extends JavaPlugin {
    private static Tracker instance;
    private SQLManager sqlManager;
    private SQLSetterGetter sqlSetterGetter;

    private LeaderboardManager leaderboardManager;

    private PlaceholderAPI placeholderAPI;

    private CommandUtils commandUtils;

    private RankUtils rankUtils;
    private int taskID;

    public int lbTaskID;

    private String[] suffix = new String[]{"","k", "M", "B", "T"};
    private int MAX_LENGTH = 4;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        initialize();
        log("Plugin enabled.");
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        cancelTask();
        savePlayerData();
        log("Plugin disabled.");
        // Plugin shutdown logic
    }
    public static Tracker getInstance() { return instance; }
    public void log(String input) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD+"[Tracker] "+input);
    }
    public static String formatMsg(String input) {
        return ChatColor.translateAlternateColorCodes('&', getInstance().getConfig().getString(input));
    }
    public static String colourize(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public void initialize() {
        sqlManager = new SQLManager(this);
        if (!sqlManager.connect()) {
            log("Failed to connect to MySQL, disabling plugin.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        sqlSetterGetter = new SQLSetterGetter();
        sqlSetterGetter.createTable();
        leaderboardManager = new LeaderboardManager();
        placeholderAPI = new PlaceholderAPI();
        commandUtils = new CommandUtils();
        rankUtils = new RankUtils();
        getPlaceholderAPI().setupPapi();
        Bukkit.getPluginManager().registerEvents(new SQLListeners(), this);
        getCommand("stats").setExecutor(new statsCommand());
        getCommand("tracker").setExecutor(new trackerCommand());
        getCommand("setstat").setExecutor(new setstatCommand());
        getCommand("addstat").setExecutor(new addstatCommand());
        startSavingTask();
    }

    public SQLManager getSqlManager() {
        return sqlManager;
    }
    public SQLSetterGetter getSqlSetterGetter() { return sqlSetterGetter; }

    public LeaderboardManager getLeaderboardManager() {
        return leaderboardManager;
    }

    public PlaceholderAPI getPlaceholderAPI() {
        return placeholderAPI;
    }

    public CommandUtils getCommandUtils() { return commandUtils; }

    public RankUtils getRankUtils() { return rankUtils; }

    public void savePlayerData() {
        GPlayer.getAllPlayerData().forEach((uuid, gPlayer) -> gPlayer.uploadPlayerDataUrgent(this, gPlayer));
    }
    public void startSavingTask() {
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new Runnable() {
            @Override
            public void run() {
                GPlayer.getAllPlayerData().forEach((uuid, gPlayer) -> gPlayer.uploadPlayerData(getInstance()));
            }
        }, 0L, 20 * 60 * 30L);
        lbTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                getLeaderboardManager().updateLeaderboards();
            }
        }, 0L, 20 * 60 * 10L);
    }
    public void cancelTask(){
        Bukkit.getScheduler().cancelTask(taskID);
        Bukkit.getScheduler().cancelTask(lbTaskID);
    }
    public String formatNumber(double number) {
        String r = new DecimalFormat("##0E0").format(number);
        r = r.replaceAll("E[0-9]", suffix[Character.getNumericValue(r.charAt(r.length() - 1)) / 3]);
        while(r.length() > MAX_LENGTH || r.matches("[0-9]+\\.[a-z]")){
            r = r.substring(0, r.length()-2) + r.substring(r.length() - 1);
        }
        return r;
    }
}
