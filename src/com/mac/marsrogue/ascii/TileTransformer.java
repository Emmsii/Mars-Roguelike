package com.mac.marsrogue.ascii;

/**
 * Created by Matt on 29/04/2017.
 */
public interface TileTransformer {
    
    void transformTile(int x, int y, AsciiCharacterData data);
}
