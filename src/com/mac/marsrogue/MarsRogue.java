package com.mac.marsrogue;

import com.esotericsoftware.minlog.Log;

import com.mac.marsrogue.engine.util.Timer;
import com.mac.marsrogue.engine.io.ColorLoader;
import com.mac.marsrogue.engine.io.Config;
import com.mac.marsrogue.game.Game;
import com.mac.marsrogue.engine.ascii.*;
import com.mac.marsrogue.ui.screen.GameScreen;
import com.mac.marsrogue.ui.screen.Screen;

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
    
    private Game game;
    private Screen screen;
    
    public MarsRogue(){
        Log.set(Log.LEVEL_TRACE);
        
        loadData();
        
        terminal = new CustomAsciiTerminal(TITLE + " " + VERSION, width(), height(), FONT){
            @Override
            public void onKeyEvent(KeyEvent e) {
                input(e);
            }
        };
        
        panel = terminal.getAsciiPanel();
        
        game = new Game();
        game.init();
        screen = new GameScreen(game);
        
        render();
    }
    
    private void loadData(){
        Log.info("Loading...");
        Timer timer = new Timer();
        timer.start();

        Config.load();
        
        new ColorLoader().load("colors.txt");
                
        timer.stop();
        Log.info("Loaded data in " + timer.result());
    }
    
    private void input(KeyEvent key){
        screen = screen.input(key);
        render();
    }
    
    private void render(){
        panel.clear();
        screen.render(panel);
        terminal.repaint();
    }
    
    public static int width(){
        return WIDTH * SCALE;
    }
    
    public static int height(){
        return HEIGHT * SCALE;
    }
}
