package com.mac.marsrogue.game.map.object;

import com.mac.marsrogue.engine.util.Maths.Point;
import com.mac.marsrogue.engine.util.color.Colors;
import com.mac.marsrogue.game.entity.Entity;
import com.mac.marsrogue.game.map.Map;

import java.awt.Color;


/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 02:44 PM.
 */
public class MapObject extends Entity{

    public MapObject(char glyph, Color color, String name, String description) {
        this(glyph, color, Colors.get("default_bg"), name, description);
    }

    public MapObject(char glyph, Color foregroundColor, Color backgroundColor, String name, String description) {
        super(glyph, foregroundColor, backgroundColor, name, description);
    }

    @Override
    public void update() {

    }

    public void place(Map map, Point spawn) {
        if(!map.inBounds(spawn.x, spawn.y, spawn.z)) return;
        map.add(this, spawn);
    }
}
