package com.loopyluke.holographicfactionmap;

import com.dsh105.holoapi.HoloAPI;
import com.dsh105.holoapi.api.Hologram;
import com.dsh105.holoapi.util.SaveIdGenerator;
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

public class DynamicHologram extends Hologram{

    //super.updateNametag(observer, message, index);
    
    static BukkitTask updateTask;
    
    private static Map<Player, BlockFace> facings = new HashMap<>();
    private static Set<DynamicHologram> holograms = new HashSet<>();
    
    private Map<BlockFace,String[]> displays = new HashMap<>();

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
    
    public static void releasePlayer(Player p){
        facings.remove(p);
    }

    @Override
    public void clearAllPlayerViews(){
        remove(this);
        super.clearAllPlayerViews();
    }

    @Override
    public boolean isSimple() {
        return true;
    }

    public DynamicHologram(Location loc, String[] north, String[] east, String[] south, String[] west){
        super(SaveIdGenerator.nextId()+"", loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ(), north);
        displays.put(BlockFace.SOUTH, north);
        displays.put(BlockFace.WEST, east);
        displays.put(BlockFace.NORTH, south);
        displays.put(BlockFace.EAST, west);
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
    
    private void updateLines(Player p, BlockFace face){
        updateLines(p, displays.get(face));
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
            holograms.forEach((h) -> h.updateLines(p, face));
        }   
    }
}