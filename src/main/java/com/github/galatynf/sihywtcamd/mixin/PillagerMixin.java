package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.mob.PillagerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(PillagerEntity.class)
public abstract class PillagerMixin extends IllagerEntity {
    protected PillagerMixin(EntityType<? extends IllagerEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initialize", at = @At("HEAD"))
    private void addSpeedBonusP(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData,
                                CompoundTag entityTag, CallbackInfoReturnable<EntityData> cir) {
        if (ModConfig.get().pillagerSpeedBonus && world.getRandom().nextFloat() < 0.25F) {
            EntityAttributeInstance speed = this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
            Objects.requireNonNull(speed).addPersistentModifier(new EntityAttributeModifier(
                    "Random pillager bonus", 0.69 * difficulty.getClampedLocalDifficulty() * speed.getValue(), EntityAttributeModifier.Operation.ADDITION));
        }
    }

    @ModifyArg(method = "method_30759", at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I"))
    private int changeProbabilityMoreEnchant(int range) {
        return ModConfig.get().pillagerMoreEnchants ? range / 3 : range;
    }
}
