/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.loopyluke.holographicfactionmap;

import com.dsh105.holoapi.HoloAPI;
import com.dsh105.holoapi.api.Hologram;
import com.dsh105.holoapi.api.HologramFactory;
import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.mcore.ps.PS;
import java.util.ArrayList;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

/**
 * @author Loopyluke
 */
public class HoloMap {
    private final Hologram hologram;
    private final Location loc;
    private final Player pl;
    
    public HoloMap(Player pl, Location loc){
        this.loc = loc;
        this.pl = pl;
        
        //Get the map strings from Factions
        ArrayList<String> mapStrings = BoardColls.get().getMap(UPlayer.get(pl), PS.valueOf(pl.getLocation()), 0);
        //Remove blank line at end that Factions adds
        if (mapStrings.get(mapStrings.size()-1).isEmpty()){
            mapStrings.remove(mapStrings.size()-1);
        }
        
        //Construct the map hologram
        HologramFactory f = new HologramFactory(HolographicFactionMap.getPlugin());
        f.withLocation(loc)
        .withSimplicity(true);
        for (String str: mapStrings){
            f.withText(str);
        }
        hologram = f.build();
        
        //Schedule a close task to automatically close the map
        closeIn(300);

    }
    
    /*
    * Closes (removes) the map's holograms
    */
    public void close(){
        //Stop tracking and remove the hologram entry
        HoloAPI.getManager().stopTracking(hologram);
    }
    
    /*
    * Closes the map in timeOut ticks
    */
    public final void closeIn(long timeOut){
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        
        scheduler.scheduleSyncDelayedTask(HolographicFactionMap.getPlugin(), new Runnable() {
            @Override
            public void run() {
                close();
            }
        }, timeOut);
    }
    
    /*
    * Get the HoloMap's location
    */
    public Location getLocation(){
        return loc;
    }
    
    /*
    * Get the UUID of the player associated with this HoloMap
    */
    public UUID getPlayerID(){
        return pl.getUniqueId();
    }
}
