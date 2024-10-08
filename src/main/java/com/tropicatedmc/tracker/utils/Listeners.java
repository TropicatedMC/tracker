package com.tropicatedmc.tracker.utils;

import com.edwardbelt.edprison.EdPrison;
import com.edwardbelt.edprison.events.EdPrisonAddMultiplierCurrency;
import com.edwardbelt.edprison.events.EdPrisonBlockBreakEvent;
import com.edwardbelt.edprison.events.EdPrisonEnchantTriggerEvent;
import com.edwardbelt.edprison.storage.obj.EdPrisonPlayer;
import com.tropicatedmc.tracker.Tracker;
import com.tropicatedmc.tracker.storage.GPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Random;

public class Listeners implements Listener {
    Tracker plugin = Tracker.getInstance();

    @EventHandler
    public void EdPrisonBlockBreakEvent(EdPrisonBlockBreakEvent e) {
        GPlayer gPlayer = GPlayer.getPlayerData(plugin, e.getPlayer().getUniqueId());
        gPlayer.addBlocks((int)e.getBlocks());
    }
   // @EventHandler
   // //public void addMultiplier(EdPrisonAddMultiplierCurrency e) {
       // Bukkit.broadcastMessage("------------------------------------");
        //Bukkit.broadcastMessage("CURRENCY : "+e.getCurrency()+ " CURRENT MULTI: "+e.getMultiplier()+ " CURRENT AMOUNT: "+e.getAmount());
       // e.setMultiplier(2);
       // e.setAmount((e.getAmount()*e.getMultiplier()));
       // Bukkit.broadcastMessage("NEW MULTI : "+e.getMultiplier()+ " NEW AMOUNT: "+e.getAmount());
       // Bukkit.broadcastMessage("------------------------------------");
    //}
}
