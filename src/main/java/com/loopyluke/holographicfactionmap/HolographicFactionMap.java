/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loopyluke.holographicfactionmap;

import com.dsh105.holoapi.HoloAPI;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


/**
 *
 * @author Loopyluke
 */
public class HolographicFactionMap extends JavaPlugin{
    HoloMapManager hmm;
    HoloAPI HAPI;
    
    @Override
    public void onEnable() {
        
        hmm = new HoloMapManager(this);
        
        getServer().getPluginManager().registerEvents(new HoloMapListener(this), this);
        
        getLogger().info("The HolographicFactionMap plugin has been loaded");
    }

    @Override
    public void onDisable() {
        getLogger().info("The HolographicFactionMap plugin has been unloaded");
    }
}
