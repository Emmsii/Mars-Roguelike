package com.mac.marsrogue.engine.io;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 01:34 PM.
 */
public abstract class Loader {
    
    public static final String DATA_FOLDER = "data/";
    
    public abstract void load(String fileName);
}
