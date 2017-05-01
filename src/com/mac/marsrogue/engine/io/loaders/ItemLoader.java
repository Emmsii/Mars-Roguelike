package com.mac.marsrogue.engine.io.loaders;

import com.esotericsoftware.minlog.Log;
import com.mac.marsrogue.engine.util.color.Colors;
import com.mac.marsrogue.game.codex.Codex;
import com.mac.marsrogue.game.entity.item.Item;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.IOException;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 04:00 PM.
 */
public class ItemLoader extends Loader{

    public static final String ITEMS_FILE = "items";

    public void load(String fileName){
        loadItems(ITEMS_FILE);
    }

    private void loadItems(String fileName){
        Log.info("Loading items...");
        JSONArray items = null;
        try {
            items = new JSONLoader(fileName).load().loadJSONArray("items");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        if(items == null){
            Log.error("Error loading items");
            System.exit(-1);
        }

        for(int i = 0; i < items.size(); i++){
            JSONObject item = (JSONObject) items.get(i);

            String name = (String) JSONLoader.parseField("name", item, "unnamed");
            String description = (String) JSONLoader.parseField("description", item, "No description found");
            char glyph = JSONLoader.parseGlyph((String) JSONLoader.parseField("glyph", item, '?'));
            Color color = Colors.get((String) JSONLoader.parseField("color", item, Colors.INVALID));

            Item newItem = new Item(glyph, color, name, description);
            Codex.items.put(name, newItem);
            Log.trace("Loaded Item: " + name);
        }

        Log.debug("Loaded " + Codex.items.count() + " items.");
    }

    private void loadWeapons(){

    }

    private void loadArmor(){

    }
}
