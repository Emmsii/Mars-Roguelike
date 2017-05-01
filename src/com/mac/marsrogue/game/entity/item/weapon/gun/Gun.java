package com.mac.marsrogue.game.entity.item.weapon.gun;

import com.mac.marsrogue.engine.util.StringUtil;
import com.mac.marsrogue.game.entity.item.Rarity;
import com.mac.marsrogue.game.entity.item.weapon.Weapon;

import java.awt.*;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 03:02 PM.
 */
public class Gun extends Weapon {

    protected Rarity rarity;

    protected String damage;
    protected int accuracy;
    protected GunClass gunClass;
    protected GunProjectile projectile;
    protected GunType type;

    protected String fireRate;
    protected int clipSize;
    protected float charge;
    protected int clips;

    public Gun(char glyph, Color color, String name, String description) {
        super(glyph, color, name, description);
    }

    public void setStats(String damage, int accuracy, String fireRate, int clipSize, GunClass gunClass, GunProjectile projectile, GunType type, Rarity rarity){
        this.damage = damage;
        this.accuracy = accuracy;
        this.projectile = projectile;
        this.gunClass = gunClass;
        this.type = type;
        this.clipSize = clipSize;
        this.fireRate = fireRate;
        this.rarity = rarity;
    }

    public float score(){
        float result = 1f;

        int damageScore = (StringUtil.totalValue(damage) / 5) + 1;
        int accuracyScore = (accuracy / 8) + 1;
        int fireRateScore = (StringUtil.totalValue(fireRate) / 2) + 1;
        int clipSizeScore = clipSize;
        float rarityScore = (rarity.multiplier * 0.85f);

        result += (damageScore + accuracyScore + fireRateScore + clipSizeScore) * rarityScore;
        //		
        //		System.out.println("Damage Score: " + damageScore);
        //		System.out.println("Accuracy Score: " + accuracyScore);
        //		System.out.println("Fire Rate Score: " + fireRateScore);
        //		System.out.println("Clip Size Score: " + clipSizeScore);
        //		System.out.println("Rarity Multiplier: " + rarityScore);
        //		
        //		System.out.println("Gun Score: " + result);

        return result * 0.1f;
    }

    public String damage(){
        return damage;
    }

    public int accuracy(){
        return accuracy;
    }

    public GunClass gunClass(){
        return gunClass;
    }

    public GunProjectile projectile(){
        return projectile;
    }

    public GunType type(){
        return type;
    }

    public String fireRate(){
        return fireRate;
    }

    public int clipSize(){
        return clipSize;
    }

    public float charge(){
        return charge;
    }

    public int clips(){
        return clips;
    }

    public Rarity rarity(){
        return rarity;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String description() {
        return damage + " " + (int) (accuracy * 100) + "% " + fireRate;

    }
}
