package com.github.galatynf.sihywtcamd.mixin.zombie;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.mob.ElderGuardianEntity;
import net.minecraft.entity.mob.GuardianEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(DrownedEntity.class)
public abstract class DrownedMixin extends ZombieEntity {
    @Unique
    private static final int TRIDENT_CHANCE = 15;
    @Unique
    private static final float VANILLA_TRIDENT_CHANCE = 0.1F * 10 / 16;

    public DrownedMixin(EntityType<? extends ZombieEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initEquipment", at = @At("HEAD"), cancellable = true)
    public void changeProbability(Random random, LocalDifficulty localDifficulty, CallbackInfo ci) {
        if (ModConfig.get().zombies.drowned.tridentSpawn) {
            int rand = random.nextInt(100);
            if (rand < TRIDENT_CHANCE) {
                this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.TRIDENT));
            } else if (rand < 18) {
                this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.FISHING_ROD));
            }
            ci.cancel();
        }
    }

    @ModifyExpressionValue(method = "initialize", at = @At(value = "CONSTANT", args = "floatValue=0.5"))
    private float keepVanillaNautilusJockeyChance(float original) {
        if (ModConfig.get().zombies.drowned.tridentSpawn) {
            return original * VANILLA_TRIDENT_CHANCE / (TRIDENT_CHANCE / 100.0F);
        }
        return original;
    }

    @Inject(method = "initialize", at = @At("TAIL"))
    private void trySpawnAsGuardianJockey(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason,
                                          EntityData entityData, CallbackInfoReturnable<EntityData> cir) {
        if (ModConfig.get().zombies.drowned.guardianJockeySpawn && this.isBaby() && !this.hasVehicle()
                && world.getRandom().nextFloat() < 0.1F + 0.1F * difficulty.getClampedLocalDifficulty()) {
            List<GuardianEntity> list = world.getEntitiesByClass(GuardianEntity.class, this.getBoundingBox().expand(5.0D, 3.0D, 5.0D),
                    guardian -> EntityPredicates.NOT_MOUNTED.test(guardian) && !(guardian instanceof ElderGuardianEntity));
            if (!list.isEmpty()) {
                GuardianEntity guardianEntity = list.getFirst();
                this.startRiding(guardianEntity);
            } else {
                GuardianEntity guardianEntity = EntityType.GUARDIAN.create(this.getEntityWorld(), SpawnReason.JOCKEY);
                if (guardianEntity != null) {
                    guardianEntity.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.getYaw(), 0.0F);
                    guardianEntity.initialize(world, difficulty, SpawnReason.JOCKEY, null);
                    this.startRiding(guardianEntity);
                    world.spawnEntity(guardianEntity);
                }
            }
        }
    }

    @ModifyArg(method = "travelInWater", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/DrownedEntity;updateVelocity(FLnet/minecraft/util/math/Vec3d;)V"))
    private float increaseVelocity(float speed) {
        return ModConfig.get().zombies.drowned.highVelocity ? 0.1F : speed;
    }
}
