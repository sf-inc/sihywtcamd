package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.Sihywtcamd;
import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.mob.VindicatorEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(VindicatorEntity.class)
public abstract class VindicatorMixin extends IllagerEntity {
    protected VindicatorMixin(EntityType<? extends IllagerEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initialize", at = @At("HEAD"))
    private void addSpeedBonusV(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData,
                                NbtCompound entityTag, CallbackInfoReturnable<EntityData> cir) {
        if (ModConfig.get().illagers.vindicator.speedBonus && world.getRandom().nextFloat() < 0.2F) {
            this.setCustomName(Text.of("Runner"));
            this.setCustomNameVisible(Sihywtcamd.DEBUG);
            EntityAttributeInstance speed = this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
            Objects.requireNonNull(speed).addPersistentModifier(new EntityAttributeModifier(
                    "Random vindicator bonus", 0.42 * difficulty.getClampedLocalDifficulty() * speed.getValue(), EntityAttributeModifier.Operation.ADDITION));
        }
    }
}
