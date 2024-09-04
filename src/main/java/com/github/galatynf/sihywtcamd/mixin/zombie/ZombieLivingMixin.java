package com.github.galatynf.sihywtcamd.mixin.zombie;

import com.github.galatynf.sihywtcamd.Sihywtcamd;
import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.github.galatynf.sihywtcamd.mixin.LivingEntityMixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ZombieEntity.class)
public abstract class ZombieLivingMixin extends LivingEntityMixin {
    @Shadow public abstract void setCanBreakDoors(boolean canBreakDoors);

    @Shadow protected abstract boolean shouldBreakDoors();

    @Shadow public abstract boolean isBaby();

    public ZombieLivingMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "initialize", at = @At("TAIL"))
    private void spawnBabyTowers(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason,
                                 EntityData entityData, NbtCompound entityNbt, CallbackInfoReturnable<EntityData> cir) {
        if (ModConfig.get().zombies.general.babyTowerHeight > 0
                && this.isBaby()
                && !this.hasVehicle()
                && world.getRandom().nextFloat() < 0.2f) {
            final float random = world.getRandom().nextFloat();
            ZombieEntity lastBaby = (ZombieEntity) (Object) this;
            ZombieEntity.ZombieData babyData = new ZombieEntity.ZombieData(true, false);

            for (int i = 0; i < ModConfig.get().zombies.general.babyTowerHeight; ++i) {
                if (random < 1.f / Math.pow(2.f, i)) {
                    ZombieEntity newBaby = (ZombieEntity) this.getType().create(world.toServerWorld());
                    if (newBaby == null) {
                        break;
                    }
                    newBaby.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.getYaw(), 0.0f);
                    newBaby.startRiding(lastBaby, true);
                    newBaby.initialize(world, difficulty, SpawnReason.JOCKEY, babyData, null);
                    newBaby.setBaby(true);

                    lastBaby = newBaby;
                } else {
                    break;
                }
            }
        }
    }

    @Inject(method = "applyAttributeModifiers", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/ZombieEntity;initAttributes()V", shift = At.Shift.AFTER), cancellable = true)
    private void applyDifferentAttributes(float chanceMultiplier, CallbackInfo ci) {
        float random = this.random.nextFloat();
        if (ModConfig.get().zombies.general.attributeVariations && !this.isBaby()) {
            String name = null;
            if (random < 0.1f) {
                name = "Caller";
                double value = 0.2 + 0.15 * chanceMultiplier + 0.15 * this.random.nextDouble();
                this.getAttributeInstance(EntityAttributes.ZOMBIE_SPAWN_REINFORCEMENTS).addPersistentModifier(
                        new EntityAttributeModifier("Caller zombie bonus", value, EntityAttributeModifier.Operation.ADDITION));
            } else if (random < 0.3f) {
                name = "Tank";
                this.setCanBreakDoors(this.shouldBreakDoors());
                double value = 0.5 + 1.0 * chanceMultiplier + this.random.nextDouble();
                this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).addPersistentModifier(
                        new EntityAttributeModifier("Tank zombie bonus", value, EntityAttributeModifier.Operation.MULTIPLY_BASE));
            } else if (random < 0.5f) {
                name = "Runner";
                double value = 0.2 + 0.15 * chanceMultiplier + 0.15 * this.random.nextDouble();
                this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).addPersistentModifier(
                        new EntityAttributeModifier("Runner zombie bonus", value, EntityAttributeModifier.Operation.MULTIPLY_BASE));
            } else if (random < 0.7f) {
                name = "Unstoppable";
                double value = 0.2 + 0.15 * chanceMultiplier + 0.15 * this.random.nextDouble();
                this.getAttributeInstance(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE).addPersistentModifier(
                        new EntityAttributeModifier("Unstoppable zombie bonus", value, EntityAttributeModifier.Operation.ADDITION));
            }

            if (Sihywtcamd.DEBUG && name != null) {
                this.setCustomName(Text.of(name));
                this.setCustomNameVisible(true);
            }
            ci.cancel();
        }
    }

    @Inject(method = "initAttributes", at = @At("HEAD"), cancellable = true)
    private void initWithIncreasedAttributes(CallbackInfo ci) {
        if (ModConfig.get().zombies.general.moreKnockbackResistance) {
            this.getAttributeInstance(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.2 + 0.2 * this.random.nextDouble());
        }
        if (ModConfig.get().zombies.general.spawnMoreReinforcement) {
            this.getAttributeInstance(EntityAttributes.ZOMBIE_SPAWN_REINFORCEMENTS).setBaseValue(0.2 + 0.2 * this.random.nextDouble());
            ci.cancel();
        }
    }

    @WrapOperation(method = "damage", at = @At(value = "NEW", target = "(Lnet/minecraft/world/World;)Lnet/minecraft/entity/mob/ZombieEntity;"))
    private ZombieEntity updateZombieType(World world, Operation<ZombieEntity> original) {
        if (ModConfig.get().zombies.general.sameTypeReinforcement
                && this.getType() != EntityType.ZOMBIE
                && this.random.nextBoolean()) {
            return (ZombieEntity) this.getType().create(world);
        } else {
            return original.call(world);
        }
    }

    @Override
    protected void onTickRiding(CallbackInfo ci) {
        if (this.getVehicle() instanceof PathAwareEntity pathAwareEntity) {
            this.bodyYaw = pathAwareEntity.bodyYaw;
        }
    }
}
