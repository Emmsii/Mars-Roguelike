package com.mac.marsrogue.engine.util.Maths;

import com.mac.marsrogue.game.Game;

import java.util.Random;

/**
 * Project: Mars Roguelike
 * Created by Matt on 29/04/2017 at 05:16 PM.
 */
public class Maths {

    public static float clamp(float value){
        return Math.max(0, Math.min(value, 1));
    }

    public static int percentage(int value, int max){
        return (int) ((float) value / (float) max) * 100;
    }

    public static int percentage(float value, float max){
        return (int) (value / max) * 100;
    }

    public static float randomizer(float value, float amount, Random rand){
        return value * (((rand.nextFloat() * 2 - 1) * amount) + 1);
    }

    public static float rangeTriangular(float lo, float hi, Random rand){
        float halfDiff = 0.5f * (hi - lo);
        float triDist = Game.random.nextFloat() + Game.random.nextFloat();
        return halfDiff * triDist + lo;
    }

    public static float range(float min, float max, Random rand){
        return rand.nextFloat() * (max - min) + min;
    }

    public static int range(int min, int max, Random rand){
        return rand.nextInt((max - min) + 1) + min;
    }

    public static int range(int min, int max){
        return Game.random.nextInt((max - min) + 1) + min;
    }

    public static int distance(int xa, int ya, int xb, int yb){
        return (int) Math.sqrt((xa - xb) * (xa - xb) + (ya - yb) * (ya - yb));
    }

    public static boolean isInteger(String in){
        try{
            Integer.parseInt(in);
        }catch(NumberFormatException | NullPointerException e){
            return false;
        }
        return true;
    }
}
