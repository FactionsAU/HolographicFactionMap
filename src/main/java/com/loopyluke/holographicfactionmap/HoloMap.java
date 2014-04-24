/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.loopyluke.holographicfactionmap;

import com.dsh105.holoapi.api.Hologram;
import com.dsh105.holoapi.api.HologramFactory;
import com.dsh105.holoapi.api.visibility.Visibility;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 *
 * @author Luke
 */
public class HoloMap {
    
    Hologram hologram;
    Location loc;
    Player pl;
    HolographicFactionMap hfm;
    
    public HoloMap(HolographicFactionMap hfm, Player pl, Location loc){
        this.loc = loc;
        this.pl = pl;
        this.hfm = hfm;
        
        //Construct the map hologram
        hologram = new HologramFactory(hfm)
        .withLocation(loc)
        .withText(ChatColor.GOLD + "Hey there!")
        .withText(ChatColor.RED + "Enjoy your stay!")
        .build();
    }
    
    public void close(){
        //Stop tracking and remove the hologram entry
        int[] holoEntities = hologram.getAllEntityIds();
        List<Entity> worldEntities = hfm.getServer().getWorld(hologram.getWorldName()).getEntities();
        hfm.getLogger().info("" + holoEntities.length);
        hfm.getLogger().info("" + worldEntities.size());
        for (int i: holoEntities){
            for (Entity e: worldEntities){
                if (e.getEntityId() == i){
                    hfm.getLogger().info("removed entity" + i);
                    e.remove();
                }
            }
        }
        hfm.hmm.stopTracking(hologram);
        hfm.hmm.clearFromFile(hologram);
    }
}
