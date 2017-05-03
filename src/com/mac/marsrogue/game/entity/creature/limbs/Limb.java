package com.mac.marsrogue.game.entity.creature.limbs;

import com.esotericsoftware.minlog.Log;

/**
 * Project: Mars Roguelike
 * Created by Matt on 03/05/2017 at 08:32 AM.
 */
public enum Limb {
    
    HEAD(5), TORSO(47), ARM_RIGHT(12), ARM_LEFT(12), LEG_LEFT(12), LEG_RIGHT(12), NOTHING(0);
    
    public int hitChance;
    
    Limb(int hitChance){
        this.hitChance = hitChance;
    }
    
    public static Limb getLimbFromName(String name){
        name = name.toUpperCase().trim().replaceAll(" ", "_");
        Limb result = Limb.NOTHING;
        try {
            result = Limb.valueOf(name);
        }catch(IllegalArgumentException e) {
            Log.warn("Unknown limb: " + name + ". Returning Limb.NOTHING");
        }
        return result;
    }
}
