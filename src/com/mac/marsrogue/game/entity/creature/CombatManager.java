package com.mac.marsrogue.game.entity.creature;

import com.esotericsoftware.minlog.Log;
import com.mac.marsrogue.engine.util.Maths.Line;
import com.mac.marsrogue.engine.util.Maths.Maths;
import com.mac.marsrogue.engine.util.Maths.Point;
import com.mac.marsrogue.engine.util.StringUtil;
import com.mac.marsrogue.engine.util.color.ColoredString;
import com.mac.marsrogue.engine.util.color.Colors;
import com.mac.marsrogue.game.entity.item.Item;
import com.mac.marsrogue.game.entity.item.weapon.gun.Gun;
import com.mac.marsrogue.game.map.tile.Tile;
import com.mac.marsrogue.game.MessageLog.LogType;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 03:01 PM.
 */
public class CombatManager {

    private Random random;

    private Creature attacker;
    private Creature defender;

    public CombatManager(Creature attacker, Creature defender){
        this.attacker = attacker;
        this.defender = defender;
        this.random = new Random();
    }

    public void meleeAttack(){

    }

    public void fireWeapon(int xt, int yt){
        if(attacker.weapon == null) return;
        if(!(attacker.weapon instanceof Gun)) return;

        Gun gun = (Gun) attacker.weapon();

        float dx = xt - attacker.x;
        float dy = yt - attacker.y;
        if(dx == 0 && dy == 0) return;
        float l = (float) Math.sqrt(dx * dx + dy * dy);

        dx /= l;
        dy /= l;

        float a = (float) Math.atan2(dy, dx);

        float accuracy = (float) gun.accuracy() / 100f;

        float r = (float) (Math.toRadians(180) * (1 - accuracy) / 2);
        float a1 = a - r;
        float a2 = a + r;

        boolean hit = false;

        List<Integer> ids = new ArrayList<Integer>();
        int totalHits = 0;
        int totalDamage = 0;

        for(int i = 0; i < StringUtil.parseString(gun.fireRate()); i++){
            float ar = Maths.rangeTriangular(a1, a2, random);

            double xr = (Math.cos(ar) * attacker.map().width());
            double yr = (Math.sin(ar) * attacker.map().height());
            for(Point p : new Line(attacker.x, attacker.y, (int) (attacker.x + xr), (int) (attacker.y + yr)).points()){

                Creature other = attacker.map().creature(p.x, p.y, attacker.z);
                if(other != null && other.id() != attacker.id()){ //TODO: Maybe a faction check
                    int damage = StringUtil.parseString(gun.damage());
                    other.damage(damage, attacker.x, attacker.y, new ColoredString("shot", Colors.get("red")));

                    if(!ids.contains(other.id())) ids.add(other.id());
                    totalHits++;
                    totalDamage += damage;
                    hit = true;
                    break; //TODO: For bullet pen, don't break here, continue line with reduced damage
                }

                if(Tile.isWall(attacker.map().tile(p.x, p.y, attacker.z).id) || attacker.map().closedDoor(p.x, p.y, attacker.z)) break;
            }
        }

        if(!hit) attacker.doAction(new ColoredString("completely miss", Colors.get("orange")), LogType.COMBAT);
        else{
            String text = "";
            if(attacker.isPlayer()){
                text = "hit %d " + (ids.size() > 1 ? "enemies" : "enemy") + " " + (totalHits > 1 ? totalHits + " times " : "") + "dealing %d damage";
            }else{
                text = "hit you " + (totalHits > 1 ? totalHits + " times " : "") + "dealing %d damage";
            }
            Color color = attacker.isPlayer() ? Colors.get("green") : Colors.get("red");

            attacker.doAction(new ColoredString(text, color), LogType.COMBAT, ids.size(), totalDamage);
        }
        attacker.setHasUsedEquipment(true);
    }

    public void throwItemAttack(Item item){
        int damage = Math.max(0, item.thrownAttackValue());
        damage = (int) (Math.random() * damage) + 1;

        attacker.doAction(new ColoredString("throw a %s at the %s for %d damage"), LogType.COMBAT, item.name(), defender.name(), damage);
    }
}
