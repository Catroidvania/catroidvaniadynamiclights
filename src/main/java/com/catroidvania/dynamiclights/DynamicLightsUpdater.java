package com.catroidvania.dynamiclights;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.EntityOtherPlayerMP;
import net.minecraft.common.block.Blocks;
import net.minecraft.common.entity.Entity;
import net.minecraft.common.entity.animals.EntityCucurboo;
import net.minecraft.common.entity.animals.EntityGlowingMooshroom;
import net.minecraft.common.entity.monsters.EntityBlaze;
import net.minecraft.common.entity.monsters.EntityCreeper;
import net.minecraft.common.entity.other.*;
import net.minecraft.common.entity.player.EntityPlayer;
import net.minecraft.common.entity.projectile.*;
import net.minecraft.common.item.Items;
import net.minecraft.common.item.block.ItemBlock;
import net.minecraft.common.item.ItemStack;
import net.minecraft.common.item.data.EmissiveItem;
import net.minecraft.common.world.World;

import java.util.List;

public class DynamicLightsUpdater {

    public DynamicLightHash lightMap = new DynamicLightHash();
    public static final Minecraft mc = Minecraft.theMinecraft;
    public static EntityPlayer player = Minecraft.theMinecraft.thePlayer;
    public static World world = Minecraft.theMinecraft.theWorld;
    public long tick = 0;

    public void updateDynamicLights() {
        player = mc.thePlayer;
        world = mc.theWorld;
        this.tick++;
        if (needsUpdate()) {
            this.lightMap.clearLightMap();
            this.updateHeldItemLight();
            if (DynamicLights.CONFIG.maxEntityDistance != DynamicLights.DynamicLightsConfig.LightsDistance.OFF) {
                this.updateEntityLight();
            }
        }
    }

    public boolean needsUpdate() {
        return this.tick % DynamicLights.CONFIG.getTicksPerUpdate(DynamicLights.CONFIG.updateSpeed) == 0;
    }

    public void updateHeldItemLight() {
        if (!DynamicLights.CONFIG.handheldLights) {
            return;
        }
        if (player != null) {
            ItemStack heldItem = player.inventory.getCurrentItem();

            if (!DynamicLights.CONFIG.alwaysLitUnderwater && player.isInWater() && !isLitUnderwater(heldItem)) {
                return;
            }

            this.lightMap.setLightWithPropagate(blockPos(player.posX), blockPos(player.posY), blockPos(player.posZ), getItemLight(heldItem));
        }
    }

    public void updateEntityLight() {
        if (!DynamicLights.CONFIG.entityLights && !DynamicLights.CONFIG.itemLights) {
            return;
        }
        if (world == null || player == null) {
            return;
        }
        List<Entity> entities = world.getLoadedEntityList();
        for (Entity entity: entities) {
            if (entity != null) {
                float entityDist = player.getDistanceToEntity(entity);
                if (entityDist > DynamicLights.CONFIG.getMaxDistanceValue(DynamicLights.CONFIG.maxEntityDistance)) {
                    continue;
                }
                this.lightMap.setLightWithPropagate(blockPos(entity.posX), blockPos(entity.posY), blockPos(entity.posZ), getEntityLight(entity));
            }
        }
    }

    public static int getEntityLight(Entity entity) {
        if (entity == null) {
            return 0;
        }

        if (DynamicLights.CONFIG.onFireLights) {
            if (entity.isBurning()) {
                return 15;
            }
        }
        if (DynamicLights.CONFIG.itemLights) {
            if (entity instanceof EntityItem) {
                ItemStack entityItem = ((EntityItem)entity).item;
                if (!DynamicLights.CONFIG.alwaysLitUnderwater && entity.isInWater() && !isLitUnderwater(entityItem)) {
                    return 0;
                }
                return getItemLight(entityItem);
            }
        }
        if (DynamicLights.CONFIG.entityLights) {
            if (entity instanceof EntityOtherPlayerMP) {
                ItemStack entityItem = ((EntityOtherPlayerMP)entity).inventory.getCurrentItem();
                if (!DynamicLights.CONFIG.alwaysLitUnderwater && entity.isInWater() && !isLitUnderwater(entityItem)) {
                    return 0;
                }
                return getItemLight(entityItem);
            } else if (entity instanceof EntityCreeper) {
                //int igniteTime = ((EntityCreeperAccessor) entity).getTimeSinceIgnited();
                int igniteTime = ((EntityCreeper) entity).getFuseTime();
                return igniteTime / 2;
            } else if (entity instanceof EntityThrownDynamite) {
                if (!DynamicLights.CONFIG.alwaysLitUnderwater && entity.isInWater()) {
                    return 0;
                }
                return 10;
            } else if (entity instanceof EntityThrownStickyTorch) {
                if (!DynamicLights.CONFIG.alwaysLitUnderwater && entity.isInWater()) {
                    return 0;
                }
                return 14;
            } else if (entity instanceof EntityFireballWyvern) {
                return 14;
            } else if (entity instanceof EntityFireballSmall) {
                return 14;
            } else if (entity instanceof EntityFireball) {
                return 14;
            } else if (entity instanceof EntityLightningBolt) {
                return 15;
            } else if (entity instanceof EntityThrownFlamingPotion) {
                if (!DynamicLights.CONFIG.alwaysLitUnderwater && entity.isInWater()) {
                    return 0;
                }
                return 10;
            } else if (entity instanceof EntityTNT) {
                return 15;
            } else if (entity instanceof EntityBlaze) {
                return 15;
            } else if (entity instanceof EntityCucurboo) {
                return 12;
            } else if (entity instanceof EntityGlowingMooshroom) {
                return 12;
            }
        }
        return 0;
    }

