package com.mac.marsrogue.game.entity.creature;

import com.esotericsoftware.minlog.Log;
import com.mac.marsrogue.engine.util.Maths.Line;
import com.mac.marsrogue.engine.util.Maths.Point;
import com.mac.marsrogue.engine.util.StringUtil;
import com.mac.marsrogue.engine.util.color.ColoredString;
import com.mac.marsrogue.engine.util.color.Colors;
import com.mac.marsrogue.game.entity.Entity;
import com.mac.marsrogue.game.entity.creature.ai.CreatureAI;
import com.mac.marsrogue.game.entity.creature.limbs.Limb;
import com.mac.marsrogue.game.entity.creature.limbs.LimbController;
import com.mac.marsrogue.game.entity.item.Equipable;
import com.mac.marsrogue.game.entity.item.Inventory;
import com.mac.marsrogue.game.entity.item.Item;
import com.mac.marsrogue.game.entity.item.armor.Armor;
import com.mac.marsrogue.game.entity.item.weapon.Explosive;
import com.mac.marsrogue.game.entity.item.weapon.Weapon;
import com.mac.marsrogue.game.map.Map;
import com.mac.marsrogue.game.MessageLog.LogType;
import com.mac.marsrogue.game.map.object.DoorTerminal;
import com.mac.marsrogue.game.map.object.Terminal;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 01:56 PM.
 */
public class Creature extends Entity{

    protected LimbController limbController;
    
    protected Weapon weapon;
   
    protected HashMap<Limb, Armor> armor;
    
    private Inventory<Item> inventory;
    
    protected ColoredString causeOfDeath;
    
    protected CreatureAI ai;
    protected Faction faction;
    protected Color bloodType;

    protected int maxHp;
    protected int hp;
    protected int awareness;
    
    protected Terminal terminal;
    protected boolean hasMoved;
    protected boolean hasUsedEquipment;
    protected int timeStationary;
    
    public Creature(char glyph, Color foregroundColor, String name, String description, Color bloodType, Faction faction) {
        super(glyph, foregroundColor, name, description);
        this.bloodType = bloodType;
        this.limbController = new LimbController();
        this.armor = new HashMap<Limb, Armor>();
        this.inventory = new Inventory<Item>();
        this.faction = faction;
        
        for(Limb l : limbController().limbs()){
            armor.put(l, null);
        }
    }

    public void setStats(int maxHp, int awareness){
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.awareness = awareness;
    }

    public void init(Map map, int id){
        super.init(map, id);
        ai.init();
    }
    
    @Override
    public void update() {
        if(ai != null) ai.update();

        if(!hasMoved) timeStationary++;
        else timeStationary = 0;
    }

    /* Movement Methods */

    public boolean moveBy(int xp, int yp, int zp){
        if(xp == 0 && yp == 0 && zp == 0){
            hasMoved = false;
            return false;
        }
        if(!map.inBounds(x + xp, y + yp, z + zp)){
            hasMoved = false;
            return false;
        }

        Creature other = map.creature(x + xp, y + yp, z + zp);
        if(other == null){
            hasMoved = ai.tryMove(x + xp, y + yp, z + zp);
            return hasMoved;
        }
        else{
            //attack;
            hasMoved = false;
            return true;
        }
    }

    public boolean moveTo(int xp, int yp, int zp){
        if(xp == 0 && yp == 0 && zp == 0) return false;
        if(!map.inBounds(xp, yp, zp)) return false;

        Creature other = map.creature(xp, yp, zp);
        if(other == null) return ai.tryMove(xp, yp, zp);

        return false;
    }
    
    /* Combat Methods */

    public void damage(int damage, int xt, int yt, ColoredString causeOfDeath){
        if(damage >= maxHp){
            //			map.bloodMap().explode(x, y, z, damage, bloodType);
        }else{
//            map.bloodMap().bloodSplat(x, y, z, xt, yt, (int) (damage * 0.4), bloodType);
        }
        modifyHp(-damage, causeOfDeath);
    }
    
    public boolean removeLimb(){
        Limb limbToRemove = limbController.getLimbToHit();
        if(limbToRemove == Limb.NOTHING) return false;
        
        Armor equippedArmor = armor(limbToRemove);
        drop(equippedArmor);
        //drop(limbitem);
        
        return true;
    }
    
    /* Item Methods */

    public void pickup(){
        Item item = map.item(x, y, z);

        if(item == null){
            doAction(new ColoredString("grab at nothing"), LogType.MESSAGE);
            return;
        }

        if(inventory.isFull()) notify(new ColoredString("You are carrying too much to pick up the %s", Colors.get("orange")), LogType.MESSAGE, item.name());
        else{
            doAction(new ColoredString("pickup a %s"), LogType.MESSAGE, item.name());
            map.remove(item);
            inventory.add(item);
        }
    }

    public void drop(Item item){
        if(map.addAtEmptyPoint(item, x, y, z)){
            unequip(item);
            doAction(new ColoredString("drop a %s"), LogType.MESSAGE, item.name());
            inventory.remove(item);
        }else{
            notify(new ColoredString("There is nowhere to drop the %s.", Colors.get("orange")), LogType.MESSAGE, item.name());
        }
    }

