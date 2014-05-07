package com.loopyluke.holographicfactionmap;

import com.dsh105.holoapi.HoloAPI;
import com.dsh105.holoapi.api.Hologram;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class DynamicHologram extends Hologram{

    //super.updateNametag(observer, message, index);
    
    static BukkitTask updateTask;
    
    private static Map<Player, BlockFace> facings = new HashMap<>();
    private static Set<DynamicHologram> holograms = new HashSet<>();

    public boolean add(DynamicHologram h) {
        return holograms.add(h);
    }

    public boolean remove(DynamicHologram h) {
        return holograms.remove(h);
    }

    public void clearAll(){
        facings.clear();
        holograms.clear();
        updateTask.cancel();
    }
    
    public void releasePlayer(Player p){
        facings.remove(p);
    }

    @Override
    protected void move(Player observer, Vector to) {
        super.move(observer, to); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void show(Player observer, double x, double y, double z) {
        super.show(observer, x, y, z); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void show(Player observer, Location location) {
        super.show(observer, location); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void show(Player observer) {
        super.show(observer); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateLine(int index, String content) {
        super.updateLine(index, content); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void refreshDisplay() {
        super.refreshDisplay(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isSimple() {
        return super.isSimple(); //To change body of generated methods, choose Tools | Templates.
    }

    public DynamicHologram(int firstTagId, String saveId, String worldName, double x, double y, double z, String... lines) {
        super(firstTagId, saveId, worldName, x, y, z, lines);
        registerTask();
    }

    public DynamicHologram(String saveId, String worldName, double x, double y, double z, String... lines) {
        super(saveId, worldName, x, y, z, lines);
        registerTask();
    }
    
    private void registerTask(){
        holograms.add(this);
        if(updateTask == null){
            updateTask = new BukkitRunnable() {
                @Override
                public void run() {
                    updateHolograms();
                }
            }.runTaskTimer(HoloAPI.getCore(), 0L, 1L);
        }
    }
    
    static void updateHolograms(){
        Collection<Player> players = new ArrayList();
        Collections.addAll(players, Bukkit.getOnlinePlayers());
        players.forEach(p -> updateHolograms(p));
    }
    
    static void updateHolograms(Player p){
        Location l = p.getLocation();
        BlockFace face = Util.yawToFace(l.getYaw(), false);
        if(facings.get(p) != face){
            facings.put(p, face);
            holograms.forEach((h) -> h.show(p));
        }   
    }
}