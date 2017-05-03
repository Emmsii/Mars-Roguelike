package com.mac.marsrogue.game.entity.creature.limbs;

import com.esotericsoftware.minlog.Log;

/**
 * Project: Mars Roguelike
 * Created by Matt on 03/05/2017 at 08:32 AM.
 */
public enum Limb {
    
    HEAD(5, "head", 0), TORSO(47, "torso", 1), ARM_RIGHT(12, "right arm", 2), ARM_LEFT(12, "left arm", 3), LEG_LEFT(12, "left leg", 4), LEG_RIGHT(12, "right leg", 5), NOTHING(0, "", 6);

    public static final Limb[] ALL = { HEAD, TORSO, ARM_RIGHT, ARM_LEFT, LEG_LEFT, LEG_RIGHT };
    
    public int hitChance;
    public String prettyName;
    public int order;
    
    Limb(int hitChance, String prettyName, int order){
        this.hitChance = hitChance;
        this.prettyName = prettyName;
        this.order = order;
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
