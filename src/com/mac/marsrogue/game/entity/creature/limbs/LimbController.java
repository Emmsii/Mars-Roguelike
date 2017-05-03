package com.mac.marsrogue.game.entity.creature.limbs;

import com.mac.marsrogue.engine.util.Pool;

import java.util.HashSet;
import java.util.Set;

/**
 * Project: Mars Roguelike
 * Created by Matt on 03/05/2017 at 08:31 AM.
 */
public class LimbController {
    
    public static final Limb[] MIN_REQUIRED_LIMBS = { Limb.TORSO };
    
    private Set<Limb> limbs;
    
    public LimbController(){
        this.limbs = new HashSet<Limb>();
    }
    
    public Limb getLimbToHit(){
        Pool<Limb> pool = new Pool<Limb>();
        
        int totalHitChance = 0;
        for(Limb l : limbs){
            totalHitChance += l.hitChance;
            pool.add(l, l.hitChance);
        }
        int missChance = 100 - totalHitChance;
        if(missChance > 0) pool.add(Limb.NOTHING, missChance);
        
        return pool.get();
    }
    
    public LimbController addLimb(Limb limb){
        if(!limbs.contains(limb)) limbs.add(limb);
        return this;
    }
    
    public boolean removeLimb(Limb limb){
        return limbs.remove(limb);
    }
    
    public boolean hasLimb(Limb limb){
        return limbs.contains(limb);
    }
}
