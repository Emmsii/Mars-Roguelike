package com.mac.marsrogue.game.map.decal;

import com.mac.marsrogue.engine.util.Maths.Maths;
import com.mac.marsrogue.engine.util.color.Colors;
import com.mac.marsrogue.game.map.Map;

import java.awt.*;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 02:25 PM.
 */
public class SootMap extends DecalMap{

    //	public static final Color SOOT_COLOR = new Color(54, 54, 54);
    public static final Color SOOT_COLOR = new Color(10, 10, 10);

    public SootMap(Map map) {
        super(map);
    }

    @Override
    public void update(int z) {

    }

    public void addSoot(int x, int y, int z, float amount){
        addSoot(x, y, z, amount, SOOT_COLOR);
    }

    public void addSoot(int x, int y, int z, float amount, Color color){
        setColor(x, y, z, Colors.randomize(SOOT_COLOR, 0.05f, true, random));
        setAlpha(x, y, z, Maths.clamp(alpha(x, y, z) + (Maths.randomizer(amount, 0.15f, random))));
        map.bloodMap().setAlpha(x, y, z, 1f - alpha(x, y, z));
    }
}
