package com.mac.marsrogue;

import com.esotericsoftware.minlog.Log;
import com.mac.marsrogue.ascii.AsciiFont;
import com.mac.marsrogue.ascii.AsciiPanel;
import com.mac.marsrogue.ascii.CustomAsciiTerminal;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Project: Mars Roguelike
 * Created by Matt on 29/04/2017 at 04:26 PM.
 */
public class MarsRogue {
    
    public static final String TITLE = "Mars Rogue";
    public static final String VERSION = "v0.1";
    
    public static final AsciiFont FONT = AsciiFont.DRAKE_10x10;
    
    private static final int WIDTH = 16;
    private static final int HEIGHT = 9;
    private static final int SCALE = 7;
    
    private CustomAsciiTerminal terminal;
    private AsciiPanel panel;
    
    public MarsRogue(){
        Log.set(Log.LEVEL_TRACE);
        
        terminal = new CustomAsciiTerminal(TITLE + " " + VERSION, width(), height(), FONT){
            @Override
            public void onKeyEvent(KeyEvent e) {
                input(e);
            }
        };
        
        panel = terminal.getAsciiPanel();
        
        render();
    }
    
    private void input(KeyEvent key){
        render();
    }
    
    private void render(){
        panel.clear();
    
        for(int y = 0; y < height(); y++){
            for(int x = 0; x < width(); x++){
                panel.write((char) (Math.random() * 255), x, y, new Color((float) Math.random(), (float) Math.random(), (float) Math.random()));
            }
        }
        
        terminal.repaint();
    }
    
    public static int width(){
        return WIDTH * SCALE;
    }
    
    public static int height(){
        return HEIGHT * SCALE;
    }
}
