package com.mac.marsrogue.ui.screen;

import com.mac.marsrogue.game.Game;
import com.mac.marsrogue.engine.ascii.AsciiPanel;

import java.awt.event.KeyEvent;

/**
 * Project: Mars Roguelike
 * Created by Matt on 29/04/2017 at 05:18 PM.
 */
public class GameScreen extends Screen{
    
    private Game game;
    
    public GameScreen(Game game){
        super("Game");
        this.game = game;
        
    }

    @Override
    public Screen input(KeyEvent key) {
        return this;
    }

    @Override
    public void render(AsciiPanel panel) {
        clearWithBorder(panel, true);
    }
}
