package com.loopyluke.holographicfactionmap;

import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class HoloMapManager{
    HashMap <UUID, HoloMap> holoMaps = new HashMap<UUID, HoloMap>();
    
    public HoloMapManager(){
    }
    
    /*
    * Opens a player's holographic map
    */
    public void openMap(Player pl, Location loc){
        if (pl != null){
            //Close any existing map for this player
            closeMap(pl);

            //Open a new map
            holoMaps.put(pl.getUniqueId(), new HoloMap(pl, loc));
        }
    }
    
    /*
    * Closes a player's holographic map
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
    
    /*
    * Closes all open HoloMaps
    */
    public void closeAllMaps(){
        HoloMap map;
        for (UUID player: holoMaps.keySet()){
            map = holoMaps.get(player);
            if (map != null){
                map.close();
                holoMaps.remove(player);
            }
        }
    }
}
