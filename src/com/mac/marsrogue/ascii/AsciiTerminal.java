package com.mac.marsrogue.ascii;

import javax.swing.*;

/**
 * Created by Matt on 29/04/2017.
 */
public class AsciiTerminal extends JFrame{
    
    private AsciiPanel panel;
    
    public AsciiTerminal(String title, int width, int height, AsciiFont font){
        panel = new AsciiPanel(width, height, font);
        this.setTitle(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().add(panel);
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    
    public AsciiPanel getAsciiPanel(){
        return panel;
    }
}
