package com.github.galatynf.sihywtcamd.mixin.skeleton;

import com.github.galatynf.sihywtcamd.cardinal.MyComponents;
import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.github.galatynf.sihywtcamd.entity.EntityUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.animal.equine.SkeletonHorse;
import net.minecraft.world.entity.animal.equine.SkeletonTrapGoal;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.equipment.trim.TrimMaterials;
import net.minecraft.world.item.equipment.trim.TrimPatterns;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkeletonTrapGoal.class)
public abstract class SkeletonHorseTrapMixin {
    @Shadow @Final private SkeletonHorse horse;

    @Shadow @Nullable protected abstract AbstractHorse createHorse(DifficultyInstance localDifficulty);

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z", shift = At.Shift.AFTER), cancellable = true)
    private void replaceSkeletonsSpawn(CallbackInfo ci) {
        if (!ModConfig.get().skeletons.skeletonHorse.skeletonsVariationOnTrap) return;

        ServerLevel serverWorld = (ServerLevel) this.horse.level();
        DifficultyInstance localDifficulty = serverWorld.getCurrentDifficultyAt(this.horse.blockPosition());


        // Spawn spectral skeleton
        AbstractSkeleton skeletonEntity = EntityTypes.SKELETON.create(this.horse.level(), EntitySpawnReason.TRIGGERED);
        if (skeletonEntity == null) return;
        this.initSkeleton(skeletonEntity, localDifficulty, this.horse);
        MyComponents.SKELETON_COMPONENT.get(skeletonEntity).setSpectral();
        EntityUtils.trimEntityArmor(serverWorld, skeletonEntity, TrimMaterials.GOLD, TrimPatterns.RAISER);
        serverWorld.addFreshEntityWithPassengers(skeletonEntity);

        AbstractHorse abstractHorseEntity;
        // Spawn stray
        if ((abstractHorseEntity = this.createHorse(localDifficulty)) != null
                && (skeletonEntity = EntityTypes.STRAY.create(this.horse.level(), EntitySpawnReason.TRIGGERED)) != null) {
            this.initSkeleton(skeletonEntity, localDifficulty, abstractHorseEntity);
            EntityUtils.trimEntityArmor(serverWorld, skeletonEntity, TrimMaterials.DIAMOND, TrimPatterns.SHAPER);
            serverWorld.addFreshEntityWithPassengers(abstractHorseEntity);
        }
        // Spawn bogged
        if ((abstractHorseEntity = this.createHorse(localDifficulty)) != null
                && (skeletonEntity = EntityTypes.BOGGED.create(this.horse.level(), EntitySpawnReason.TRIGGERED)) != null) {
            this.initSkeleton(skeletonEntity, localDifficulty, abstractHorseEntity);
            EntityUtils.trimEntityArmor(serverWorld, skeletonEntity, TrimMaterials.EMERALD, TrimPatterns.HOST);
            serverWorld.addFreshEntityWithPassengers(abstractHorseEntity);
        }
        // Spawn parched
        if ((abstractHorseEntity = this.createHorse(localDifficulty)) != null
                && (skeletonEntity = EntityTypes.PARCHED.create(this.horse.level(), EntitySpawnReason.TRIGGERED)) != null) {
            this.initSkeleton(skeletonEntity, localDifficulty, abstractHorseEntity);
            EntityUtils.trimEntityArmor(serverWorld, skeletonEntity, TrimMaterials.COPPER, TrimPatterns.DUNE);
            serverWorld.addFreshEntityWithPassengers(abstractHorseEntity);
        }

        ci.cancel();
    }

    @Unique
    private void initSkeleton(AbstractSkeleton skeletonEntity, DifficultyInstance localDifficulty, AbstractHorse vehicle) {
        if (skeletonEntity == null) return;

        skeletonEntity.finalizeSpawn((ServerLevel) vehicle.level(), localDifficulty, EntitySpawnReason.TRIGGERED, null);
        skeletonEntity.setPos(vehicle.getX(), vehicle.getY(), vehicle.getZ());
        skeletonEntity.invulnerableTime = 60;
        skeletonEntity.setPersistenceRequired();

        if (skeletonEntity.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
            skeletonEntity.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
        }
        EntityUtils.enchantEquipment(skeletonEntity, EquipmentSlot.MAINHAND, localDifficulty);
        EntityUtils.enchantEquipment(skeletonEntity, EquipmentSlot.HEAD, localDifficulty);

        if (vehicle != this.horse) {
            vehicle.push(this.horse.getRandom().triangle(0.0, 1.1485), 0.0, this.horse.getRandom().triangle(0.0, 1.1485));
        }
        skeletonEntity.startRiding(vehicle);
    }
}
