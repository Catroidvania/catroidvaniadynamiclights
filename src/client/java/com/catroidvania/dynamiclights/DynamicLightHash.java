package com.catroidvania.dynamiclights;

import java.util.HashMap;

public class DynamicLightHash {

    public HashMap<CoordHashKey, Integer> dynamicLightMap;

    public DynamicLightHash() {
        this.dynamicLightMap = new HashMap<>(1024);
    }

    public void clearLightMap() {
        this.dynamicLightMap.clear();
    }

    public void setLightWithPropagate(int x, int y, int z, int level) {
        int currentLevel = this.getLight(x, y, z);
        if (level > currentLevel) {
            this.dynamicLightMap.put(new CoordHashKey(x, y, z), level);
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
