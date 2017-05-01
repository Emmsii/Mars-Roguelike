package com.mac.marsrogue.engine.io.loaders;

import com.esotericsoftware.minlog.Log;
import com.mac.marsrogue.game.codex.Codex;
import com.mac.marsrogue.game.entity.creature.Faction;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.HashMap;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 04:02 PM.
 */
public class FactionLoader extends Loader{

    @Override
    public void load(String fileName) {
        Log.info("Loading factions...");
        JSONArray factions = null;
        try {
            factions = new JSONLoader(fileName).load().loadJSONArray("factions");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        if(factions == null){
            Log.error("Error loading factions");
            System.exit(-1);
        }

        for(int i = 0; i < factions.size(); i++){
            JSONObject faction = (JSONObject) factions.get(i);

            String name = (String) JSONLoader.parseField("name", faction, "unnamed");
            HashMap<String, Byte> relations = new HashMap<String, Byte>();

            if(faction.containsKey("relations")){
                JSONArray relationsArray = (JSONArray) faction.get("relations");

                for(int j = 0; j < relationsArray.size(); j++){
                    JSONObject relation = (JSONObject) relationsArray.get(j);
                    String factionName = (String) JSONLoader.parseField("faction", relation, "unknown_faction");
                    byte action = Faction.parseAction((String) JSONLoader.parseField("action", relation, "do_nothing"));
                    relations.put(factionName, action);
                }
            }else Log.warn("No relations found for faction: " + name);

            Faction newFaction = new Faction(name, relations);
            Codex.factions.put(name.toLowerCase().trim(), newFaction);
            Log.trace("Loaded Faction: " + name);
        }

        Log.debug("Loaded: " + Codex.factions.count() + " factions.");
    }
}
