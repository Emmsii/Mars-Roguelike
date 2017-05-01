package com.mac.marsrogue.engine.io.loaders;

import com.esotericsoftware.minlog.Log;
import com.mac.marsrogue.engine.util.color.Colors;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 01:38 PM.
 */
public class ColorLoader extends Loader{

    public void load(String fileName){
        Log.info("Loading colors...");

        InputStream in = ColorLoader.class.getClassLoader().getResourceAsStream(DATA_FOLDER + fileName);
        try(BufferedReader br = new BufferedReader(new InputStreamReader(in))){
            String line;
            List<String> lines = new ArrayList<String>();
            while((line = br.readLine()) != null) lines.add(line);
            for(String s : lines){
                if(s.startsWith("#") || s.isEmpty()) continue;
                String[] split = s.split(":");
                String colorName = split[0].trim().toLowerCase();
                String[] rgb = split[1].split(",");
                if(rgb.length != 3) Log.warn("Color " + colorName + " is invalid: " + split[1]);
                else{
                    int r = Integer.parseInt(rgb[0].trim());
                    int g = Integer.parseInt(rgb[1].trim());
                    int b = Integer.parseInt(rgb[2].trim());
                    if(r < 0) r = 0;
                    if(g < 0) g = 0;
                    if(b < 0) b = 0;
                    if(r > 255) r = 255;
                    if(g > 255) g = 255;
                    if(b > 255) b = 255;
                    Colors.add(colorName, new Color(r, g, b));
                }
                Color c = Colors.get(colorName);
                Log.trace("Loaded Color " + colorName + " [" + c.getRed() + ", " + c.getGreen() + ", " + c.getBlue() + "]");
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        Log.debug("Loaded " + Colors.count() + " colors.");
    }
}
