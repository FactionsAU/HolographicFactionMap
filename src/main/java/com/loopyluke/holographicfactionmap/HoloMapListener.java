/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.loopyluke.holographicfactionmap;

import org.bukkit.Material;
import static org.bukkit.Material.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import static org.bukkit.event.block.Action.*;
import org.bukkit.event.player.PlayerInteractEvent;


/**
 * @author Loopyluke
 */
public final class HoloMapListener implements Listener{
    
    public HoloMapListener(){
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onRightClick(PlayerInteractEvent event){
        if (event.hasItem()){
            Material m = event.getItem().getType();
            //Open a player's map
            if (event.getAction() == RIGHT_CLICK_BLOCK){
                if (m == EMPTY_MAP || m == MAP){
                    HolographicFactionMap.getManager()
                    .openMap(event.getPlayer(), event.getClickedBlock().getLocation().add(0.5, 3.5, 0.5));
                    //event.setCancelled(true);
                }
            //Close any open map for the player
            }else if (event.getAction() == RIGHT_CLICK_AIR){
                if (m == EMPTY_MAP || m == MAP){
                    HolographicFactionMap.getManager().closeMap(event.getPlayer());
                    //event.setCancelled(true);
                }
            }
        }
    }
}
