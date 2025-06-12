package com.catroidvania.dynamiclights.mixins;

import com.catroidvania.dynamiclights.DynamicLightsClient;
import net.minecraft.client.Minecraft;
import net.minecraft.common.world.EnumSkyBlock;
import net.minecraft.common.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(World.class)
public class WorldMixins {

    public World thisWorld = (World)(Object)this;

    @Overwrite
    public float getBrightness(int x, int y, int z, int brightness) {
        int light = thisWorld.getBlockLightValue(x, y, z);

        if (light < brightness) {
            light = brightness;
        }

        int dynamicLight = DynamicLightsClient.lightHandler.lightMap.getLight(x, y, z);
        if (light < dynamicLight) {
            light = dynamicLight;
        }

        //thisWorld.markBlockNeedsUpdate(x, y, z);
        //thisWorld.markBlockAsNeedsUpdate(x, y, z);
        return thisWorld.worldProvider.lightBrightnessTable[light];
    }

    @Overwrite
    public float getLightBrightness(int x, int y, int z) {
        int light = thisWorld.getBlockLightValue(x, y, z);

        int dynamicLight = DynamicLightsClient.lightHandler.lightMap.getLight(x, y, z);
        if (light < dynamicLight) {
            light = dynamicLight;
        }

        //thisWorld.markBlockNeedsUpdate(x, y, z);
        //thisWorld.markBlockAsNeedsUpdate(x, y, z);
        return thisWorld.worldProvider.lightBrightnessTable[light];
    }

    @Overwrite
    public int getFullBlockLightValue(int x, int y, int z) {
        int light = thisWorld.getChunkFromChunkCoords(x >> 4, y >> 4, z >> 4).getBlockLightValue(x & 15, y & 15, z & 15, 0);
        int dynamicLight = DynamicLightsClient.lightHandler.lightMap.getLight(x, y, z);
        if (light < dynamicLight) {
            light = dynamicLight;
        }

        return light;
    }

    public int getBlockLightValue(int x, int y, int z) {
        int light = thisWorld.getBlockLightValue_do(x, y, z, true);
        int dynamicLight = DynamicLightsClient.lightHandler.lightMap.getLight(x, y, z);
        if (light < dynamicLight) {
            light = dynamicLight;
        }

        return light;
    }
}
