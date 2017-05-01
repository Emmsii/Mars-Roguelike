package com.mac.marsrogue.game.map.decal;

import com.mac.marsrogue.engine.util.color.Colors;
import com.mac.marsrogue.game.map.Map;

import java.awt.*;
import java.util.HashMap;
import java.util.Random;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 02:24 PM.
 */
public abstract class DecalMap {
    protected final Random random;

    protected final Map map;
    protected final int width, height, depth;

    protected HashMap<Integer, Color[][]> color;
    protected HashMap<Integer, float[][]> alpha;

    protected int time;
    protected int updateFrequency;

    public DecalMap(Map map){
        this(map, 5);
    }

    public DecalMap(Map map, int updateFrequency){
        this.map = map;
        this.width = map.width();
        this.height = map.height();
        this.depth = map.depth();
        this.color = new HashMap<Integer, Color[][]>();
        this.alpha = new HashMap<Integer, float[][]>();
        this.random = new Random();
        this.updateFrequency = updateFrequency;

        for(int z = 0; z < depth; z++){
            color.put(z, new Color[width][height]);
            alpha.put(z, new float[width][height]);
        }
    }

    public abstract void update(int z);

    public void setColor(int x, int y, int z, Color color){
        if(!map.inBounds(x, y, z)) return;
        this.color.get(z)[x][y] = color;
    }

    public void setAlpha(int x, int y, int z, float alpha){
        if(!map.inBounds(x, y, z)) return;
        this.alpha.get(z)[x][y] = alpha;
    }

    public Color color(int x, int y, int z){
        if(!map.inBounds(x, y, z)) return Colors.get("black");
        return color.get(z)[x][y];
    }

    public float alpha(int x, int y, int z){
        if(!map.inBounds(x, y, z)) return 0f;
        return alpha.get(z)[x][y];
    }
    
}
