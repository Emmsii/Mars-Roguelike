package com.mac.marsrogue.game.builders;

import com.mac.marsrogue.engine.util.Maths.Maths;

import java.util.Random;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 03:51 PM.
 */
public class NameBuilder {

    public static String generateGunName(Random random){
        String startingLetters = "AKMNPU";
        String result = "";
        int length = Maths.range(3, 5, random);
        boolean hasDash = false;
        int numberCount = 0;

        result += startingLetters.charAt(random.nextInt(startingLetters.length()));
        for(int i = 0; i < length; i++){
            if(random.nextFloat() < 0.1f && i > 3 && result.charAt(result.length() - 1) != '-') break;
            if(i < (length / 2) + 1 && random.nextFloat() < 0.35f && !hasDash){
                hasDash = true;
                result += "-";
            }else{
                if(random.nextFloat() < 0.2f){
                    result += (char) (random.nextInt(26) + 'a');
                    numberCount = 0;
                }else{
                    result += random.nextInt(10);
                    numberCount++;
                }
            }
            if(numberCount >= 3) break;
            if(!hasDash && i >= length * 0.75) break;
        }

        return result.toUpperCase();
    }
}
