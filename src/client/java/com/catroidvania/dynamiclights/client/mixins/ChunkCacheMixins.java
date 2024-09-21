package com.catroidvania.dynamiclights.client.mixins;

import com.catroidvania.dynamiclights.DynamicLightsClient;
import net.minecraft.client.Minecraft;
import net.minecraft.src.game.level.World;
import net.minecraft.src.game.level.chunk.ChunkCache;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ChunkCache.class)
public class ChunkCacheMixins {

    public ChunkCache thisChunkCache = (ChunkCache)(Object)this;

    @Overwrite
    public float getBrightness(int x, int y, int z, int brightness) {
        int light = thisChunkCache.getLightValue(x, y, z);
        int dynamicLight = DynamicLightsClient.lightHandler.lightMap.getLight(x, y, z);
        if (light < dynamicLight) {
            light = dynamicLight;
        }
        if (light < brightness) {
            light = brightness;
        }
        Minecraft.theMinecraft.theWorld.markBlockNeedsUpdate(x, y, z);
        return Minecraft.theMinecraft.theWorld.worldProvider.lightBrightnessTable[light];
    }

    @Overwrite
    public float getLightBrightness(int x, int y, int z) {
        int light = thisChunkCache.getLightValue(x, y, z);
        int dynamicLight = DynamicLightsClient.lightHandler.lightMap.getLight(x, y, z);
        if (light < dynamicLight) {
            light = dynamicLight;
        }
        Minecraft.theMinecraft.theWorld.markBlockNeedsUpdate(x, y, z);
        return Minecraft.theMinecraft.theWorld.worldProvider.lightBrightnessTable[light];
    }
}
