package com.catroidvania.dynamiclights.client.mixins;

import net.minecraft.src.game.entity.monster.EntityCreeper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityCreeper.class)
public interface EntityCreeperMixins {
    @Accessor
    int getTimeSinceIgnited();
}
