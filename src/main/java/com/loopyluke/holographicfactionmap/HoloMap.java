/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.loopyluke.holographicfactionmap;

import com.dsh105.holoapi.HoloAPI;
import com.dsh105.holoapi.api.Hologram;
import com.dsh105.holoapi.api.HologramFactory;
import com.dsh105.holoapi.util.SaveIdGenerator;
import com.massivecraft.factions.Const;
import com.massivecraft.factions.FFlag;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.RelationParticipator;
import com.massivecraft.factions.entity.Board;
import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.factions.util.AsciiCompass;
import com.massivecraft.mcore.ps.PS;
import com.massivecraft.mcore.util.Txt;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
        ArrayList<String> mapStrings = getMap(UPlayer.get(pl), PS.valueOf(pl.getLocation()), 0);
        //ArrayList<String> mapStrings = BoardColls.get().getMap(UPlayer.get(pl), PS.valueOf(pl.getLocation()), 0);
        //Remove blank line at end that Factions adds
        if (mapStrings.get(mapStrings.size()-1).isEmpty()){
            mapStrings.remove(mapStrings.size()-1);
        }
        
        //Construct the map hologram
        String saveId = SaveIdGenerator.nextId()+"";
        String[] north = appendDirection(mapStrings, "north");
        String[] east = appendDirection(mapStrings, "East");
        String[] south = appendDirection(mapStrings, "South");
        String[] west = appendDirection(mapStrings, "West");
        
        hologram = new DynamicHologram(loc,north,east,south,west);
        HoloAPI.getManager().track(hologram, HolographicFactionMap.getPlugin());
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

    private String[] appendDirection(ArrayList<String> mapStrings, String north) {
        ArrayList<String> out = new ArrayList<String>(mapStrings);
        out.add(north);
        
        String[] outArr = new String[out.size()];
        outArr = out.toArray(outArr);
        
        return  outArr;
    }
    
    enum Orientation{
            NONE, NEUTRAL, ALLY, ENEMY, SAFE, WAR, EVENT
        }
    
    public ArrayList<String> getMap(RelationParticipator observer, PS centerPs, double inDegrees)
    {
        char[] MAP_KEY_CHARS = "AX?\\/#?$%=&^ABCDEFGHJKLMNOPQRSTUVWXYZ1234567890abcdeghjmnopqrsuvwxyz".toCharArray();
        
        //Extract the board for the world
        Board b = BoardColls.get().get2(pl.getWorld());
        //ret = b.getMap(observer, centerPs, inDegrees);
        
        centerPs = centerPs.getChunkCoords(true);

        ArrayList<String> ret = new ArrayList<String>();
        Faction centerFaction = b.getFactionAt(centerPs);

        //Put a title on the map
        //ret.add(Txt.titleize("("+centerPs.getChunkX() + "," + centerPs.getChunkZ()+") "+centerFaction.getName(observer)));

        int halfWidth = 21 / 2;
        int halfHeight = 13 / 2;

        //Set the top-left position to get from (for looping)
        PS topLeftPs = centerPs.plusChunkCoords(-halfWidth, -halfHeight);

        //Set map sizes
        int width = halfWidth * 2 + 1;
        int height = halfHeight * 2 + 1;

        // Make room for the list of names
        height--;
        
        //Build a map of the factions and the color they will display on map
        Map<Faction, ChatColor> FactionCols = new HashMap<Faction, ChatColor>();
        //And also a map of strings for faction orientations
        Map<Rel, String> symbols = new EnumMap<Rel, String>(Rel.class);
        //The player's own faction
        symbols.put(Rel.LEADER, "#");
        symbols.put(Rel.OFFICER, "#");
        symbols.put(Rel.MEMBER, "#");
        symbols.put(Rel.RECRUIT, "#");
        //Other factions
        symbols.put(Rel.ALLY, "A");
        symbols.put(Rel.ENEMY, "X");
        symbols.put(Rel.NEUTRAL, "=");
        //And a string for no faction
        String NONE = "-";
        String WARZONE = "%";
        String SAFEZONE = "$";
        
        //Get a list of chat colors
        List<ChatColor> colors = new ArrayList<ChatColor>(Arrays.asList(ChatColor.values()));
        //Remove reserved colors and other font styles
        colors.remove(ChatColor.MAGIC);
        colors.remove(ChatColor.BOLD);
        colors.remove(ChatColor.STRIKETHROUGH);
        colors.remove(ChatColor.ITALIC);
        colors.remove(ChatColor.UNDERLINE);
        colors.remove(ChatColor.RESET);
        //Reserved colors
        colors.remove(ChatColor.GRAY); //Map background
        colors.remove(ChatColor.GREEN); //Own faction
        colors.remove(ChatColor.AQUA); //Player location
        colors.remove(ChatColor.DARK_RED); //WarZone
        colors.remove(ChatColor.GOLD); //SafeZone
        
        //Create iterators for relations
        Iterator<ChatColor> neutral = colors.iterator();
        Iterator<ChatColor> ally = colors.iterator();
        Iterator<ChatColor> enemy = colors.iterator();

        // For each row
        for (int dz = 0; dz < height; dz++)
        {
            // Draw and add that row
            String row = "";
            for (int dx = 0; dx < width; dx++)
            {
                //Is this the centre chunk? (The one the player is in)
                if(dx == halfWidth && dz == halfHeight)
                {
                        row += ChatColor.AQUA + "+";
                        continue;
                }

                PS herePs = topLeftPs.plusChunkCoords(dx, dz);
                Faction hereFaction = b.getFactionAt(herePs);
                if (hereFaction.isNone())
                {
                        row += ChatColor.GRAY + NONE;
                }
                else
                {
                    //A faction exists at this location
                    if (!FactionCols.containsKey(hereFaction)){
                        //Add the faction to the hashmap as it does not already exist
                        switch (hereFaction.getRelationTo(observer)){
                            case NEUTRAL:
                                if (!neutral.hasNext()){
                                    neutral = colors.iterator();
                                }
                                FactionCols.put(hereFaction, neutral.next());
                                break;
                            case ALLY:
                                if (!ally.hasNext()){
                                    ally = colors.iterator();
                                }
                                FactionCols.put(hereFaction, ally.next());
                                break;
                            case ENEMY:
                                if (!enemy.hasNext()){
                                    enemy = colors.iterator();
                                }
                                FactionCols.put(hereFaction, enemy.next());
                                break;
                            case LEADER:
                            case OFFICER:
                            case MEMBER:
                            case RECRUIT:
                                FactionCols.put(hereFaction, ChatColor.GREEN);
                                break;
                        }
                    }
                    //Add the character to the map row
                    if (hereFaction.getRelationTo(observer) == Rel.TRUCE){
                        if (hereFaction.getFlag(FFlag.PVP)){
                            row += ChatColor.DARK_RED + WARZONE;
                        }else{
                            row += ChatColor.GOLD + SAFEZONE;
                        }
                    }else{
                        row += FactionCols.get(hereFaction) + symbols.get(hereFaction.getRelationTo(observer));
                    }
                }
            }
            ret.add(row);
        }

        // Get the compass
        //ArrayList<String> asciiCompass = AsciiCompass.getAsciiCompass(inDegrees, ChatColor.RED, Txt.parse("<a>"));

        // Add the compass
        /*
        ret.set(1, asciiCompass.get(0)+ret.get(1).substring(3*3));
        ret.set(2, asciiCompass.get(1)+ret.get(2).substring(3*3));
        ret.set(3, asciiCompass.get(2)+ret.get(3).substring(3*3));
        */
        
        /*
        String fRow = "";
        for (Faction keyfaction : fList.keySet())
        {
                fRow += ""+keyfaction.getColorTo(observer) + fList.get(keyfaction) + ": " + keyfaction.getName() + " ";
        }
        fRow = fRow.trim();
        ret.add(fRow);
        */
                
        return ret;
    }
}
