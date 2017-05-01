package com.mac.marsrogue.engine.io.loaders;

import com.esotericsoftware.minlog.Log;
import com.mac.marsrogue.engine.util.Maths.Maths;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 01:33 PM.
 */
public class JSONLoader {
    
    private final String path;
    private JSONObject object;
    
    public JSONLoader(String name){
        this.path = Loader.DATA_FOLDER + name + ".json";
    }
    
    public JSONLoader load() throws IOException, ParseException {
        InputStream in = JSONLoader.class.getClassLoader().getResourceAsStream(path);
        object = (JSONObject) new JSONParser().parse(new BufferedReader(new InputStreamReader(in)));
        return this;
    }
    
    public JSONArray loadJSONArray(String name){
        if(object == null){
            Log.error("Error loading array: " + name + ". JSONObject is null");
            return null;
        }
        JSONArray array = (JSONArray) object.get(name);
        if(array == null){
            Log.error("Error loading array: " + name + " from: " + path);
            return null;
        }
        return array;
    }

    public static Object parseField(String key, JSONObject object, Object def){
        if(object.containsKey(key)) return object.get(key);
        else{
            Log.warn("Unknown key: " + key);
            return def;
        }
    }

    public static char parseGlyph(String input){
        input = input.trim();
        if(Maths.isInteger(input)){
            int value = Integer.parseInt(input);
            if(value > 255 || value < 0){
                Log.error("Error: Glyph must be at integer between 0-255.");
                return '?';
            }
            return (char) Integer.parseInt(input);
        }else{
            if(input.length() > 1){
                Log.error("Error: Glyph must be one character long. '" + input + "'");
                return '?';
            }
            return input.charAt(0);
        }
    }
}