    public void throwItem(Item item, int xp, int yp, int zp){
        Point end = new Point(x, y, z);

        for(Point p : new Line(x, y, xp, yp).points()){
            if(map.solid(p.x, p.y, z)) break;
            end = p;
        }

        xp = end.x;
        yp = end.y;

        Creature c = map.creature(xp, yp, zp);

        unequip(item);
        inventory.remove(item);
        map.addAtEmptyPoint(item, xp, yp, zp);

        if(c != null) new CombatManager(this, c).throwItemAttack(item);
        else doAction(new ColoredString("throw a %s"), LogType.COMBAT, item.name());

        if(item instanceof Explosive) ((Explosive) item).arm();
    }

    public void equip(Item item){
        if(item instanceof Equipable) ((Equipable) item).equip(this);
        else notify(new ColoredString("You cannot equip a %s", Colors.get("red")), LogType.MESSAGE, item.name());
    }

    public void unequip(Item item){
        if(item instanceof Equipable) ((Equipable) item).unequip(this);
        else notify(new ColoredString("You cannot unequip a %s", Colors.get("red")), LogType.MESSAGE, item.name());
    }

    public boolean isEquipped(Equipable equipable){
        Log.debug("TODO: Is equiped"); //TODO: Is equipped
        return false;
    }
    
    /* Log Methods */

    public void notify(ColoredString message, LogType logType, Object ... params){
        if(ai == null) return;
        message.text = String.format(message.text, params);
        ai.notify(message, logType);
    }

    public void doAction(ColoredString message, LogType logType, Object ... params){
        for(Creature other : getCreaturesWhoSeeMe()){
            if(other == this) other.notify(new ColoredString("You " + message.text + ".", message.color), logType, params);
            else other.notify(new ColoredString(String.format("The %s %s.", name, StringUtil.makeSecondPerson(message.text)), message.color), logType, params);
        }
    }
    
    /* Util Methods */
    
    public List<Creature> getCreaturesWhoSeeMe(Faction ofFaction){
        List<Creature> others = new ArrayList<Creature>();
        if(map == null) return others;
        for(Creature c : map.creatures(z)){
            //			if(c.id == this.id) continue;
            if(ofFaction == Faction.ALL || ofFaction == null || c.faction().name().equalsIgnoreCase(faction.name())){
                if(c.canSee(x, y, z)) others.add(c);
            }
        }

        return others;
    }

    public List<Creature> getCreaturesWhoSeeMe(){
        return getCreaturesWhoSeeMe(Faction.ALL);
    }

    public boolean canSee(Creature creature){
        return canSee(creature.x, creature.y, creature.z);
    }
    
    public boolean canSee(int xp, int yp, int zp){
        return ai.canSee(xp, yp, zp);
    }

    public boolean canEnter(int xp, int yp, int zp){
        return ai.canEnter(xp, yp, zp);
    }
    
    /* Modifier Methods */

    public void modifyHp(int amount, ColoredString causeOfDeath){
        hp += amount;
        this.causeOfDeath = causeOfDeath;

        if(hp > maxHp) hp = maxHp;
        else if(hp < 1){
            doAction(new ColoredString("die", Colors.get("red")), LogType.COMBAT);
            map.remove(this);
        }
    }
    
    /* Setter Methods */

    public void setAi(CreatureAI ai){
        this.ai = ai;
    }

    public void setWeapon(Weapon weapon){
        this.weapon = weapon;
    }

    public void setArmor(Limb limb, Armor armor){
        this.armor.put(limb, armor);
    }

    public void setMoved(boolean hasMoved){
        this.hasMoved = hasMoved;
    }

    public void setHasUsedEquipment(boolean hasUsedEquipment){
        this.hasUsedEquipment = hasUsedEquipment;
    }

    public void setTerminal(DoorTerminal terminal){
        this.terminal = terminal;
    }
    
    /* Getter Methods */

    public LimbController limbController(){
        return limbController;
    }
    
    public Inventory<Item> inventory(){
        return inventory;
    }

    public Weapon weapon(){
        return weapon;
    }

    public Armor armor(Limb limb){
        return armor.get(limb);
    }
    
    public HashMap<Limb, Armor> armor(){
        return armor;
    }

    public int awareness(){
        return awareness;
    }

    public int hp(){
        return hp;
    }

    public int maxHp(){
        return maxHp;
    }

    public Faction faction(){
        return faction;
    }

    public ColoredString causeOfDeath(){
        return causeOfDeath;
    }

    public int timeStationary(){
        return timeStationary;
    }

    public boolean hasMoved(){
        return hasMoved;
    }

    public boolean hasUsedEquipment(){
        return hasUsedEquipment;
    }

    public Terminal terminal(){
        return terminal;
    }

    public CreatureAI ai(){
        return ai;
    }
    
    public boolean isPlayer(){
        return glyph == '@';
    }
        
    /* Saving/Loading Methods */
   
}
