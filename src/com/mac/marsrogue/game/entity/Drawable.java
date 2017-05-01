package com.mac.marsrogue.game.entity;

import com.mac.marsrogue.engine.ascii.AsciiPanel;
import com.mac.marsrogue.engine.util.color.Colors;

import java.awt.*;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 01:48 PM.
 */
public abstract class Drawable {
    
    protected char glyph;
    protected Color foregroundColor;
    protected Color backgroundColor;
    
    public Drawable(char glyph, Color foregroundColor){
        this(glyph, foregroundColor, Colors.get("default_bg"));
    }
    
    public Drawable(char glyph, Color foregroundColor, Color backgroundColor){
        this.glyph = glyph;
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;
    }
    
    public void draw(AsciiPanel panel, int x, int y){
        if(x < 0 || y < 0 || x >= panel.getWidthInCharacters() || y >= panel.getHeightInCharacters()) return;
        panel.write(glyph, x, y, foregroundColor, backgroundColor);
    }
    
    public char glyph(){
        return glyph;
    }
    
    public Color foregroundColor(){
        return foregroundColor;
    }
    
    public Color backgroundColor(){
        return backgroundColor;
    }
            
}
