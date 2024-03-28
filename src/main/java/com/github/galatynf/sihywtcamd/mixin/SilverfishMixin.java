package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SilverfishEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SilverfishEntity.class)
public class SilverfishMixin extends HostileEntity {
    protected SilverfishMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        if (ModConfig.get().arthropods.general.larvaeSpeedBonus) {
            EntityAttributeInstance speed = this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
            if (speed != null) {
                double random = 0.25 * world.getRandom().nextDouble() * speed.getValue();
                double localDifficulty = 0.25 * difficulty.getClampedLocalDifficulty() * speed.getValue();
                speed.addPersistentModifier(new EntityAttributeModifier(
                        "Random speed bonus",  random + localDifficulty, EntityAttributeModifier.Operation.ADDITION));
            }
        }
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }
}
