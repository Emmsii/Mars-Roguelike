package com.mac.marsrogue.game.entity.item.weapon.gun;

import com.mac.marsrogue.engine.util.color.Colors;

import java.awt.*;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 03:03 PM.
 */
public enum GunProjectile {

    BOLT(0.5f, 1.25f, Colors.get("bolt"), false), LASER(0.75f, 1.5f, Colors.get("laser"), false), SLUG(1f, 1f, Colors.get("slug"), false);

    public float damageModifier;
    public float matterCostModifier;
    public Color color;
    public boolean causesBleeding;

    private GunProjectile(float damageModifier, float matterCostModifier, Color color, boolean causesBleeding) {
        this.damageModifier = damageModifier;
        this.matterCostModifier = matterCostModifier;
        this.color = color;
        this.causesBleeding = causesBleeding;
    }
}