    public static int getItemLight(ItemStack item) {
        if (item == null) {
            return 0;
        }
        int id = item.getItemID();

        if (item.getItem().isItemBlock()) {
            ItemBlock itemBlock = (ItemBlock)item.getItem();
            if (itemBlock == null) {
                return 0;
            }
            id = itemBlock.blockID;
            int blockLight = Blocks.BLOCKS_LIST[id].getLightValueInt();
            if (blockLight > 0) {
                return blockLight;
            }

        /* // i think i will ignore emissives for now
            if (id == Blocks.TORCH.blockID) {
                return 14;
            } else if (id == Blocks.GLOWSTONE.blockID ||
                        id == Blocks.JACK_O_LANTERN.blockID ||
                        id == Blocks.HIVELIGHT.blockID ||
                        id == Blocks.FROZESTONE.blockID) {
                return  15;
            } else if (id == Blocks.GLOWING_MUSHROOM.blockID) {
                return 7;
            } else if (id == Blocks.MAGMA.blockID ||
                        id == Blocks.MAGMA_BRICK.blockID ||
                        id == Blocks.MAGMA_PILLAR.blockID ||
                        id == Blocks.GLOWING_MUSHROOM_CAP.blockID ||
                        id == Blocks.KOTTAMAGMA.blockID ||
                        id == Blocks.KOTTAMAGMA_BRICK.blockID ||
                        id == Blocks.KOTTAMAGMA_PILLAR.blockID) {
                return 13;
            } else if (id == Blocks.BLUE_CORAL.blockID ||
                        id == Blocks.RED_CORAL.blockID ||
                        id == Blocks.YELLOW_CORAL.blockID ||
                        id == Blocks.DEAD_CORAL.blockID) {
                return 11;
            } else if (id == Blocks.BROWN_MUSHROOM.blockID) {
                return 1;
            }
        */
        } else {
            if (id == Items.STICKY_TORCH.itemID) {
                return 14;
            } else if (id == Items.LAVA_BUCKET.itemID ||
                    id == Items.GOLDEN_LAVA_BUCKET.itemID ||
                    id == Items.ACID_BUCKET.itemID ||
                    id == Items.GOLDEN_ACID_BUCKET.itemID) {
                return 15;
            } else if (id == Items.MOLTEN_SWORD.itemID ||
                    id == Items.MOLTEN_SHOVEL.itemID ||
                    id == Items.MOLTEN_PICKAXE.itemID ||
                    id == Items.MOLTEN_AXE.itemID ||
                    id == Items.MOLTEN_HOE.itemID) {
                return 13;
            } else if (id == Items.BLAZE_SPAWN_EGG.itemID ||
                    id == Items.BOTTLED_FLAME.itemID) {
                return 12;
            } else if (id == Items.GLOWSTONE_DUST.itemID ||
                    id == Items.FIRE_CHARGE.itemID ||
                    id == Items.LIGHTNING_CHARGE.itemID ||
                    id == Items.FLAMING_POTION.itemID ||
                    id == Items.CUCURBOO_SPAWN_EGG.itemID ||
                    id == Items.GLOWING_MOOSHROOM_SPAWN_EGG.itemID) {
                return 9;
            }
        }

        if (item.getItem() instanceof EmissiveItem) {
            return 6;
        }

        return 0;
    }

    public static boolean isLitUnderwater(ItemStack item) {
        if (item == null) {
            return false;
        }
        int id = item.getItemID();

        if (item.getItem().isItemBlock()) {
            ItemBlock itemBlock = (ItemBlock)item.getItem();
            if (itemBlock == null) {
                return false;
            }
            id = itemBlock.blockID;

            return id != Blocks.TORCH.blockID &&
                    id != Blocks.JACK_O_LANTERN.blockID &&
                    id != Blocks.JACK_O_MELON.blockID   // oh god theres variants
                    /* vvv made of rocks :/ &&
                    id != Blocks.CITRINE_TORCH.blockID &&
                    id != Blocks.JET_TORCH.blockID &&
                    id != Blocks.QUARTZ_TORCH.blockID &&
                    id != Blocks.MYTHRIL_TORCH.blockID*/;
        } else {
            return id != Items.STICKY_TORCH.itemID &&
                    id != Items.FLAMING_POTION.itemID &&
                    id != Items.LAVA_BUCKET.itemID &&
                    id != Items.GOLDEN_LAVA_BUCKET.itemID &&
                    id != Items.FIRE_CHARGE.itemID;
        }
    }

    public static int blockPos(double pos) {
        return (int)Math.floor(pos);
    }
}
