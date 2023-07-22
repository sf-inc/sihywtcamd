package com.github.galatynf.sihywtcamd.mixin.zombie;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.mob.GuardianEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(DrownedEntity.class)
public abstract class DrownedMixin extends ZombieEntity {
    public DrownedMixin(EntityType<? extends ZombieEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initEquipment", at = @At("HEAD"), cancellable = true)
    public void changeProbability(Random random, LocalDifficulty localDifficulty, CallbackInfo ci) {
        if (ModConfig.get().zombie.drowned.tridentSpawn) {
            int rand = random.nextInt(100);
            if (rand < 15) {
                this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.TRIDENT));
            } else if (rand < 18) {
                this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.FISHING_ROD));
            }
            ci.cancel();
        }
    }

    @Inject(method = "initialize", at = @At("TAIL"))
    private void trySpawnAsGuardianJockey(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, NbtCompound entityNbt, CallbackInfoReturnable<EntityData> cir) {
        if (ModConfig.get().zombie.drowned.guardianJockeySpawn && this.isBaby()
                && world.getRandom().nextFloat() < 0.1F + 0.1F * world.getLocalDifficulty(this.getBlockPos()).getClampedLocalDifficulty()) {
            List<GuardianEntity> list = world.getEntitiesByClass(GuardianEntity.class, this.getBoundingBox().expand(5.0D, 3.0D, 5.0D), EntityPredicates.NOT_MOUNTED);
            if (!list.isEmpty()) {
                GuardianEntity guardianEntity = list.get(0);
                this.startRiding(guardianEntity);
            } else {
                GuardianEntity guardianEntity = EntityType.GUARDIAN.create(this.getWorld());
                if (guardianEntity != null) {
                    guardianEntity.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.getYaw(), 0.0F);
                    guardianEntity.initialize(world, difficulty, SpawnReason.JOCKEY, null, null);
                    this.startRiding(guardianEntity);
                    world.spawnEntity(guardianEntity);
                }
            }
        }
    }

    @Override
    public void tickRiding() {
        super.tickRiding();
        if (this.getVehicle() instanceof PathAwareEntity pathAwareEntity) {
            this.bodyYaw = pathAwareEntity.bodyYaw;
        }
    }

    @ModifyArg(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/DrownedEntity;updateVelocity(FLnet/minecraft/util/math/Vec3d;)V"))
    private float increaseVelocity(float speed) {
        return ModConfig.get().zombie.drowned.highVelocity ? 0.1F : speed;
    }
}
