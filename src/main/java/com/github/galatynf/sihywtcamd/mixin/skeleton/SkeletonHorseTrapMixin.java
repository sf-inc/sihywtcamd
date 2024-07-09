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
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.SkeletonHorseEntity;
import net.minecraft.entity.mob.StrayEntity;
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
    @Shadow @Nullable protected abstract SkeletonEntity getSkeleton(LocalDifficulty localDifficulty, AbstractHorseEntity vehicle);

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;spawnEntityAndPassengers(Lnet/minecraft/entity/Entity;)V", shift = At.Shift.AFTER), cancellable = true)
    private void summonOtherSkeletons(CallbackInfo ci) {
        if (!ModConfig.get().skeletons.skeletonHorse.skeletonsVariationOnTrap) return;

        ServerWorld serverWorld = (ServerWorld) this.skeletonHorse.getWorld();
        LocalDifficulty localDifficulty = serverWorld.getLocalDifficulty(this.skeletonHorse.getBlockPos());

        AbstractSkeletonEntity skeletonEntity;
        AbstractHorseEntity abstractHorseEntity;
        // Add stray
        if ((abstractHorseEntity = this.getHorse(localDifficulty)) != null
                && (skeletonEntity = this.getStray(localDifficulty, abstractHorseEntity)) != null) {
            skeletonEntity.setBaby(false);
            skeletonEntity.startRiding(abstractHorseEntity);
            abstractHorseEntity.addVelocity(this.skeletonHorse.getRandom().nextTriangular(0.0, 1.1485), 0.0, this.skeletonHorse.getRandom().nextTriangular(0.0, 1.1485));
            serverWorld.spawnEntityAndPassengers(abstractHorseEntity);
        }
        // Add spectral
        if ((abstractHorseEntity = this.getHorse(localDifficulty)) != null
                && (skeletonEntity = this.getSkeleton(localDifficulty, abstractHorseEntity)) != null) {
            ((SpectralSkeletonIMixin) skeletonEntity).sihywtcamd$setSpectral();
            skeletonEntity.setBaby(false);
            skeletonEntity.startRiding(abstractHorseEntity);
            abstractHorseEntity.addVelocity(this.skeletonHorse.getRandom().nextTriangular(0.0, 1.1485), 0.0, this.skeletonHorse.getRandom().nextTriangular(0.0, 1.1485));
            serverWorld.spawnEntityAndPassengers(abstractHorseEntity);
        }
        // Add a baby
        if ((abstractHorseEntity = this.getHorse(localDifficulty)) != null
                && (skeletonEntity = this.getSkeleton(localDifficulty, abstractHorseEntity)) != null) {
            skeletonEntity.setBaby(true);
            skeletonEntity.startRiding(abstractHorseEntity);
            abstractHorseEntity.addVelocity(this.skeletonHorse.getRandom().nextTriangular(0.0, 1.1485), 0.0, this.skeletonHorse.getRandom().nextTriangular(0.0, 1.1485));
            serverWorld.spawnEntityAndPassengers(abstractHorseEntity);
        }

        ci.cancel();
    }

    @Unique
    @Nullable
    private StrayEntity getStray(LocalDifficulty localDifficulty, AbstractHorseEntity vehicle) {
        StrayEntity strayEntity = EntityType.STRAY.create(vehicle.getWorld());
        if (strayEntity != null) {
            strayEntity.initialize((ServerWorld)vehicle.getWorld(), localDifficulty, SpawnReason.TRIGGERED, null);
            strayEntity.setPosition(vehicle.getX(), vehicle.getY(), vehicle.getZ());
            strayEntity.timeUntilRegen = 60;
            strayEntity.setPersistent();
            if (strayEntity.getEquippedStack(EquipmentSlot.HEAD).isEmpty()) {
                strayEntity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
            }
            this.enchantEquipment(strayEntity, EquipmentSlot.MAINHAND, localDifficulty);
            this.enchantEquipment(strayEntity, EquipmentSlot.HEAD, localDifficulty);
        }
        return strayEntity;
    }

    @Unique
    private void enchantEquipment(StrayEntity rider, EquipmentSlot slot, LocalDifficulty localDifficulty) {
        ItemStack itemStack = rider.getEquippedStack(slot);
        itemStack.set(DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT);
        EnchantmentHelper.applyEnchantmentProvider(itemStack, rider.getWorld().getRegistryManager(), EnchantmentProviders.MOB_SPAWN_EQUIPMENT, localDifficulty, rider.getRandom());
        rider.equipStack(slot, itemStack);
    }
}
