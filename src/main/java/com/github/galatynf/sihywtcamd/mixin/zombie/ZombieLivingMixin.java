package com.github.galatynf.sihywtcamd.mixin.zombie;

import com.github.galatynf.sihywtcamd.Sihywtcamd;
import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.github.galatynf.sihywtcamd.mixin.LivingEntityMixin;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.github.galatynf.sihywtcamd.Sihywtcamd.id;

@Mixin(ZombieEntity.class)
public abstract class ZombieLivingMixin extends LivingEntityMixin {
    @Unique
    private static final Identifier CALLER_BONUS_ID = id("caller_zombie_bonus");
    @Unique
    private static final Identifier RUNNER_BONUS_ID = id("runner_zombie_bonus");
    @Unique
    private static final Identifier TANK_BONUS_ID = id("tank_zombie_bonus");
    @Unique
    private static final Identifier UNSTOPPABLE_BONUS_ID = id("unstoppable_zombie_bonus");

    @Shadow public abstract boolean canBreakDoors();
    @Shadow public abstract boolean isBaby();

    @Shadow public abstract void setCanBreakDoors(boolean canBreakDoors);

    public ZombieLivingMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "initialize", at = @At("TAIL"))
    private void spawnBabyTowers(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason,
                                 EntityData entityData, CallbackInfoReturnable<EntityData> cir) {
        if (ModConfig.get().zombies.general.babyTowerHeight > 0
                && this.isBaby()
                && !this.hasVehicle()
                && world.getRandom().nextFloat() < 0.2f) {
            final float random = world.getRandom().nextFloat();
            ZombieEntity lastBaby = (ZombieEntity) (Object) this;
            ZombieEntity.ZombieData babyData = new ZombieEntity.ZombieData(true, false);

            for (int i = 0; i < ModConfig.get().zombies.general.babyTowerHeight; ++i) {
                if (random < 1.f / Math.pow(2.f, i)) {
                    ZombieEntity newBaby = (ZombieEntity) this.getType().create(world.toServerWorld(), SpawnReason.JOCKEY);
                    if (newBaby == null) {
                        break;
                    }
                    newBaby.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.getYaw(), 0.0f);
                    newBaby.startRiding(lastBaby, true);
                    newBaby.initialize(world, difficulty, SpawnReason.JOCKEY, babyData);
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
                this.getAttributeInstance(EntityAttributes.SPAWN_REINFORCEMENTS).addPersistentModifier(
                        new EntityAttributeModifier(CALLER_BONUS_ID, value, EntityAttributeModifier.Operation.ADD_VALUE));
            } else if (random < 0.3f) {
                name = "Tank";
                this.setCanBreakDoors(this.canBreakDoors());
                double value = 0.5 + 1.0 * chanceMultiplier + this.random.nextDouble();
                this.getAttributeInstance(EntityAttributes.MAX_HEALTH).addPersistentModifier(
                        new EntityAttributeModifier(TANK_BONUS_ID, value, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));
            } else if (random < 0.5f) {
                name = "Runner";
                double value = 0.2 + 0.15 * chanceMultiplier + 0.15 * this.random.nextDouble();
                this.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED).addPersistentModifier(
                        new EntityAttributeModifier(RUNNER_BONUS_ID, value, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));
            } else if (random < 0.7f) {
                name = "Unstoppable";
                double value = 0.2 + 0.15 * chanceMultiplier + 0.15 * this.random.nextDouble();
                this.getAttributeInstance(EntityAttributes.KNOCKBACK_RESISTANCE).addPersistentModifier(
                        new EntityAttributeModifier(UNSTOPPABLE_BONUS_ID, value, EntityAttributeModifier.Operation.ADD_VALUE));
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
            this.getAttributeInstance(EntityAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.2 + 0.2 * this.random.nextDouble());
        }
        if (ModConfig.get().zombies.general.spawnMoreReinforcement) {
            this.getAttributeInstance(EntityAttributes.SPAWN_REINFORCEMENTS).setBaseValue(0.2 + 0.2 * this.random.nextDouble());
            ci.cancel();
        }
    }

    @Override
    protected void onTickRiding(CallbackInfo ci) {
        if (this.getVehicle() instanceof PathAwareEntity pathAwareEntity) {
            this.bodyYaw = pathAwareEntity.bodyYaw;
        }
    }
}
