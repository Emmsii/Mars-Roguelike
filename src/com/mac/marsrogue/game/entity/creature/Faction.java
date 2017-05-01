package com.mac.marsrogue.game.entity.creature;

import com.esotericsoftware.minlog.Log;
import com.mac.marsrogue.game.codex.Codex;

import java.util.HashMap;
import java.util.Map;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 02:02 PM.
 */
public class Faction {

    public static final Faction ALL = new Faction("all", null);
    
    public static final byte DO_NOTHING = 0;
    public static final byte ATTACK_ON_SIGHT = 1;
    public static final byte ATTACK_WHEN_ATTACKED = 2;
    public static final byte FLEE_ON_SIGHT = 3;
    public static final byte FLEE_WHEN_ATTACKED = 4;
        
    private Map<String, Byte> relations;
    private String name;
    
    public Faction(String name, HashMap<String, Byte> relations){
        this.name = name;
        this.relations = relations;
    }
    
    public String name(){
        return name;
    }
    
    public Map<String, Byte> relations(){
        return relations;
    }
    
    public byte relation(String factionName){
        if(!Codex.factions.has(factionName)){
            Log.warn("Faction: " + factionName + " does not exist.");
            return DO_NOTHING;
        }
        
        if(!relations.containsKey(factionName)){
            Log.warn("Faction " + name + " has no relations with " + factionName);
            return DO_NOTHING;
        }
        return relations.get(factionName);
    }
    
    public static byte parseAction(String input){
        switch(input.toLowerCase().trim()){
            case "do_nothing": return DO_NOTHING;
            case "attack_on_sight": return ATTACK_ON_SIGHT;
            case "attack_when_attacked": return ATTACK_WHEN_ATTACKED;
            case "flee_on_sight": return FLEE_ON_SIGHT;
            case "flee_when_attack": return FLEE_WHEN_ATTACKED;
            default:
                Log.warn("Unknown faction action: " + input);
                return DO_NOTHING;
        }
    }
}
