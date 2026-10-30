package com.github.galatynf.sihywtcamd.entity;

import com.github.galatynf.sihywtcamd.cardinal.MyComponents;
import com.github.galatynf.sihywtcamd.cardinal.api.PillagerEntityComponentAPI;
import com.github.galatynf.sihywtcamd.imixin.PillatrooperIMixin;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RangedCrossbowAttackGoal;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.item.component.Fireworks;

import static com.github.galatynf.sihywtcamd.Sihywtcamd.id;

/*
 * Inspired by CrossbowAttackGoal
 * */
public class CrossbowAirAttackGoal<T extends Monster & CrossbowAttackMob & PillatrooperIMixin> extends Goal {
    private final T actor;
    private Stage stage = Stage.MOVE;
    private final double speed;
    private final float squaredRange;
    private int seeingTargetTicker;
    private int chargedTicksLeft;
    private int cooldown;

    private static final Identifier REDUCED_GRAVITY_ID = id("reduced_gravity");

    public CrossbowAirAttackGoal(T actor, double speed, float range) {
        this.actor = actor;
        this.speed = speed;
        this.squaredRange = range * range;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return this.hasAliveTarget() && this.isEntityHoldingCrossbow() && this.actor.sihywtcamd$canUseAirAttack()
                && !this.actor.isPassenger() && isLanded(this.actor);
    }

    private boolean isEntityHoldingCrossbow() {
        return this.actor.isHolding(Items.CROSSBOW);
    }

    @Override
    public boolean canContinueToUse() {
        return this.hasAliveTarget() && this.isEntityHoldingCrossbow() && this.actor.sihywtcamd$canUseAirAttack();
    }

    private boolean hasAliveTarget() {
        return this.actor.getTarget() != null && this.actor.getTarget().isAlive();
    }

    @Override
    public void start() {
        super.start();
        this.stage = Stage.MOVE;
        this.chargedTicksLeft = 0;
        this.cooldown = 0;
    }

