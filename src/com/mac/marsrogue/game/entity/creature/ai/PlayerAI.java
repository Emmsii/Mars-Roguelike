package com.mac.marsrogue.game.entity.creature.ai;

import com.esotericsoftware.minlog.Log;
import com.mac.marsrogue.engine.util.color.ColoredString;
import com.mac.marsrogue.game.MessageLog;
import com.mac.marsrogue.game.MessageLog.LogType;
import com.mac.marsrogue.game.entity.creature.Creature;


import java.util.HashMap;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 03:48 PM.
 */
public class PlayerAI extends CreatureAI{
    
    private HashMap<LogType, MessageLog> logs;
    
    public PlayerAI(Creature creature, HashMap<LogType, MessageLog> logs) {
        super(creature);
        this.logs = logs;
    }

    @Override
    public void init() {
        
    }

    @Override
    public void notify(ColoredString message, LogType type) {
        if(!logs.containsKey(type)){
            Log.error("Log type: " + type + "not found.");
            return;
        }
        logs.get(type).add(message);
    }
}
