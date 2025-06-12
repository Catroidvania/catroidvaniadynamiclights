package com.catroidvania.dynamiclights;

import com.fox2code.foxevents.EventHandler;
import com.fox2code.foxloader.event.GlobalTickEvent;
import com.fox2code.foxloader.event.client.CameraAndRenderUpdatedEvent;
import com.fox2code.foxloader.event.world.WorldTickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.common.world.World;

public class DynamicLightsClient extends DynamicLights {

    public static final DynamicLightsClient INSTANCE = new DynamicLightsClient();
    public static final DynamicLightsUpdater lightHandler = new DynamicLightsUpdater();

    private DynamicLightsClient() {}

    @EventHandler
    public void onTick(GlobalTickEvent event) {
        lightHandler.updateDynamicLights();
    }

    @EventHandler
    public void onWorldTick(WorldTickEvent event) {
        //lightHandler.updateDynamicLights();
    }

    @EventHandler
    public void onCameraAndRenderUpdatedEvent(CameraAndRenderUpdatedEvent event) {
        //lightHandler.updateDynamicLights();
    }
}
