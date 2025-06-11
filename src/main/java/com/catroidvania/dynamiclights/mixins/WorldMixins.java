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
}
