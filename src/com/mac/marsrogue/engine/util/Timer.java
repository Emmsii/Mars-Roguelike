package com.mac.marsrogue.engine.util;

/**
 * Project: Mars Roguelike
 * Created by Matt on 30/04/2017 at 09:13 AM.
 */
public class Timer {
    
    private double startTime;
    private double endTime;
    private boolean hasFinished;
    private boolean running;
    
    public Timer(){
        this.startTime = 0;
        this.endTime = 0;
        this.hasFinished = false;
        this.running = false;
    }
    
    public void start(){
        startTime = System.nanoTime();
        running = true;
    }
    
    public void stop(){
        endTime = System.nanoTime();
        hasFinished = true;
        running = false;
    }
    
    public String result(){
        if(running) return "Still Running";
        if(!hasFinished) return "Still Running";
        String result = "";
        String unit = "";
        
        double totalTime = (endTime - startTime) / 1000000;
        if(totalTime >= 1000) unit = "s";
        else unit = "ms";
        
        result += totalTime + unit;
        
        return result;
    }
}
