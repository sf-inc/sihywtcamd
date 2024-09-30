package com.github.galatynf.sihywtcamd.entity;

import com.github.galatynf.sihywtcamd.imixin.PillatrooperIMixin;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.component.type.FireworksComponent;
import net.minecraft.entity.CrossbowUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.EnumSet;
import java.util.List;

import static com.github.galatynf.sihywtcamd.Sihywtcamd.id;

/*
 * Inspired by CrossbowAttackGoal
 * */
public class CrossbowAirAttackGoal<T extends HostileEntity & CrossbowUser & PillatrooperIMixin> extends Goal {
    private final T actor;
    private LivingEntity target;
    private Stage stage = Stage.MOVE;
    private final double speed;
    private final float squaredRange;
    private int seeingTargetTicker;
    private int chargedTicksLeft;

    private static final Identifier REDUCED_GRAVITY_ID = id("reduced_gravity");

    public CrossbowAirAttackGoal(T actor, double speed, float range) {
        this.actor = actor;
        this.speed = speed;
        this.squaredRange = range * range;
        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
    }

    @Override
    public boolean canStart() {
        this.target = this.actor.getTarget();
        return this.hasAliveTarget() && this.isEntityHoldingCrossbow() && this.actor.sihywtcamd$canUseAirAttack();
    }

    private boolean isEntityHoldingCrossbow() {
        return this.actor.isHolding(Items.CROSSBOW);
    }

    @Override
    public boolean shouldContinue() {
        return this.hasAliveTarget() && this.isEntityHoldingCrossbow() && this.actor.sihywtcamd$canUseAirAttack();
    }

    private boolean hasAliveTarget() {
        return this.target != null && this.target.isAlive();
    }

    @Override
    public void stop() {
        super.stop();
        this.actor.setAttacking(false);
        this.actor.setTarget(null);
        this.seeingTargetTicker = 0;
        if (this.actor.isUsingItem()) {
            this.actor.clearActiveItem();
            this.actor.setCharging(false);
            this.actor.getActiveItem().set(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.DEFAULT);
        }
        this.actor.removeStatusEffect(StatusEffects.SLOW_FALLING);
        EntityAttributeInstance gravity = this.actor.getAttributeInstance(EntityAttributes.GENERIC_GRAVITY);
        if (gravity != null) {
            gravity.removeModifier(REDUCED_GRAVITY_ID);
        }
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (this.target != null) {
            boolean bl = this.actor.getVisibilityCache().canSee(this.target);
            boolean bl2 = this.seeingTargetTicker > 0;
            if (bl != bl2) {
                this.seeingTargetTicker = 0;
            }

            if (bl) {
                this.seeingTargetTicker++;
            } else {
                this.seeingTargetTicker--;
            }

            boolean isInAir = this.actor.getSteppingBlockState().isAir();
            if (this.stage.ordinal() > Stage.AIR_FLY.ordinal()) {
                if (this.actor.getWorld().getTime() % 2 == 0) {
                    ((ServerWorld) this.actor.getWorld()).spawnParticles(
                            ParticleTypes.FIREWORK,
                            this.actor.getX(),
                            this.actor.getY(),
                            this.actor.getZ(),
                            1,
                            this.actor.getRandom().nextGaussian() * 0.5,
                            this.actor.getRandom().nextGaussian() * 0.25 - 0.25,
                            this.actor.getRandom().nextGaussian() * 0.5,
                            0.05
                    );
                }
                if (!isInAir) {
                    this.actor.removeStatusEffect(StatusEffects.SLOW_FALLING);
                    EntityAttributeInstance gravity = this.actor.getAttributeInstance(EntityAttributes.GENERIC_GRAVITY);
                    if (gravity != null) {
                        gravity.removeModifier(REDUCED_GRAVITY_ID);
                    }
                }
            }

            this.actor.getLookControl().lookAt(this.target, 30.0F, 30.0F);
            if (this.stage == Stage.MOVE) {
                double d = this.actor.squaredDistanceTo(this.target);
                boolean bl3 = (d > (double)this.squaredRange || this.seeingTargetTicker < 5) && this.chargedTicksLeft == 0;
                if (bl3) {
                    this.actor.getNavigation().startMovingTo(this.target, this.speed);
                } else {
                    this.actor.getNavigation().stop();
                    this.stage = Stage.AIR_LAUNCH;
                }
            } else if (this.stage == Stage.AIR_LAUNCH) {
                this.actor.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, -1, 0, false, false));
                double multiplier = -0.90;
                EntityAttributeInstance gravity = this.actor.getAttributeInstance(EntityAttributes.GENERIC_GRAVITY);
                if (gravity != null && !gravity.hasModifier(REDUCED_GRAVITY_ID)) {
                    gravity.addPersistentModifier(new EntityAttributeModifier(
                            REDUCED_GRAVITY_ID, multiplier, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));
                }

                FireworkRocketEntity fireworkRocketEntity = new FireworkRocketEntity(this.actor.getWorld(), this.actor,
                        this.actor.getX(), this.actor.getY(), this.actor.getZ(), getFirework());
                this.actor.getWorld().spawnEntity(fireworkRocketEntity);
                this.actor.startRiding(fireworkRocketEntity);

                this.stage = Stage.AIR_FLY;
            } else if (this.stage == Stage.AIR_FLY) {
                if (!this.actor.hasVehicle()) {
                    this.stage = Stage.UNCHARGED;
                }
            } else if (this.stage == Stage.UNCHARGED) {
                this.actor.setCurrentHand(ProjectileUtil.getHandPossiblyHolding(this.actor, Items.CROSSBOW));
                this.stage = Stage.CHARGING;
                this.actor.setCharging(true);
            } else if (this.stage == Stage.CHARGING) {
                if (!this.actor.isUsingItem()) {
                    this.stage = Stage.UNCHARGED;
                }

                int i = this.actor.getItemUseTime();
                ItemStack itemStack = this.actor.getActiveItem();
                if (i >= CrossbowItem.getPullTime(itemStack, this.actor)) {
                    this.actor.stopUsingItem();
                    this.stage = Stage.CHARGED;
                    this.chargedTicksLeft = this.actor.getRandom().nextInt(20);
                    this.actor.setCharging(false);
                }
            } else if (this.stage == Stage.CHARGED) {
                if (this.chargedTicksLeft-- <= 0) {
                    this.stage = Stage.READY_TO_ATTACK;
                }
            } else if (this.stage == Stage.READY_TO_ATTACK && bl) {
                this.actor.shootAt(this.target, 1.0F);
                if (isInAir) {
                    this.stage = Stage.UNCHARGED;
                } else {
                    this.actor.sihywtcamd$resetDelay();
                    this.stage = Stage.MOVE;
                }
            }
        }
    }

    private static ItemStack getFirework() {
        ItemStack itemStack = new ItemStack(Items.FIREWORK_ROCKET);
        itemStack.set(DataComponentTypes.FIREWORKS, new FireworksComponent(2, List.of()));
        return itemStack;
    }

    enum Stage {
        MOVE,
        AIR_LAUNCH,
        AIR_FLY,
        UNCHARGED,
        CHARGING,
        CHARGED,
        READY_TO_ATTACK
    }
}
