package com.catroidvania.dynamiclights;

import com.catroidvania.dynamiclights.client.mixins.EntityCreeperMixins;
import net.minecraft.client.Minecraft;
import net.minecraft.src.game.block.Block;
import net.minecraft.src.game.entity.Entity;
import net.minecraft.src.game.entity.monster.EntityBlaze;
import net.minecraft.src.game.entity.monster.EntityCreeper;
import net.minecraft.src.game.entity.other.*;
import net.minecraft.src.game.entity.player.EntityPlayer;
import net.minecraft.src.game.item.Item;
import net.minecraft.src.game.item.ItemStack;
import net.minecraft.src.game.level.World;

import java.util.List;

public class DynamicLightsUpdater {

    public DynamicLightHash lightMap = new DynamicLightHash();
    public static final Minecraft mc = Minecraft.theMinecraft;
    public static boolean needsReset = false;

    public void updateDynamicLights() {
        this.lightMap.clearLightMap();
        this.updateHeldItemLight();
        this.updateEntityLight();
    }

    public void updateHeldItemLight() {
        if (!DynamicLights.CONFIG.handheldLights) {
            return;
        }
        EntityPlayer player = mc.thePlayer;
        if (player != null) {
            ItemStack heldItem = player.inventory.getCurrentItem();
            this.lightMap.setLightWithPropagate(blockPos(player.posX), blockPos(player.posY), blockPos(player.posZ), getItemLight(heldItem));
        }
    }

    public void updateEntityLight() {
        if (!DynamicLights.CONFIG.entityLights && !DynamicLights.CONFIG.itemLights) {
            return;
        }
        World world = mc.theWorld;
        if (world == null) {
            return;
        }
        List<Entity> entities = world.getLoadedEntityList();
        for (Entity entity: entities) {
            if (entity != null) {
                this.lightMap.setLightWithPropagate(blockPos(entity.posX), blockPos(entity.posY), blockPos(entity.posZ), getEntityLight(entity));
            }
        }
    }

    public int getEntityLight(Entity entity) {
        if (entity == null) {
            return 0;
        }
        if (DynamicLights.CONFIG.onFireLights) {
            if (entity.fire > 0) {
                return 15;
            }
        }
        if (DynamicLights.CONFIG.itemLights) {
            if (entity instanceof EntityItem) {
                ItemStack entityItem = ((EntityItem)entity).item;
                return getItemLight(entityItem);
            }
        }
        if (DynamicLights.CONFIG.entityLights) {
            if (entity instanceof EntityCreeper) {
                int fuse_time = ((EntityCreeperMixins) entity).getTimeSinceIgnited();
                return fuse_time / 2;
            } else if (entity instanceof EntityDynamite) {
                return 10;
            } else if (entity instanceof EntityTorch) {
                return 14;
            } else if (entity instanceof EntityWyvernFireball) {
                return 14;
            } else if (entity instanceof EntitySmallFireball) {
                return 14;
            } else if (entity instanceof EntityFireball) {
                return 14;
            } else if (entity instanceof EntityLightningBolt) {
                return 15;
            } else if (entity instanceof EntityMolotov) {
                return 7;
            } else if (entity instanceof EntityTNTPrimed) {
                return 15;
            } else if (entity instanceof EntityBlaze) {
                return 15;
            }
        }
        return 0;
    }

    public int getItemLight(ItemStack item) {
        if (item == null) {
            return 0;
        }
        int id = item.itemID;

        if (id == Item.flintAndSteel.itemID) {
            return 0;
        }

        // this is terrible
        if (id == Block.torch.blockID ||
                id == Item.stickyTorch.itemID ||
                id == Item.swordFire.itemID ||
                id == Item.shovelFire.itemID ||
                id == Item.pickaxeFire.itemID ||
                id == Item.axeFire.itemID ||
                id == Item.hoeFire.itemID) {
            return 14;
        } else if (id == Item.bucketLava.itemID ||
                id == Item.goldenBucketLava.itemID ||
                id == Block.glowstone.blockID ||
                id == Block.pumpkinLantern.blockID ||
                id == Block.hiveLight.blockID ||
                id == Block.frozestone.blockID ||
                id == Item.bottledFlame.itemID) {
            return 15;
        } else if (id == Block.torchRedstoneActive.blockID ||
                id == Block.mushroomGlowing.blockID ||
                id == Item.glowstoneDust.itemID ||
                id == Item.fireCharge.itemID ||
                id == Item.lightningCharge.itemID ||
                id == Item.molotov.itemID) {
            return 7;
        } else if (id == Block.magma.blockID ||
                id == Block.magmaBrick.blockID ||
                id == Block.magmaPillar.blockID ||
                id == Block.mushroomCapGlowing.blockID ||
                id == Block.kottamagma.blockID ||
                id == Block.kottamagmaPillar.blockID ||
                id == Block.kottamagmaBrick.blockID ||
                id == Item.blazeSpawnEgg.itemID) {
            return 13;
        } else if (id == Block.mushroomBrown.blockID) {
            return 1;
        } else if (id == Block.coralBlue.blockID ||
                id == Block.coralRed.blockID ||
                id == Block.coralYellow.blockID ||
                id == Block.coralDead.blockID) {
            return 11;
        }

        return 0;
    }

    public int blockPos(double pos) {
        return (int)Math.floor(pos);
    }
}
