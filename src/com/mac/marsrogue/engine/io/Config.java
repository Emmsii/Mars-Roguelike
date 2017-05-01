package com.mac.marsrogue.engine.io;

import com.esotericsoftware.minlog.Log;

import java.io.*;
import java.util.Properties;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 01:41 PM.
 */
public class Config {
    
    private static final String FILE_NAME = "config.txt";
    
    public static boolean friendlyFire = false;


    public static void reset(){
        Log.debug("Resetting config.");
    }
    
    public static void load(){
        Log.info("Loading config...");
        
        if(!exists()){
            Log.info("Config file does not exist.");
            save();
        }
        
        Properties prop = new Properties();
        InputStream in = null;

        try{
            in = new FileInputStream(FILE_NAME);
            prop.load(in);

            friendlyFire = prop.getProperty("friendly_fire") != null;
        }catch(IOException e){
            Log.error("Error loading config.");
            e.printStackTrace();
        }finally{
            if(in != null){
                try{
                    in.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static void save(){
        Log.info("Saving config...");
        Properties prop = new Properties();
        OutputStream out = null;

        try{
            out = new FileOutputStream(FILE_NAME);

            prop.setProperty("friendly_fire", Boolean.toString(friendlyFire));

            prop.store(out, null);
        }catch(IOException e){
            Log.error("Error saving config.");
            e.printStackTrace();
        }finally{
            if(out != null){
                try{
                    out.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
    
    public static boolean exists(){
        return new File(FILE_NAME).exists();
    }
    
}
