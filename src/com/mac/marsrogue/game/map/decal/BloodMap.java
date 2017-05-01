package com.mac.marsrogue.game.map.decal;

import com.mac.marsrogue.engine.util.Maths.Point;
import com.mac.marsrogue.engine.util.Maths.Line;
import com.mac.marsrogue.engine.util.color.Colors;
import com.mac.marsrogue.game.map.Map;

import java.awt.*;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 02:25 PM.
 */
public class BloodMap extends DecalMap{

    public static final float BLOOD_CHANGE_FREQUENCY = 0.1f;
    public static final float MIN_TRANSPARENCY = 0.55f;

    public BloodMap(Map map) {
        super(map);
    }

    public void update(int z){
        time++;
        if(time % updateFrequency != 0) return;

        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                if(map.solid(x, y, z)) continue;
                if(color.get(z)[x][y] == null) continue;
                if(random.nextFloat() <= BLOOD_CHANGE_FREQUENCY){
                    modifyTransparency(x, y, z, random.nextFloat() * 0.1f);
                    darkenBlood(x, y, z);
                }
            }
        }
    }

    public void bloodSplat(int x, int y, int z, int xt, int yt, int strength, Color color){
        float angle = (float) Math.atan2(y - yt, x - xt);
        float dx = (float) (x + (Math.cos(angle) * strength));
        float dy = (float) (y + (Math.sin(angle) * strength));

        for(int i = 0; i < strength * 0.4 / 2; i++){
            for(Point p : new Line(x, y, (int) (dx + ((Math.random() * 2) - 1) * 2), (int) (dy + ((Math.random() * 2) - 1) * 2)).points()){
                if(Math.random() < 0.225) continue;
                if(map.solid(p.x, p.y, p.z)) break;
                addBlood(p.x, p.y, z, color);
            }
        }
    }

    public void modifyTransparency(int x, int y, int z, float amount){
        if(alpha.get(z)[x][y] < MIN_TRANSPARENCY) return;
        if(alpha.get(z)[x][y] - amount < 0) alpha.get(z)[x][y] = 0;
        else alpha.get(z)[x][y] -= amount;
    }

    public void darkenBlood(int x, int y, int z){
        if(alpha.get(z)[x][y] < MIN_TRANSPARENCY) return;
        color.get(z)[x][y] = Colors.darken(color.get(z)[x][y], (1f - (alpha.get(z)[x][y] / 1f)) / 2f);
    }

    public void addBlood(int x, int y, int z, Color bloodColor){
        if(!map.inBounds(x, y, z)) return;
        alpha.get(z)[x][y] = 0.8f;
        Color newBlood = Colors.randomize(bloodColor, 0.25f, false, random);
        Color mixedBlood = color.get(z)[x][y];
        if(mixedBlood == null) color.get(z)[x][y] = newBlood;
        else color.get(z)[x][y] = Colors.blend(newBlood, mixedBlood, 0.5f);
    }
}
