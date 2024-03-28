package com.github.galatynf.sihywtcamd.entity;

import java.util.EnumSet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.BowItem;
import net.minecraft.item.Items;

public class BowQuickAttackGoal<T extends HostileEntity & RangedAttackMob> extends Goal {
    private final T actor;
    private final double speed;
    private final int attackInterval;
    private final int maxShots;
    private final double squaredRange;
    private int cooldown = -1;
    private int shotCount = 0;
    private int shootInterval = 10;
    private int targetSeeingTicker;
    private boolean movingToLeft;
    private boolean backward;
    private int combatTicks = -1;

    public BowQuickAttackGoal(T actor, double speed, int attackInterval, int maxShots, double range) {
        this.actor = actor;
        this.speed = speed;
        this.attackInterval = attackInterval;
        this.maxShots = Math.max(1, maxShots);
        this.squaredRange = range * range;
        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
    }

    @Override
    public boolean canStart() {
        if (this.actor.getTarget() == null) {
            return false;
        }
        return this.isHoldingBow();
    }

    protected boolean isHoldingBow() {
        return this.actor.isHolding(Items.BOW);
    }

    @Override
    public boolean shouldContinue() {
        return (this.canStart() || !this.actor.getNavigation().isIdle()) && this.isHoldingBow();
    }

    @Override
    public void start() {
        super.start();
        this.actor.setAttacking(true);
    }

    @Override
    public void stop() {
        super.stop();
        this.actor.setAttacking(false);
        this.targetSeeingTicker = 0;
        this.cooldown = -1;
        this.shotCount = 0;
        this.actor.clearActiveItem();
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity target = this.actor.getTarget();
        if (target == null) {
            return;
        }
        double distance = this.actor.squaredDistanceTo(target.getX(), target.getY(), target.getZ());
        boolean canSee = this.actor.getVisibilityCache().canSee(target);
        boolean seeing = this.targetSeeingTicker > 0;
        if (canSee != seeing) {
            this.targetSeeingTicker = 0;
        }
        if (canSee) {
            ++this.targetSeeingTicker;
        } else {
            --this.targetSeeingTicker;
        }
        if (distance > this.squaredRange || this.targetSeeingTicker < 20) {
            this.actor.getNavigation().startMovingTo(target, this.speed);
            this.combatTicks = -1;
        } else {
            this.actor.getNavigation().stop();
            ++this.combatTicks;
        }
        if (this.combatTicks >= 20) {
            if (this.actor.getRandom().nextFloat() < 0.3f) {
                this.movingToLeft = !this.movingToLeft;
            }
            if (this.actor.getRandom().nextFloat() < 0.3f) {
                this.backward = !this.backward;
            }
            this.combatTicks = 0;
        }
        if (this.combatTicks > -1) {
            if (distance > this.squaredRange * 0.75) {
                this.backward = false;
            } else if (distance < this.squaredRange * 0.25) {
                this.backward = true;
            }
            this.actor.getMoveControl().strafeTo(this.backward ? -0.5f : 0.5f, this.movingToLeft ? 0.5f : -0.5f);
            Entity entity = this.actor.getControllingVehicle();
            if (entity instanceof MobEntity mobEntity) {
                mobEntity.lookAtEntity(target, 30.0f, 30.0f);
            }
            this.actor.lookAtEntity(target, 30.0f, 30.0f);
        } else {
            this.actor.getLookControl().lookAt(target, 30.0f, 30.0f);
        }

        if (this.actor.isUsingItem()) {
            int i;
            if (!canSee && this.targetSeeingTicker < -60) {
                this.actor.clearActiveItem();
            } else if (canSee && (i = this.actor.getItemUseTime()) >= this.shootInterval) {
                this.actor.attack(target, BowItem.getPullProgress(i));
                this.actor.clearActiveItem();
                if (++this.shotCount >= this.maxShots) {
                    this.shotCount = 0;
                    this.cooldown = this.attackInterval;
                }
            }
        } else if (--this.cooldown <= 0 && this.targetSeeingTicker >= -60) {
            this.actor.setCurrentHand(ProjectileUtil.getHandPossiblyHolding(this.actor, Items.BOW));
            this.shootInterval = this.actor.getRandom().nextBetween(8, 12);
        }
    }
}
