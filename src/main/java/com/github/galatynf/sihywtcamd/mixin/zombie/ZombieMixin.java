package com.github.galatynf.sihywtcamd.mixin.zombie;

import com.github.galatynf.sihywtcamd.Sihywtcamd;
import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
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
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ZombieEntity.class)
public abstract class ZombieMixin extends HostileEntity {
    @Shadow public abstract void setCanBreakDoors(boolean canBreakDoors);

    @Shadow protected abstract boolean shouldBreakDoors();

    @Shadow public abstract boolean isBaby();

    protected ZombieMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
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

            for (int i = 0; i < ModConfig.get().zombies.general.babyTowerHeight; ++i) {
                if (random < 1.f / Math.pow(2.f, i)) {
                    ZombieEntity newBaby = (ZombieEntity) this.getType().create(world.toServerWorld());
                    if (newBaby == null) {
                        break;
                    }
                    newBaby.setBaby(true);
                    newBaby.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.getYaw(), 0.0f);
                    newBaby.startRiding(lastBaby, true);
                    newBaby.initialize(world, difficulty, SpawnReason.JOCKEY, null, null);

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

    @ModifyVariable(method = "damage", at = @At("STORE"))
    private ZombieEntity updateZombieType(ZombieEntity zombie) {
        return ModConfig.get().zombies.general.sameTypeReinforcement && this.random.nextBoolean()
                ? (ZombieEntity) this.getType().create(zombie.getWorld())
                : zombie;
    }

    @Override
    public void tickRiding() {
        super.tickRiding();
        if (this.getVehicle() instanceof PathAwareEntity pathAwareEntity) {
            this.bodyYaw = pathAwareEntity.bodyYaw;
        }
    }
}
