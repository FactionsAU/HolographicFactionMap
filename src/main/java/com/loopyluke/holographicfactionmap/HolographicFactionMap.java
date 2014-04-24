/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loopyluke.holographicfactionmap;

import com.dsh105.holoapi.HoloAPI;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Loopyluke
 */
public class HolographicFactionMap extends JavaPlugin{
    private static HoloMapManager HOLOMAPMANAGER;
    private static HolographicFactionMap HFM;
    protected static HoloAPI HOLOAPI;
    
    @Override
    public void onEnable() {
        HOLOMAPMANAGER = new HoloMapManager();
        HOLOAPI = getHoloAPI();
        HFM = this;
        
        //Register events
        getServer().getPluginManager().registerEvents(new HoloMapListener(), this);
        
        //Notify enable complete
        getLogger().info("The HolographicFactionMap plugin has been loaded");
    }

    @Override
    public void onDisable() {
        //TODO: disable stuff
        
        //Notify disable complete
        getLogger().info("The HolographicFactionMap plugin has been unloaded");
    }
    
    /*
    * Gets a reference to the HoloAPI plugin
    */
    public HoloAPI getHoloAPI() {
        Plugin plugin = this.getServer().getPluginManager().getPlugin("HoloAPI");
        if (plugin == null || !(plugin instanceof HoloAPI)) {
            // HoloAPI isn't installed (or loaded) on this server - nag the server owner about this
            return null;
        }

        return ((HoloAPI) plugin);
    }
    
    /*
    * Returns a reference to this plugin
    */
    public static HolographicFactionMap getPlugin(){
        return HFM;
    }
    
    /*
    * Gets a reference to the HoloMapManager
    */
    public static HoloMapManager getManager(){
        return HOLOMAPMANAGER;
    }
    
}
