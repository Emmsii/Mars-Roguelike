package com.mac.marsrogue.engine.io.loaders;

import com.esotericsoftware.minlog.Log;
import com.mac.marsrogue.engine.util.color.Colors;
import com.mac.marsrogue.game.codex.Codex;
import com.mac.marsrogue.game.entity.creature.Creature;
import com.mac.marsrogue.game.entity.creature.Faction;
import com.mac.marsrogue.game.entity.creature.limbs.Limb;
import com.mac.marsrogue.game.entity.creature.limbs.LimbController;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 04:00 PM.
 */
public class CreatureLoader extends Loader{

    @Override
    public void load(String fileName)  {
        Log.info("Loading creatures...");
        JSONArray creatures = null;
        try {
            creatures = new JSONLoader(fileName).load().loadJSONArray("creatures");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        if(creatures == null){
            Log.error("Error loading creatures");
            System.exit(-1);
        }

        for(int i = 0; i < creatures.size(); i++){
            JSONObject creature = (JSONObject) creatures.get(i);

            String name = (String) JSONLoader.parseField("name", creature, "unnamed");
            String description = (String) JSONLoader.parseField("description", creature, "No description found");
            char glyph = JSONLoader.parseGlyph((String) JSONLoader.parseField("glyph", creature, '?'));
            Color color = Colors.get((String) JSONLoader.parseField("color", creature, Colors.INVALID));
            int maxHp = (int) ((long) JSONLoader.parseField("max_hp", creature, (long) 100));
            int awareness = (int) ((long) JSONLoader.parseField("awareness", creature, (long) 20));
            Color bloodType = Colors.get((String) (JSONLoader.parseField("blood_type", creature, Colors.INVALID)) + "_blood");
            Faction faction = Codex.factions.get((String) JSONLoader.parseField("faction", creature, "unknown"));
            
            Set<String> newFlags = new HashSet<String>();
            JSONArray flags = (JSONArray) JSONLoader.parseField("flags", creature, null);
            if(flags != null){
                for(int j = 0; j < flags.size(); j++){
                    JSONObject flag = (JSONObject) flags.get(j);
                    String flagName = (String) JSONLoader.parseField("name", flag, "INVALID");
                    newFlags.add(flagName);
                }
            }

            Creature newCreature = new Creature(glyph, color, name, description, bloodType, faction);
            newCreature.setStats(maxHp, awareness);
            
            JSONArray limbs = (JSONArray) JSONLoader.parseField("limbs", creature, null);
            if(limbs != null){
                for(int j = 0; j < limbs.size(); j++){
                    JSONObject limb = (JSONObject) limbs.get(j);
                    String limbName = (String) JSONLoader.parseField("name", limb, Limb.NOTHING);
                    newCreature.limbController().addLimb(Limb.getLimbFromName(limbName));
                }
                
                for(Limb l : LimbController.MIN_REQUIRED_LIMBS){
                    if(!newCreature.limbController().hasLimb(l)){
                        Log.warn("Creature: " + name + " missing required limb: " + l);
                    }
                }
            }
                       
            for(String flag : newFlags) newCreature.addFlag(flag);

            Codex.creatures.put(name, newCreature);
            Log.trace("Loaded Creature: " + name);
        }

        Log.debug("Loaded " + Codex.creatures.count() + " creatures.");
    }
    
}
