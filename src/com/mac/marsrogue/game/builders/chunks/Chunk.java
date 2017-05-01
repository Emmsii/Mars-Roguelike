package com.mac.marsrogue.game.builders.chunks;

import com.mac.marsrogue.game.builders.MapBuilder;
import com.mac.marsrogue.game.map.Map;

import java.util.Random;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 03:33 PM.
 */
public abstract class Chunk {
    
    public int x, y, z;
    
    public Chunk(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public abstract void place(MapBuilder mapBuilder, Map map, int size, Random random);
}
