package com.mac.marsrogue.game.entity.item.weapon.gun;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 03:03 PM.
 */
public enum GunType {

    SINGLE(1, 1, 1, 9), SPREAD(0.55f, 15, 20, 6), BURST(0.9f, 3, 3, 12), AUTOMATIC(0.75f, 8, 14, 12);

    public static final GunType[] ALL = { SINGLE, SPREAD, BURST, GunType.AUTOMATIC };

    public float damageModifier;
    public int minFireRate;
    public int maxFireRate;
    public int clipSize;

    private GunType(float damageModifier, int minFireRate, int maxFireRate, int clipSize){
        this.damageModifier = damageModifier;
        this.minFireRate = minFireRate;
        this.maxFireRate = maxFireRate;
        this.clipSize = clipSize;
    }
}
