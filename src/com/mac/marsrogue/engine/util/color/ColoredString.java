package com.mac.marsrogue.engine.util.color;

import java.awt.*;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 02:47 PM.
 */
public class ColoredString {

    public String text;
    public Color color;

    public ColoredString() {

    }

    public ColoredString(String text){
        this(text, Colors.get("default_fg"));
    }

    public ColoredString(String text, Color color){
        this.text = text;
        this.color = color;
    }
}
