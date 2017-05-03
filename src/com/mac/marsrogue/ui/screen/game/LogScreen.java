package com.mac.marsrogue.ui.screen.game;

import com.mac.marsrogue.MarsRogue;
import com.mac.marsrogue.engine.ascii.AsciiPanel;
import com.mac.marsrogue.engine.util.color.ColoredString;
import com.mac.marsrogue.game.MessageLog;
import com.mac.marsrogue.ui.screen.Screen;

import java.awt.event.KeyEvent;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 03:29 PM.
 */
public class LogScreen extends Screen{

    private MessageLog log;
    private int messageCount;

    private String defaultTitle;

    public LogScreen(int x, int width, MessageLog log, int messageCount, String title) {
        super(x, MarsRogue.height() - (messageCount + 2), width, messageCount + 2, title);
        this.log = log;
        this.messageCount = messageCount;
        this.defaultTitle = title;
    }

    public LogScreen(MessageLog log, int messageCount, String title) {
        super(0, MarsRogue.height() - (messageCount + 2), MarsRogue.width() / 2, messageCount + 2, title);
        this.log = log;
        this.messageCount = messageCount;
        this.defaultTitle = title;
    }

    @Override
    public Screen input(KeyEvent e) {
        return null;
    }

    @Override
    public void render(AsciiPanel panel) {
        setTitle(defaultTitle + " (" + log.newEntries() + ")");
        clearWithBorder(panel, true);
        for(int i = 0 ; i < messageCount; i++){
            int get = log.getEntries().size() - i - 1;
            if(get < 0) continue;
            ColoredString entry = log.getEntries().get(get);
            panel.write(entry.text, x + 1, y + height - 2 - i, entry.color);
        }
        log.resetNewEntryCount();
    }
}
