package com.catroidvania.dynamiclights.mixins;

import com.catroidvania.dynamiclights.DynamicLightsClient;
import net.minecraft.client.Minecraft;
import net.minecraft.common.world.EnumSkyBlock;
import net.minecraft.common.world.chunk.ChunkCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ChunkCache.class)
public class ChunkCacheMixins {

    public ChunkCache thisChunkCache = (ChunkCache)(Object)this;

    @Overwrite
    public float getBrightness(int x, int y, int z, int brightness) {
        int light = thisChunkCache.getLightValue(x, y, z);

        if (light < brightness) {
            light = brightness;
        }

        int dynamicLight = DynamicLightsClient.lightHandler.lightMap.getLight(x, y, z);
        if (light < dynamicLight) {
            light = dynamicLight;
        }

        //Minecraft.theMinecraft.theWorld.markBlockNeedsUpdate(x, y, z);
        //Minecraft.theMinecraft.theWorld.markBlockAsNeedsUpdate(x, y, z);
        return Minecraft.theMinecraft.theWorld.worldProvider.lightBrightnessTable[light];
    }

    @Overwrite
    public float getLightBrightness(int x, int y, int z) {
        int light = thisChunkCache.getLightValue(x, y, z);

        int dynamicLight = DynamicLightsClient.lightHandler.lightMap.getLight(x, y, z);
        if (light < dynamicLight) {
            light = dynamicLight;
        }

        //Minecraft.theMinecraft.theWorld.markBlockNeedsUpdate(x, y, z);
        //Minecraft.theMinecraft.theWorld.markBlockAsNeedsUpdate(x, y, z);
        return Minecraft.theMinecraft.theWorld.worldProvider.lightBrightnessTable[light];
    }
}
