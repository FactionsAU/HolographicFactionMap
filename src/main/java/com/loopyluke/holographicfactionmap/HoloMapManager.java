/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.loopyluke.holographicfactionmap;

import com.dsh105.holoapi.api.SimpleHoloManager;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 *
 * @author Luke
 */
public class HoloMapManager extends SimpleHoloManager {
    
    HashMap <UUID, HoloMap> holoMaps;
    HolographicFactionMap hfm;
    
    public HoloMapManager(HolographicFactionMap hfm){
        this.hfm = hfm;
        holoMaps = new HashMap<UUID, HoloMap>();
    }
    
    /*
    * Opens a player's holographic map.
    */
    public void openMap(Player pl, Location loc){
        if (pl != null){
            //Close any existing map for this player
            closeMap(pl);

            //Open a new map
            holoMaps.put(pl.getUniqueId(), new HoloMap(hfm, pl, loc));
        }
    }
    
    /*
    * Closes the player's holographic map.
    */
    public void closeMap(Player pl){
        if (pl != null){
            HoloMap map = holoMaps.get(pl.getUniqueId());
            //Remove the map and its associated hologram(s)
            if (map != null){
                map.close();
                holoMaps.remove(pl.getUniqueId());
            }
        }
    }
}
