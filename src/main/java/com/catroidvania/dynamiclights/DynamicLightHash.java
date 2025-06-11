package com.catroidvania.dynamiclights;

import net.minecraft.client.Minecraft;
import net.minecraft.common.world.chunk.Chunk;

import java.util.HashMap;

public class DynamicLightHash {

    public HashMap<CoordHashKey, Integer> dynamicLightMap;

    public DynamicLightHash() {
        this.dynamicLightMap = new HashMap<>(512);
    }

    public void clearLightMap() {
        this.dynamicLightMap.clear();
    }

    public void setLightWithUpdate(int x, int y, int z, int level) {
        setLightWithPropagate(x, y, z, level);
        Chunk chunk = Minecraft.theMinecraft.theWorld.getChunkFromBlockCoordsSafe(x, y, z);
        chunk.setLightValue(x & 15, y & 15, z & 15, level);
        Minecraft.theMinecraft.theWorld.scheduleLightingUpdate(
                x - level,
                y - level,
                z - level,
                x + level,
                y + level,
                z + level,
                0
        );
    }

    public void setLightWithPropagate(int x, int y, int z, int level) {
        int currentLevel = this.getLight(x, y, z);
        if (level > currentLevel) {
            this.dynamicLightMap.put(new CoordHashKey(x, y, z), level);
            //Minecraft.theMinecraft.theWorld.markBlockAsNeedsUpdate(x, y, z);
            level -= 1;
            this.setLightWithPropagate(x + 1, y, z, level);
            this.setLightWithPropagate(x - 1, y, z, level);
            this.setLightWithPropagate(x, y + 1, z, level);
            this.setLightWithPropagate(x, y - 1, z, level);
            this.setLightWithPropagate(x, y, z + 1, level);
            this.setLightWithPropagate(x, y, z - 1, level);
        }
    }

    public void setLight(int x, int y,  int z, int level) {
        this.dynamicLightMap.put(new CoordHashKey(x, y, z), level);
    }

    public int getLight(int x, int y, int z) {
        Integer lightLevel = this.dynamicLightMap.get(new CoordHashKey(x, y, z));
        return lightLevel == null ? 0 : lightLevel;
    }

}
