package com.mac.marsrogue.game.codex;

import com.esotericsoftware.minlog.Log;
import com.mac.marsrogue.game.entity.creature.Creature;
import com.mac.marsrogue.game.entity.creature.Faction;
import com.mac.marsrogue.game.entity.item.Item;
import com.mac.marsrogue.game.entity.item.blueprint.GunBlueprint;
import com.mac.marsrogue.game.entity.item.weapon.gun.Gun;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 02:06 PM.
 */
public class Codex {
    
    public static CodexItem<Faction> factions = new CodexItem<Faction>("factions");
    public static CodexItem<Creature> creatures = new CodexItem<Creature>("creatures");
    public static CodexItem<Item> items = new CodexItem<Item>("items");
    
    public static Map<Integer, List<GunBlueprint>> gunBlueprints = new HashMap<Integer, List<GunBlueprint>>();
    
    public static void addBlueprint(GunBlueprint blueprint, int level){
        if(!gunBlueprints.containsKey(level)) gunBlueprints.put(level, new ArrayList<GunBlueprint>());
        gunBlueprints.get(level).add(blueprint);
    }
    
    public static List<GunBlueprint> getBlueprints(int level){
        List<GunBlueprint> result = null;

        do{
            result = gunBlueprints.get(level);
            level--;
        }while(result == null && level > 0);
        if(result == null){
            Log.error("Could not find blueprints, starting from level " + level);
            System.exit(-1);
        }
        return result;
    }
    
}
