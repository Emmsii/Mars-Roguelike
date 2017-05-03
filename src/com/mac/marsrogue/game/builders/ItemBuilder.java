package com.mac.marsrogue.game.builders;

import com.esotericsoftware.minlog.Log;
import com.mac.marsrogue.engine.util.Maths.Maths;
import com.mac.marsrogue.engine.util.Pool;
import com.mac.marsrogue.engine.util.StringUtil;
import com.mac.marsrogue.engine.util.color.Colors;
import com.mac.marsrogue.game.Globals;
import com.mac.marsrogue.game.codex.Codex;
import com.mac.marsrogue.game.entity.creature.limbs.Limb;
import com.mac.marsrogue.game.entity.item.Rarity;
import com.mac.marsrogue.game.entity.item.armor.Armor;
import com.mac.marsrogue.game.entity.item.blueprint.GunBlueprint;
import com.mac.marsrogue.game.entity.item.weapon.Explosive;
import com.mac.marsrogue.game.entity.item.weapon.gun.Gun;
import com.mac.marsrogue.game.entity.item.weapon.gun.GunClass;
import com.mac.marsrogue.game.entity.item.weapon.gun.GunProjectile;
import com.mac.marsrogue.game.entity.item.weapon.gun.GunType;

import java.util.Random;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 03:32 PM.
 */
public class ItemBuilder {
    
    public static void createGunBlueprints(int depth, int minPerLevel, int maxPerLevel, float chancePerLevel, Random random){
        Log.info("Creating gun blueprints...");
        int created = 0;
        for(int z = 0; z < depth; z++){
            if(random.nextFloat() <= chancePerLevel || z == 0){
                int count = Maths.range(minPerLevel, maxPerLevel, random);
                for(int i = 0; i < count; i++){
                    Gun gun = newGun(z, random);
                    GunBlueprint gunBlueprint = new GunBlueprint(gun.name() + " " + gun.type().name() + " Blueprint", "description", gun);
                    Codex.addBlueprint(gunBlueprint, z);
                    created++;
                }
                Log.trace("Created " + count + " gun blueprints for level " + z);
            }else{
                Log.trace("No blueprints created for level " + z);
            }
        }
        Log.debug("Created " + created + " gun blueprints.");
    }

    public static Gun newGun(int level, Random random){
        Pool<Rarity> rairtyPool = new Pool<Rarity>(random);
        for(Rarity r : Rarity.values()) rairtyPool.add(r, (int) (r.value + (level * 0.025)));

        Rarity rairity = rairtyPool.get();

        GunClass gunClass = GunClass.values.get(random.nextInt(GunClass.values.size()));
        GunType type = gunClass.allowedTypes().get(random.nextInt(gunClass.allowedTypes().size()));
        GunProjectile projectile = GunProjectile.values()[random.nextInt(GunProjectile.values().length)];

        float levelModifier = (float) Math.pow((level * 0.75) + 1, 1.025);

        float damageMultiplier = 1 + Maths.randomizer(type.damageModifier * projectile.damageModifier * levelModifier * rairity.multiplier, 0.25f, random);
        String damage = multiplyDamage(gunClass.baseDamage(), damageMultiplier);
        int accuracy = StringUtil.parseString(gunClass.baseAccuracy());
        int finalAccuracy = (int) (accuracy + (accuracy * ((rairity.multiplier - 1) / 3)));
        if(finalAccuracy > 100) finalAccuracy = 100;

        int min = type.minFireRate;
        int max = type.maxFireRate;

        if(min != max){
            do{
                min = (int) Maths.randomizer(type.minFireRate, 0.1f, random);
                max = (int) Maths.randomizer(type.maxFireRate, 0.1f, random);
            }while(min >= max && min <= 0 && max <= 0);
        }else{
            min = (int) Maths.randomizer(type.minFireRate, 0.1f, random);
            max = min;
        }

        if(min <= 0) min = 1;
        if(max <= 0) max = 1;

        String fireRate = min == max ? min + "" : min + "-" + max;
        int clipSize = (int) Maths.randomizer(type.clipSize, 0.25f, random);
        int finalClipSize = (int) (clipSize + ((clipSize * (rairity.multiplier - 1)) / 2));

        int matterCost = (int) (Gun.BASE_MATTER_COST * gunClass.matterCostModifier() * projectile.matterCostModifier * Maths.randomizer(0.9f, 1.1f, random) * Globals.GUN_MATTER_COST_MULTIPLIER);

        //		System.out.println("------ New Gun (" + level + ") -------");
        //		System.out.println("Rarity: " + rairity + " (+" + rairity.multiplier + ")");
        //		System.out.println("Class: " + gunClass.name());
        //		System.out.println("Type: " + type);
        //		System.out.println("Projectile: " + projectile);
        //		
        //		System.out.println("Damage Multiplier: " + damageMultiplier + " (" + type.damageModifier + " * " + projectile.damageModifier + " * " + levelModifier + " * " + rairity.multiplier + " +- 0.25f)");
        //		System.out.println("Damage: " + damage + "(" + gunClass.baseDamage() +" * " + damageMultiplier +")");
        //		System.out.println("Accuracy: " + finalAccuracy + "% (" + accuracy + ")");
        //		System.out.println("Fire Rate: " + fireRate);
        //		System.out.println("Clip Size: " + finalClipSize + "(" + clipSize + ")");
        //		

        String name = NameBuilder.generateGunName(random);
        Gun gun = new Gun(gunClass.glyph(), projectile.color, name, "Gun description goes here");
        gun.setStats(damage, accuracy, fireRate, finalClipSize, gunClass, projectile, type, matterCost, rairity);

        return gun;
    }

    public static Explosive newExplosive(int level, Random random){
        Explosive explosive = new Explosive((char) 7, Colors.get("grenade"), "Grenade", "Explodes.");
        return explosive;
    }

    public static Armor newArmorOfType(int level, Limb limb, Random random){
        Armor armor = new Armor('&', Colors.get("orange"), StringUtil.capitalizeAll(limb.prettyName) + " Armor " + random.nextInt(99), "Its armor", limb);

        return armor;
    }
    
    public static Armor newArmor(int level, Random random){
        Armor armor = new Armor('&', Colors.get("orange"), "Chest Armor", "Its armor", Limb.TORSO);

        return armor;
    }

    private static String multiplyDamage(String damage, float amount){
        String[] split = damage.split("-");
        int min = Math.round(Integer.parseInt(split[0]) * amount);
        int max = Math.round(Integer.parseInt(split[1]) * amount);
        return min + "-" + max;
    }
}
