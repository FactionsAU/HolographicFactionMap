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
import org.bukkit.event.server.MapInitializeEvent;


/**
 *
 * @author Luke
 */
public final class HoloMapListener implements Listener{
    HolographicFactionMap hfm;
    //boolean initializeCancelled = false;
    
    public HoloMapListener(HolographicFactionMap hfm){
        this.hfm = hfm;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onRightClick(PlayerInteractEvent event){
        if (event.hasItem()){
            Material m = event.getItem().getType();
            if (event.getAction() == RIGHT_CLICK_BLOCK){
                if (m == EMPTY_MAP || m == MAP){
                    hfm.hmm.openMap(event.getPlayer(), event.getClickedBlock().getLocation().add(0.5, 1, 0.5));
                    event.setCancelled(true);
                }
            }else if (event.getAction() == RIGHT_CLICK_AIR){
                if (m == EMPTY_MAP || m == MAP){
                    hfm.hmm.closeMap(event.getPlayer());
                    event.setCancelled(true);
                }
            }
        }
    }
    
    /*
    @EventHandler(priority = EventPriority.NORMAL)
    public void onMapInitialize(MapInitializeEvent event){
        if(initializeCancelled){
            
        }
    }
    */
    
}
