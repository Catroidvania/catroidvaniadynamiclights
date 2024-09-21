package com.catroidvania.dynamiclights.client.mixins;

import com.catroidvania.dynamiclights.DynamicLightsClient;
import net.minecraft.src.game.level.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(World.class)
public class WorldMixins {

    public World thisWorld = (World)(Object)this;

    @Overwrite
    public float getBrightness(int x, int y, int z, int brightness) {
        int light = thisWorld.getBlockLightValue(x, y, z);
        int dynamicLight = DynamicLightsClient.lightHandler.lightMap.getLight(x, y, z);
        if (light < dynamicLight) {
            light = dynamicLight;
        }
        if (light < brightness) {
            light = brightness;
        }
        thisWorld.markBlockNeedsUpdate(x, y, z);
        return thisWorld.worldProvider.lightBrightnessTable[light];
    }

    @Overwrite
    public float getLightBrightness(int x, int y, int z) {
        int light = thisWorld.getBlockLightValue(x, y, z);
        int dynamicLight = DynamicLightsClient.lightHandler.lightMap.getLight(x, y, z);
        if (light < dynamicLight) {
            light = dynamicLight;
        }
        thisWorld.markBlockNeedsUpdate(x, y, z);
        return thisWorld.worldProvider.lightBrightnessTable[light];
    }
}
