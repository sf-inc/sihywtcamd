package com.github.galatynf.sihywtcamd.mixin.skeleton;

import com.github.galatynf.sihywtcamd.cardinal.MyComponents;
import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.github.galatynf.sihywtcamd.entity.EntityUtils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.SkeletonHorseTrapTriggerGoal;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.equipment.trim.ArmorTrimMaterials;
import net.minecraft.item.equipment.trim.ArmorTrimPatterns;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.LocalDifficulty;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkeletonHorseTrapTriggerGoal.class)
public abstract class SkeletonHorseTrapMixin {
    @Shadow @Final private SkeletonHorseEntity skeletonHorse;

    @Shadow @Nullable protected abstract AbstractHorseEntity getHorse(LocalDifficulty localDifficulty);

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;spawnEntity(Lnet/minecraft/entity/Entity;)Z", shift = At.Shift.AFTER), cancellable = true)
    private void replaceSkeletonsSpawn(CallbackInfo ci) {
        if (!ModConfig.get().skeletons.skeletonHorse.skeletonsVariationOnTrap) return;

        ServerWorld serverWorld = (ServerWorld) this.skeletonHorse.getWorld();
        LocalDifficulty localDifficulty = serverWorld.getLocalDifficulty(this.skeletonHorse.getBlockPos());


        // Spawn spectral skeleton
        AbstractSkeletonEntity skeletonEntity = EntityType.SKELETON.create(this.skeletonHorse.getWorld(), SpawnReason.TRIGGERED);
        if (skeletonEntity == null) return;
        this.initSkeleton(skeletonEntity, localDifficulty, this.skeletonHorse);
        MyComponents.SKELETON_COMPONENT.get(skeletonEntity).setSpectral();
        EntityUtils.trimEntityArmor(serverWorld, skeletonEntity, ArmorTrimMaterials.GOLD, ArmorTrimPatterns.RAISER);
        serverWorld.spawnEntityAndPassengers(skeletonEntity);

        AbstractHorseEntity abstractHorseEntity;
        // Spawn stray
        if ((abstractHorseEntity = this.getHorse(localDifficulty)) != null
                && (skeletonEntity = EntityType.STRAY.create(this.skeletonHorse.getWorld(), SpawnReason.TRIGGERED)) != null) {
            this.initSkeleton(skeletonEntity, localDifficulty, abstractHorseEntity);
            EntityUtils.trimEntityArmor(serverWorld, skeletonEntity, ArmorTrimMaterials.DIAMOND, ArmorTrimPatterns.SHAPER);
            serverWorld.spawnEntityAndPassengers(abstractHorseEntity);
        }
        // Spawn bogged
        if ((abstractHorseEntity = this.getHorse(localDifficulty)) != null
                && (skeletonEntity = EntityType.BOGGED.create(this.skeletonHorse.getWorld(), SpawnReason.TRIGGERED)) != null) {
            this.initSkeleton(skeletonEntity, localDifficulty, abstractHorseEntity);
            EntityUtils.trimEntityArmor(serverWorld, skeletonEntity, ArmorTrimMaterials.EMERALD, ArmorTrimPatterns.HOST);
            serverWorld.spawnEntityAndPassengers(abstractHorseEntity);
        }
        // Spawn wither skeleton
        if ((abstractHorseEntity = this.getHorse(localDifficulty)) != null
                && (skeletonEntity = EntityType.WITHER_SKELETON.create(this.skeletonHorse.getWorld(), SpawnReason.TRIGGERED)) != null) {
            this.initSkeleton(skeletonEntity, localDifficulty, abstractHorseEntity);
            skeletonEntity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
            EntityUtils.enchantEquipment(skeletonEntity, EquipmentSlot.MAINHAND, localDifficulty);
            EntityUtils.trimEntityArmor(serverWorld, skeletonEntity, ArmorTrimMaterials.NETHERITE, ArmorTrimPatterns.RIB);
            serverWorld.spawnEntityAndPassengers(abstractHorseEntity);
        }

        ci.cancel();
    }

    @Unique
    private void initSkeleton(AbstractSkeletonEntity skeletonEntity, LocalDifficulty localDifficulty, AbstractHorseEntity vehicle) {
        if (skeletonEntity == null) return;

        skeletonEntity.initialize((ServerWorld) vehicle.getWorld(), localDifficulty, SpawnReason.TRIGGERED, null);
        skeletonEntity.setPosition(vehicle.getX(), vehicle.getY(), vehicle.getZ());
        skeletonEntity.timeUntilRegen = 60;
        skeletonEntity.setPersistent();

        if (skeletonEntity.getEquippedStack(EquipmentSlot.HEAD).isEmpty()) {
            skeletonEntity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
        }
        EntityUtils.enchantEquipment(skeletonEntity, EquipmentSlot.MAINHAND, localDifficulty);
        EntityUtils.enchantEquipment(skeletonEntity, EquipmentSlot.HEAD, localDifficulty);

        vehicle.addVelocity(this.skeletonHorse.getRandom().nextTriangular(0.0, 1.1485), 0.0, this.skeletonHorse.getRandom().nextTriangular(0.0, 1.1485));
        skeletonEntity.startRiding(vehicle);
    }
}
