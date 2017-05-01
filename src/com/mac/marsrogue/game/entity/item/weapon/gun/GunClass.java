package com.mac.marsrogue.game.entity.item.weapon.gun;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 03:03 PM.
 */
public class GunClass {

    public static List<GunClass> values = new ArrayList<GunClass>();

    private String name;
    private char glyph;
    private String baseDamage;
    private String baseAccuracy;
    private List<GunType> allowedTypes;

    public static final GunClass pistol = new GunClass("Pistol", (char) 170, "3-6", "80-95", GunType.ALL);
    public static final GunClass rifle = new GunClass("Rifle", (char) 170, "8-12", "80-90", GunType.AUTOMATIC, GunType.BURST, GunType.SINGLE);
    public static final GunClass shotgun = new GunClass("Shotgun", (char) 170, "2-4", "45-60", GunType.SPREAD);
    public static final GunClass longshot = new GunClass("Longshot", (char) 170, "10-20", "80-100", GunType.SINGLE);

    public GunClass(String name, char glyph, String baseDamage, String baseAccuracy, GunType ... allowed){
        this.name = name;
        this.glyph = glyph;
        this.baseDamage = baseDamage;
        this.baseAccuracy = baseAccuracy;
        this.allowedTypes = new ArrayList<GunType>();
        for(GunType t : allowed) allowedTypes.add(t);
        GunClass.values.add(this);
    }

    public String name(){
        return name;
    }

    public char glyph(){
        return glyph;
    }

    public String baseDamage(){
        return baseDamage;
    }

    public String baseAccuracy(){
        return baseAccuracy;
    }

    public List<GunType> allowedTypes(){
        return allowedTypes;
    }
}