    @Override
    public void stop() {
        super.stop();
        this.actor.setAggressive(false);
        this.actor.setTarget(null);
        this.seeingTargetTicker = 0;
        if (this.actor.isUsingItem()) {
            this.actor.stopUsingItem();
            this.actor.setChargingCrossbow(false);
            this.actor.getUseItem().set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
        }
        if (this.stage.ordinal() > Stage.AIR_LAUNCH.ordinal()) {
            this.endAirAttack();
        }
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity livingEntity = this.actor.getTarget();
        if (livingEntity != null) {
            boolean bl = this.actor.getSensing().hasLineOfSight(livingEntity);
            boolean bl2 = this.seeingTargetTicker > 0;
            if (bl != bl2) {
                this.seeingTargetTicker = 0;
            }

            if (bl) {
                this.seeingTargetTicker++;
            } else {
                this.seeingTargetTicker--;
            }

            boolean isInAir = !isLanded(this.actor);
            if (this.stage.ordinal() > Stage.AIR_FLY.ordinal()) {
                if (this.actor.level().getGameTime() % 2 == 0) {
                    ((ServerLevel) this.actor.level()).sendParticles(
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
            }

            this.actor.getLookControl().setLookAt(livingEntity, 30.0F, 30.0F);
            if (this.stage == Stage.MOVE) {
                double d = this.actor.distanceToSqr(livingEntity);
                boolean bl3 = (d > (double)this.squaredRange || this.seeingTargetTicker < 5) && this.chargedTicksLeft == 0;
                if (bl3) {
                    this.moveTowards(livingEntity);
                } else {
                    this.cooldown = 0;
                    this.actor.getNavigation().stop();
                    this.stage = Stage.AIR_LAUNCH;
                }
            } else if (this.stage == Stage.AIR_LAUNCH) {
                FireworkRocketEntity fireworkRocketEntity = new FireworkRocketEntity(this.actor.level(), this.actor,
                        this.actor.getX(), this.actor.getY(), this.actor.getZ(), getFirework());
                this.actor.level().addFreshEntity(fireworkRocketEntity);
                if (this.actor.startRiding(fireworkRocketEntity)) {
                    this.actor.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, -1, 0, false, false));
                    double multiplier = -0.90;
                    AttributeInstance gravity = this.actor.getAttribute(Attributes.GRAVITY);
                    if (gravity != null && !gravity.hasModifier(REDUCED_GRAVITY_ID)) {
                        gravity.addPermanentModifier(new AttributeModifier(
                                REDUCED_GRAVITY_ID, multiplier, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
                    }

                    this.stage = Stage.AIR_FLY;
                } else {
                    fireworkRocketEntity.discard();
                    this.endAirAttack();
                }
            } else if (this.stage == Stage.AIR_FLY) {
                if (!this.actor.isPassenger()) {
                    this.stage = Stage.UNCHARGED;

                    Optional<PillagerEntityComponentAPI> pillagerComponent = MyComponents.PILLAGER_COMPONENT.maybeGet(this.actor);
                    pillagerComponent.ifPresent(pillagerComponent2 -> pillagerComponent2.setFireworkRocket(true));
                }
            } else if (this.stage == Stage.UNCHARGED) {
                this.actor.startUsingItem(ProjectileUtil.getWeaponHoldingHand(this.actor, Items.CROSSBOW));
                this.stage = Stage.CHARGING;
                this.actor.setChargingCrossbow(true);
            } else if (this.stage == Stage.CHARGING) {
                if (!this.actor.isUsingItem()) {
                    this.stage = Stage.UNCHARGED;
                }

                int i = this.actor.getTicksUsingItem();
                ItemStack itemStack = this.actor.getUseItem();
                if (i >= CrossbowItem.getChargeDuration(itemStack, this.actor)) {
                    this.actor.releaseUsingItem();
                    this.stage = Stage.CHARGED;
                    this.chargedTicksLeft = 1 + this.actor.getRandom().nextInt(20);
                    this.actor.setChargingCrossbow(false);
                }
            } else if (this.stage == Stage.CHARGED) {
                if (--this.chargedTicksLeft == 0) {
                    this.stage = Stage.READY_TO_ATTACK;
                }
            } else if (this.stage == Stage.READY_TO_ATTACK) {
                if (bl) {
                    this.actor.performRangedAttack(livingEntity, 1.0F);
                    if (isInAir && this.actor.hasEffect(MobEffects.SLOW_FALLING)) {
                        this.stage = Stage.UNCHARGED;
                    } else {
                        this.endAirAttack();
                    }
                } else if (!isInAir) {
                    if (this.seeingTargetTicker < -40) {
                        this.endAirAttack();
                    } else {
                        this.moveTowards(livingEntity);
                    }
                }
            }
        }
    }

    private void moveTowards(LivingEntity target) {
        if (--this.cooldown <= 0) {
            this.actor.getNavigation().moveTo(target, this.speed);
            this.cooldown = RangedCrossbowAttackGoal.PATHFINDING_DELAY_RANGE.sample(this.actor.getRandom());
        }
    }

    private void endAirAttack() {
        this.stage = Stage.MOVE;
        this.actor.sihywtcamd$resetDelay();

        Optional<PillagerEntityComponentAPI> pillagerComponent = MyComponents.PILLAGER_COMPONENT.maybeGet(this.actor);
        pillagerComponent.ifPresent(pillagerComponent2 -> pillagerComponent2.setFireworkRocket(false));
    }

    private static ItemStack getFirework() {
        ItemStack itemStack = new ItemStack(Items.FIREWORK_ROCKET);
        itemStack.set(DataComponents.FIREWORKS, new Fireworks(2, List.of()));
        return itemStack;
    }

    public static void removeAirEffectsOnGround(LivingEntity entity) {
        AttributeInstance gravity = entity.getAttribute(Attributes.GRAVITY);
        if (gravity != null && gravity.hasModifier(REDUCED_GRAVITY_ID)
                && !entity.isPassenger() && isLanded(entity)) {
            gravity.removeModifier(REDUCED_GRAVITY_ID);
            entity.removeEffect(MobEffects.SLOW_FALLING);
        }
    }

    private static boolean isLanded(LivingEntity entity) {
        return entity.onGround() || entity.isInLiquid();
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
