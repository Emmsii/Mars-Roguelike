package com.mac.marsrogue.engine.ascii;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Project: Mars Roguelike
 * Created by Matt on 29/04/2017 at 04:23 PM at 04:24 PM.
 */
public class CustomAsciiTerminal extends AsciiTerminal{

    public CustomAsciiTerminal(String title, int width, int height, AsciiFont font) {
        super(title, width, height, font);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowCloseHandler());
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                onKeyEvent(e);
            }
        });
    }
    
    public void onKeyEvent(KeyEvent e){
        
    }
}
