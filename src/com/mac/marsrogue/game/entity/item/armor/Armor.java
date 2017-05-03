package com.mac.marsrogue.game.entity.item.armor;

import com.mac.marsrogue.engine.util.color.ColoredString;
import com.mac.marsrogue.engine.util.color.Colors;
import com.mac.marsrogue.game.entity.creature.Creature;
import com.mac.marsrogue.game.entity.creature.limbs.Limb;
import com.mac.marsrogue.game.entity.item.Equipable;
import com.mac.marsrogue.game.entity.item.Item;
import com.mac.marsrogue.game.MessageLog.LogType;

import java.awt.Color;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 02:59 PM.
 */
public class Armor extends Item implements Equipable{

    private boolean equipped;

    protected Limb armorLocation;

    protected int maxDurability;
    protected int durability;
    protected float damageReduction;

    public Armor(char glyph, Color color, String name, String description, Limb armorLocation) {
        super(glyph, color, name, description);
        this.armorLocation = armorLocation;
        this.equipped = false;
    }

    public void setStats(int maxDurability, float damageReduction){
        this.maxDurability = maxDurability;
        this.durability = maxDurability;
        this.damageReduction = damageReduction;
    }

    @Override
    public void equip(Creature creature) {

        if(creature.armor(armorLocation) != null){
            if(creature.armor(armorLocation).equals(this)){
                creature.unequip(creature.armor(armorLocation));
                return;
            }
            creature.unequip(creature.armor(armorLocation));
        }

        creature.setArmor(armorLocation, this);
        creature.doAction(new ColoredString("equip a %s"), LogType.MESSAGE, name);
        equipped = true;
    }

    @Override
    public void unequip(Creature creature) {
        creature.setArmor(armorLocation, null);
        creature.doAction(new ColoredString("unequip a %s"), LogType.MESSAGE, name);
        equipped = false;
    }
    
    public Limb armorLocation(){
        return armorLocation;
    }

    @Override
    public boolean isEquipped() {
        return equipped;
    }

    public int maxDurability(){
        return maxDurability;
    }

    public int durability(){
        return durability;
    }

    public float damageReduction(){
        return damageReduction;
    }

    public float score(){
        return 0f;
    }
}
