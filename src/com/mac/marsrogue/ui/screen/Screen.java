package com.mac.marsrogue.ui.screen;

import com.mac.marsrogue.MarsRogue;
import com.mac.marsrogue.engine.util.Maths.Line;
import com.mac.marsrogue.engine.util.Maths.Point;
import com.mac.marsrogue.engine.util.color.Colors;
import com.mac.marsrogue.ui.ascii.AsciiPanel;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Project: Mars Roguelike
 * Created by Matt on 29/04/2017 at 05:03 PM.
 */
public abstract class Screen {
    
    protected final String letters = "abcdefghijklmnopqrstuvwxyz";
    
    protected String title;
    protected int x, y;
    protected int width, height;
    
    public Screen(){
        this("");
    }
    
    public Screen(String title){
        this(0, 0, MarsRogue.width(), MarsRogue.height(), title);   
    }
    
    public Screen(int x, int y, int width, int height, String title){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.title = title;
    }
    
    public abstract Screen input(KeyEvent key);
    public abstract void render(AsciiPanel panel);
    
    public void clearWithBorder(AsciiPanel panel, boolean thickBorder){
        for(int ya = 0; ya < height; ya++){
            int yp = y + ya;
            for(int xa = 0; xa < width; xa++){
                int xp = x + xa;
                if(xp < 0 || yp < 0 || xp >= panel.getWidthInCharacters() || yp >= panel.getHeightInCharacters()) continue;

                panel.write(' ', xp, yp);

                if(xa == 0 || xa == width - 1) panel.write((char) (thickBorder ? 186 : 179), xp, yp, Colors.get("border"));
                else if(ya == 0 || ya == height - 1) panel.write((char) (thickBorder ? 205 : 196), xp, yp, Colors.get("border"));
                if(xa == 0 && ya == 0) panel.write((char) 254, xp, yp, Colors.get("border"));
                else if(xa == width - 1 && ya == 0) panel.write((char) 254, xp, yp, Colors.get("border"));
                else if(xa == 0 && ya == height - 1) panel.write((char) 254, xp, yp, Colors.get("border"));
                else if(xa == width - 1 && ya == height - 1) panel.write((char) 254, xp, yp, Colors.get("border"));
            }
        }

        if(title != null && title.length() > 0){
            panel.write((char) (thickBorder ? 181 : 180), x + 2, y, Colors.get("border"));
            panel.write((char) (thickBorder ? 198 : 195), x + 3 + title.length(), y, Colors.get("border"));
            panel.write(title, x + 3, y);
        }
    }

    public void drawLine(AsciiPanel panel, int sx, int sy, int ex, int ey, char glyph, Color color, boolean skipEnds){
        Line line = new Line(sx, sy, ex, ey);
        for(int i = skipEnds ? 1 : 0; i < line.length() - (skipEnds ? 1 : 0); i++){
            Point p = line.points().get(i);
            if(!inScreenBounds(p.x, p.y)) continue;
            panel.write(glyph, p.x, p.y, color, Colors.get("default_bg"));
        }
    }

    public void drawLineHor(AsciiPanel panel, int x, int y, int length, Color color, boolean thick){
        for(int i = 0; i < length; i++){
            if(x + i >= panel.getWidthInCharacters()) return;
            panel.write((char) (thick ? 205 : 196), x + i, y, color);
        }
    }

    public void drawLineVert(AsciiPanel panel, int x, int y, int length, Color color, boolean thick){
        for(int i = 0; i < length; i++){
            if(y + i >= panel.getHeightInCharacters()) return;
            panel.write((char) (thick ? 186 : 179), x, y + i, color);
        }
    }

    public boolean inScreenBounds(int x, int y){
        return x >= 0 && y >= 0 && x < width && y < height;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public int width(){
        return width;
    }

    public int height(){
        return height;
    }
}
