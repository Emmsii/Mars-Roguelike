package com.mac.marsrogue.engine.util;

import com.esotericsoftware.minlog.Log;
import com.mac.marsrogue.engine.util.Maths.Maths;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: Mars Roguelike
 * Created by Matt on 01/05/2017 at 03:04 PM.
 */
public class StringUtil {

    public static int totalValue(String input){
        String[] split = input.split("-");
        if(split.length == 1) return Integer.parseInt(split[0]);
        int result = 0;
        result += Integer.parseInt(split[0]);
        result += Integer.parseInt(split[1]);
        return result;
    }

    public static int parseString(String input){
        if(input == null || input.length() == 0) return 0;
        input = input.trim();
        if(input.length() == 1) return Integer.parseInt(input);
        String[] split = input.split("-");
        if(split.length != 2){
            Log.warn("Invalid input string: " + input);
            return 0;
        }
        return Maths.range(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
    }

    public static String capitalize(String text){
        if(text == null || text.length() == 0) return "null";
        text = text.toLowerCase();
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    public static String capitalizeAll(String text){
        if(text == null || text.length() == 0) return "null";
        text = text.toLowerCase();
        String result = "";
        for(int i = 0; i < text.length(); i++){
            if(i == 0 || i > 1 && text.charAt(i - 1) == ' ') result += Character.toString(text.charAt(i)).toUpperCase();
            else result += text.charAt(i);
        }
        return result;
    }

    public static List<String> lineWrap(String text, int width, boolean shiftNewLines){
        String[] words = text.trim().split(" ");
        StringBuilder currentLine = new StringBuilder();
        List<String> newLines = new ArrayList<String>();

        int currentLength = 0;
        for(int i = 0; i < words.length; i++){
            currentLine.append(words[i] + " ");
            currentLength = currentLine.length();

            int nextWordLength = 0;
            if(i + 1 < words.length) nextWordLength = words[i + 1].length();
            if(currentLength + nextWordLength >= width - 2 || i + 1 >= words.length){
                newLines.add(currentLine.toString());
                currentLine = new StringBuilder();
                if(shiftNewLines) currentLine.append(" ");
            }
        }

        return newLines;
    }

    public static String makeSecondPerson(String text){
        if(text == null || text.length() == 0) return "null";
        String[] words = text.split(" ");
        words[0] = words[0] + "s";

        StringBuilder builder = new StringBuilder();
        for(String word : words){
            builder.append(" ");
            builder.append(word);
        }

        return builder.toString().trim();
    }
}
