package com.github.galatynf.sihywtcamd.mixin.zombie;

import com.github.galatynf.sihywtcamd.Sihywtcamd;
import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.github.galatynf.sihywtcamd.mixin.LivingEntityMixin;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.github.galatynf.sihywtcamd.Sihywtcamd.id;

@Mixin(Zombie.class)
public abstract class ZombieLivingMixin extends LivingEntityMixin {
    @Unique
    private static final Identifier CALLER_BONUS_ID = id("caller_zombie_bonus");
    @Unique
    private static final Identifier RUNNER_BONUS_ID = id("runner_zombie_bonus");
    @Unique
    private static final Identifier TANK_BONUS_ID = id("tank_zombie_bonus");
    @Unique
    private static final Identifier UNSTOPPABLE_BONUS_ID = id("unstoppable_zombie_bonus");

    @Shadow public abstract boolean isBaby();

    @Shadow public abstract void setCanBreakDoors(boolean canBreakDoors);

    public ZombieLivingMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Inject(method = "finalizeSpawn", at = @At("TAIL"))
    private void spawnBabyTowers(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason spawnReason,
                                 SpawnGroupData entityData, CallbackInfoReturnable<SpawnGroupData> cir) {
        if (ModConfig.get().zombies.general.babyTowerHeight > 0
                && spawnReason != EntitySpawnReason.CONVERSION
                && this.isBaby()
                && !this.isPassenger()
                && world.getRandom().nextFloat() < 0.2f) {
            final float random = world.getRandom().nextFloat();
            Zombie lastBaby = (Zombie) (Object) this;
            Zombie.ZombieGroupData babyData = new Zombie.ZombieGroupData(true, false);

            for (int i = 0; i < ModConfig.get().zombies.general.babyTowerHeight; ++i) {
                if (random < 1.f / Math.pow(2.f, i)) {
                    Zombie newBaby = (Zombie) this.getType().create(world.getLevel(), EntitySpawnReason.JOCKEY);
                    if (newBaby == null) {
                        break;
                    }
                    newBaby.snapTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0f);
                    newBaby.startRiding(lastBaby, false, false);
                    newBaby.finalizeSpawn(world, difficulty, EntitySpawnReason.JOCKEY, babyData);
                    newBaby.setBaby(true);

                    lastBaby = newBaby;
                } else {
                    break;
                }
            }
        }
    }

    @Inject(method = "handleAttributes", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/zombie/Zombie;randomizeReinforcementsChance()V", shift = At.Shift.AFTER), cancellable = true)
    private void applyDifferentAttributes(float chanceMultiplier, EntitySpawnReason spawnReason, CallbackInfo ci) {
        float random = this.random.nextFloat();
        if (ModConfig.get().zombies.general.attributeVariations && !this.isBaby()) {
            String name = null;
            if (random < 0.1f) {
                name = "Caller";
                double value = 0.2 + 0.15 * chanceMultiplier + 0.15 * this.random.nextDouble();
                this.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE).addOrReplacePermanentModifier(
                        new AttributeModifier(CALLER_BONUS_ID, value, AttributeModifier.Operation.ADD_VALUE));
            } else if (random < 0.3f) {
                name = "Tank";
                this.setCanBreakDoors(true);
                double value = 0.5 + 1.0 * chanceMultiplier + this.random.nextDouble();
                this.getAttribute(Attributes.MAX_HEALTH).addOrReplacePermanentModifier(
                        new AttributeModifier(TANK_BONUS_ID, value, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
                this.setHealth(this.getMaxHealth());
            } else if (random < 0.5f) {
                name = "Runner";
                double value = 0.2 + 0.15 * chanceMultiplier + 0.15 * this.random.nextDouble();
                this.getAttribute(Attributes.MOVEMENT_SPEED).addOrReplacePermanentModifier(
                        new AttributeModifier(RUNNER_BONUS_ID, value, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
            } else if (random < 0.7f) {
                name = "Unstoppable";
                double value = 0.2 + 0.15 * chanceMultiplier + 0.15 * this.random.nextDouble();
                this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addOrReplacePermanentModifier(
                        new AttributeModifier(UNSTOPPABLE_BONUS_ID, value, AttributeModifier.Operation.ADD_VALUE));
            }

            if (ModConfig.get().general.enableDebugInfo && name != null) {
                this.setCustomName(Component.nullToEmpty(name));
                this.setCustomNameVisible(true);
            }
            ci.cancel();
        }
    }

    @Inject(method = "randomizeReinforcementsChance", at = @At("HEAD"), cancellable = true)
    private void initWithIncreasedAttributes(CallbackInfo ci) {
        if (ModConfig.get().zombies.general.moreKnockbackResistance) {
            this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0.2 + 0.2 * this.random.nextDouble());
        }
        if (ModConfig.get().zombies.general.spawnMoreReinforcement) {
            this.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE).setBaseValue(0.2 + 0.2 * this.random.nextDouble());
            ci.cancel();
        }
    }

    @Override
    protected void onTickRiding(CallbackInfo ci) {
        if (this.getVehicle() instanceof PathfinderMob pathAwareEntity) {
            this.yBodyRot = pathAwareEntity.yBodyRot;
        }
    }
}
