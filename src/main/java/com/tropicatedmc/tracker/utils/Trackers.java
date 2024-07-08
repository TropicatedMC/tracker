package com.tropicatedmc.tracker.utils;

public enum Trackers {
    KILLS("kills"),
    DEATHS("deaths"),
    KILLSTREAK("killstreak"),
    RANK("rank"),
    REBIRTH("rebirth"),
    MILESTONES("milestones"),
    BLOCKS("blocks"),
    BOSS_KILLS("bosskills");

    private final String name;

    Trackers(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Trackers findByValue(String value) {
        Trackers result = null;
        for (Trackers trackers : values()) {
            if (trackers.getName().equalsIgnoreCase(value)) {
                result = trackers;
                break;
            }
        }
        return result;
    }

}
