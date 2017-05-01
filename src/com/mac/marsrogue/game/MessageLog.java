package com.mac.marsrogue.game;

import com.mac.marsrogue.engine.util.color.ColoredString;
import com.mac.marsrogue.engine.util.color.Colors;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 02:47 PM.
 */
public class MessageLog {

    public enum LogType{ COMBAT, MESSAGE }

    private List<ColoredString> entries;

    private int newEntries = 0;

    public MessageLog() {
        this.entries = new ArrayList<ColoredString>();
    }

    public void add(String text){
        add(new ColoredString(text, Colors.get("default_fg")));
    }

    public void add(ColoredString string){
        if(!checkForRepeat(string.text, string.color)) entries.add(string);
        newEntries++;
    }

    private boolean checkForRepeat(String log, Color color){
        if(entries == null || entries.isEmpty()) return false;

        String previous = entries.get(entries.size() - 1).text;
        String previousClean = previous;

        String[] previousSplit = previous.split(" ");
        if(previousSplit[previousSplit.length - 1].startsWith("x")){
            previousClean = "";
            for(int i = 0; i < previousSplit.length - 1; i++) previousClean += previousSplit[i] + " ";
        }

        if(previousClean.trim().equals(log.trim())){
            String[] split = previous.split(" ");
            int count = 1;
            if(split[split.length - 1].startsWith("x")){
                String last = split[split.length - 1];
                count = Integer.parseInt(last.substring(1));
            }
            count += 1;
            entries.set(entries.size() - 1, new ColoredString(log += " x" + count, color));
            return true;
        }
        return false;
    }

    public int newEntries(){
        return newEntries;
    }

    public void resetNewEntryCount(){
        newEntries = 0;
    }

    public List<ColoredString> getEntries(){
        return entries;
    }
}
