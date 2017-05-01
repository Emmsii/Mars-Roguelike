package com.mac.marsrogue.game.entity.item.armor;

import com.mac.marsrogue.engine.util.color.ColoredString;
import com.mac.marsrogue.engine.util.color.Colors;
import com.mac.marsrogue.game.entity.creature.Creature;
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

    protected ArmorLocation armorLocation;

    protected int maxDurability;
    protected int durability;
    protected float damageReduction;

    public Armor(char glyph, Color color, String name, String description, ArmorLocation armorLocation) {
        super(glyph, color, name, description);
        this.armorLocation = armorLocation;
    }

    public void setStats(int maxDurability, float damageReduction){
        this.maxDurability = maxDurability;
        this.durability = maxDurability;
        this.damageReduction = damageReduction;
    }

    @Override
    public void equip(Creature creature) {
        if(creature.head() != null && creature.head() != this) creature.unequip(creature.head());
        if(creature.chest() != null && creature.chest() != this) creature.unequip(creature.chest());
        if(creature.legs() != null && creature.legs() != this) creature.unequip(creature.legs());

        if(isEquipped()){
            unequip(creature);
            return;
        }

        switch(armorLocation){
            case HEAD: creature.setHead(this); break;
            case CHEST:
                //				if(creature.chest() != null){
                //					if(creature.chest() == this) return;
                //					else creature.unequip(creature.chest());
                //				}
                creature.setChest(this);
                break;
            case LEGS: creature.setChest(this); break;
            default:
                creature.notify(new ColoredString("I don't know what to do with this %s", Colors.get("orange")), LogType.MESSAGE, name);
                return;
        }

        creature.doAction(new ColoredString("equip a %s"), LogType.MESSAGE, name);
        equipped = true;
    }

    @Override
    public void unequip(Creature creature) {
        switch(armorLocation){
            case HEAD: creature.setHead(null); break;
            case CHEST: creature.setChest(null); break;
            case LEGS: creature.setLegs(null); break;
            default:
                creature.notify(new ColoredString("I don't know what to do with this %s", Colors.get("orange")), LogType.MESSAGE, name); //TODO: Maybe remove this
                return;
        }
        creature.doAction(new ColoredString("unequip a %s"), LogType.MESSAGE, name);
        equipped = false;
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
