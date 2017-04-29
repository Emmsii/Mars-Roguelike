package com.mac.marsrogue.ui.ascii;

import com.esotericsoftware.minlog.Log;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by Matt on 29/04/2017.
 */
public class WindowCloseHandler extends WindowAdapter{

    @Override
    public void windowClosing(WindowEvent e) {
        Log.info("Bye!");
        System.exit(0);
    }
}
