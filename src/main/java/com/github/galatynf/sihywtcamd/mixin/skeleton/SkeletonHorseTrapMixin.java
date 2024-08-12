package com.github.galatynf.sihywtcamd.mixin.skeleton;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.github.galatynf.sihywtcamd.imixin.SpectralSkeletonIMixin;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.provider.EnchantmentProviders;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.SkeletonHorseTrapTriggerGoal;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
        AbstractSkeletonEntity skeletonEntity = EntityType.SKELETON.create(this.skeletonHorse.getWorld());
        if (skeletonEntity == null) return;
        this.initSkeleton(skeletonEntity, localDifficulty, this.skeletonHorse);
        ((SpectralSkeletonIMixin) skeletonEntity).sihywtcamd$setSpectral();
        serverWorld.spawnEntityAndPassengers(skeletonEntity);

        AbstractHorseEntity abstractHorseEntity;
        // Spawn stray
        if ((abstractHorseEntity = this.getHorse(localDifficulty)) != null
                && (skeletonEntity = EntityType.STRAY.create(this.skeletonHorse.getWorld())) != null) {
            this.initSkeleton(skeletonEntity, localDifficulty, abstractHorseEntity);
            serverWorld.spawnEntityAndPassengers(abstractHorseEntity);
        }
        // Spawn bogged
        if ((abstractHorseEntity = this.getHorse(localDifficulty)) != null
                && (skeletonEntity = EntityType.BOGGED.create(this.skeletonHorse.getWorld())) != null) {
            this.initSkeleton(skeletonEntity, localDifficulty, abstractHorseEntity);
            serverWorld.spawnEntityAndPassengers(abstractHorseEntity);
        }
        // Spawn wither skeleton
        if ((abstractHorseEntity = this.getHorse(localDifficulty)) != null
                && (skeletonEntity = EntityType.WITHER_SKELETON.create(this.skeletonHorse.getWorld())) != null) {
            this.initSkeleton(skeletonEntity, localDifficulty, abstractHorseEntity);
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
        this.enchantEquipment(skeletonEntity, EquipmentSlot.MAINHAND, localDifficulty);
        this.enchantEquipment(skeletonEntity, EquipmentSlot.HEAD, localDifficulty);

        vehicle.addVelocity(this.skeletonHorse.getRandom().nextTriangular(0.0, 1.1485), 0.0, this.skeletonHorse.getRandom().nextTriangular(0.0, 1.1485));
        skeletonEntity.startRiding(vehicle);
    }

    @Unique
    private void enchantEquipment(AbstractSkeletonEntity rider, EquipmentSlot slot, LocalDifficulty localDifficulty) {
        ItemStack itemStack = rider.getEquippedStack(slot);
        itemStack.set(DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT);
        EnchantmentHelper.applyEnchantmentProvider(itemStack, rider.getWorld().getRegistryManager(),
                EnchantmentProviders.MOB_SPAWN_EQUIPMENT, localDifficulty, rider.getRandom());
        rider.equipStack(slot, itemStack);
    }
}
