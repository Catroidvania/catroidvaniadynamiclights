package com.catroidvania.dynamiclights;

import com.fox2code.foxloader.config.ConfigEntry;
import com.fox2code.foxloader.event.FoxLoaderEvents;
import com.fox2code.foxloader.event.world.WorldTickEvent;
import com.fox2code.foxloader.launcher.FoxLauncher;
import com.fox2code.foxloader.loader.Mod;
import net.minecraft.common.block.Block;
import net.minecraft.common.block.Blocks;

import java.io.*;

import static com.fox2code.foxloader.loader.ModLoader.getConfigFolder;

public class DynamicLights extends Mod {

    public static final DynamicLightsConfig CONFIG = new DynamicLightsConfig();
    public static File configDir;
    public static File configFile;
    public static FoxLoaderEvents.CallbackList callbacks;

    @Override
    public void onPreInit() {
        setConfigObject(CONFIG);
        configDir = getConfigFolder();
        configFile = new File(configDir, "catroidvaniadynamiclights.txt");
        CONFIG.readConfig();
        System.out.println("dynamic lights initialised");
    }

    @Override
    public void onPostInit() {
        if (FoxLauncher.isClient()) {
            FoxLoaderEvents.INSTANCE.registerEvents(DynamicLightsClient.INSTANCE);
        }
    }

    public static class DynamicLightsConfig {

        public enum LightsDistance {
            OFF,
            SHORT,
            MEDIUM,
            FAR,
            UNLIMITED
        }

        public enum UpdatesPerSecond {
            MINIMAL,
            DECREASED,
            SMOOTH,
            SMOOTHEST
        }

        @ConfigEntry(configName = "dropped item lights", handlerName = "writeConfig")
        public boolean itemLights = true;

        @ConfigEntry(configName = "entity lights", handlerName = "writeConfig")
        public boolean entityLights = true;

        @ConfigEntry(configName = "handheld lights", handlerName = "writeConfig")
        public boolean handheldLights = true;

        // hostile mobs burning during the day causes so much lag
        @ConfigEntry(configName = "on fire lights", handlerName = "writeConfig")
        public boolean onFireLights = true;

        @ConfigEntry(configName = "always lit underwater", handlerName = "writeConfig")
        public boolean alwaysLitUnderwater = false;

        /*
        @ConfigEntry(configName = "always block update", handlerName = "writeConfig")
        public boolean alwaysBlockUpdate = true;
        */

        @ConfigEntry(configName =  "max entity distance", handlerName = "writeConfig")
        public LightsDistance maxEntityDistance = LightsDistance.MEDIUM;

        @ConfigEntry(configName =  "light updates", handlerName = "writeConfig")
        public UpdatesPerSecond updateSpeed = UpdatesPerSecond.SMOOTH;

        public void writeConfig() {
            try {
                PrintWriter pwriter = new PrintWriter(new FileWriter(configFile));
                pwriter.println("itemLights:" + CONFIG.itemLights);
                pwriter.println("entityLights:" + CONFIG.entityLights);
                pwriter.println("handheldLights:" + CONFIG.handheldLights);
                pwriter.println("onFireLights:" + CONFIG.onFireLights);
                pwriter.println("alwaysLitUnderwater:" + CONFIG.alwaysLitUnderwater);
                //pwriter.println("alwaysBlockUpdate:" + CONFIG.alwaysBlockUpdate);
                pwriter.println("maxEntityDistance:" + CONFIG.maxEntityDistance);
                pwriter.println("updateSpeed:" + CONFIG.updateSpeed);
                pwriter.close();
            } catch (Exception exception) {
                System.out.println("could not write to config file!");
                exception.printStackTrace();
            }
        }

        public void readConfig() {
            try {
                if (configFile.exists()) {
                    BufferedReader breader = new BufferedReader(new FileReader(configFile));
                    String line = "";

                    while ((line = breader.readLine()) != null) {
                        try {
                            String[] strings = line.split(":");
                            if (strings[0].equals("itemLights")) {
                                CONFIG.itemLights = strings[1].equals("true");
                            }
                            if (strings[0].equals("entityLights")) {
                                CONFIG.entityLights = strings[1].equals("true");
                            }
                            if (strings[0].equals("handheldLights")) {
                                CONFIG.handheldLights = strings[1].equals("true");
                            }
                            if (strings[0].equals("onFireLights")) {
                                CONFIG.onFireLights = strings[1].equals("true");
                            }
                            if (strings[0].equals("alwaysLitUnderwater")) {
                                CONFIG.alwaysLitUnderwater = strings[1].equals("true");
                            }
                            /*
                            if (strings[0].equals("alwaysBlockUpdate")) {
                                CONFIG.alwaysBlockUpdate = strings[1].equals("true");
                            }
                            */
                            if (strings[0].equals("maxEntityDistance")) {
                                CONFIG.maxEntityDistance = LightsDistance.valueOf(strings[1]);
                            }
                            if (strings[0].equals("updateSpeed")) {
                                CONFIG.updateSpeed = UpdatesPerSecond.valueOf(strings[1]);
                            }
                        } catch (Exception exception) {
                            System.out.println("failed to parse option " + line);
                        }
                    }
                    breader.close();
                }
            } catch (Exception exception) {
                System.out.println("could not read config file!");
                exception.printStackTrace();
            }
        }

        public float getMaxDistanceValue(LightsDistance ld) {
            return switch (ld) {
                case SHORT -> 16.0f;
                case MEDIUM -> 32.0f;
                case FAR -> 64.0f;
                case UNLIMITED -> 1024.0f;
                default -> 0;
            };
        }

        public int getTicksPerUpdate(UpdatesPerSecond ups) {
            return switch (ups) {
                case MINIMAL -> 10;
                case DECREASED -> 4;
                case SMOOTH -> 2;
                case SMOOTHEST -> 1;
                default -> 1;
            };
        }
    }
}
