package com.github.galatynf.sihywtcamd.mixin.piglin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.AbstractPiglinEntity;
import net.minecraft.entity.mob.HoglinEntity;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(PiglinEntity.class)
public abstract class PiglinMixin extends AbstractPiglinEntity {
    @Shadow protected abstract void setCannotHunt(boolean cannotHunt);

    public PiglinMixin(EntityType<? extends AbstractPiglinEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initialize", at = @At("TAIL"))
    private void spawnOnHoglin(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData,
                               NbtCompound entityTag, CallbackInfoReturnable<EntityData> cir) {
        if (ModConfig.get().nether.piglin.rideHoglin && !this.hasVehicle()
                && world.getRandom().nextFloat() < 0.05F + 0.05F * difficulty.getClampedLocalDifficulty()) {
            this.setCannotHunt(true);
            List<HoglinEntity> list = world.getEntitiesByClass(HoglinEntity.class, this.getBoundingBox().expand(5.0D, 3.0D, 5.0D), EntityPredicates.NOT_MOUNTED);
            if (!list.isEmpty()) {
                HoglinEntity hoglinEntity = list.get(0);
                this.startRiding(hoglinEntity);
            } else {
                HoglinEntity hoglinEntity2 = EntityType.HOGLIN.create(this.getWorld());
                if (hoglinEntity2 != null) {
                    hoglinEntity2.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.getYaw(), 0.0F);
                    hoglinEntity2.initialize(world, difficulty, SpawnReason.JOCKEY, null, null);
                    this.startRiding(hoglinEntity2);
                    world.spawnEntity(hoglinEntity2);
                }
            }
        }
    }
}
