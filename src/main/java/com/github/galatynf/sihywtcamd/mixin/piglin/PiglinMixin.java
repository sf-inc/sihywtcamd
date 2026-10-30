package com.github.galatynf.sihywtcamd.mixin.piglin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

@Mixin(Piglin.class)
public abstract class PiglinMixin extends AbstractPiglin {
    public PiglinMixin(EntityType<? extends AbstractPiglin> entityType, Level world) {
        super(entityType, world);
    }

    @ModifyReturnValue(method = "canHunt", at = @At("RETURN"))
    private boolean cannotHuntWhileRidingHoglin(boolean original) {
        return original && !(this.getVehicle() instanceof Hoglin);
    }

    @Inject(method = "finalizeSpawn", at = @At("TAIL"))
    private void spawnOnHoglin(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason spawnReason,
                               SpawnGroupData entityData, CallbackInfoReturnable<SpawnGroupData> cir) {
        if (ModConfig.get().nether.piglin.rideHoglin && !this.isPassenger()
                && world.getRandom().nextFloat() < 0.05F + 0.05F * difficulty.getSpecialMultiplier()) {
            List<Hoglin> list = world.getEntitiesOfClass(Hoglin.class, this.getBoundingBox().inflate(5.0D, 3.0D, 5.0D),
                    hoglin -> EntitySelector.ENTITY_NOT_BEING_RIDDEN.test(hoglin) && hoglin.isBaby() == this.isBaby() && !hoglin.isLeashed());
            if (!list.isEmpty()) {
                Hoglin hoglinEntity = list.getFirst();
                this.startRiding(hoglinEntity);
            } else {
                Hoglin hoglinEntity2 = EntityTypes.HOGLIN.create(this.level(), EntitySpawnReason.JOCKEY);
                if (hoglinEntity2 != null) {
                    hoglinEntity2.snapTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                    hoglinEntity2.finalizeSpawn(world, difficulty, EntitySpawnReason.JOCKEY, null);
                    hoglinEntity2.setBaby(this.isBaby());
                    this.startRiding(hoglinEntity2);
                    world.addFreshEntity(hoglinEntity2);
                }
            }
        }
    }
}
