package com.github.galatynf.sihywtcamd.entity;

import com.github.galatynf.sihywtcamd.init.BlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.math.BlockPos;

import java.util.EnumSet;

/*
* Inspired by BowAttackGoal
* */
public class CobwebAttackGoal<T extends HostileEntity & RangedAttackMob> extends Goal {
    private final T actor;
    private final double speed;
    private final int attackInterval;
    private final float squaredRange;
    private int cooldown = -1;
    private int targetSeeingTicker;
    private boolean movingToLeft;
    private boolean backward;
    private int combatTicks = -1;

    public CobwebAttackGoal(T actor, double speed, int attackInterval, float range) {
        this.actor = actor;
        this.speed = speed;
        this.attackInterval = attackInterval;
        this.squaredRange = range * range;
        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
    }

    @Override
    public boolean canStart() {
        LivingEntity target = this.actor.getTarget();
        return target != null
                && this.actor.distanceTo(target) > 2.f
                && isNotCobweb(target.getBlockPos())
                && isNotCobweb(target.getBlockPos().up());
    }

    private boolean isNotCobweb(BlockPos blockPos) {
        return !this.actor.getWorld().getBlockState(blockPos).isIn(
                RegistryEntryList.of(
                        Block::getRegistryEntry,
                        Blocks.COBWEB,
                        BlockRegistry.MESSY_COBWEB));
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
    }

    @Override
    public void tick() {
        LivingEntity livingEntity = this.actor.getTarget();
        if (livingEntity != null) {
            double d = this.actor.squaredDistanceTo(livingEntity);
            boolean bl = this.actor.getVisibilityCache().canSee(livingEntity);
            boolean bl2 = this.targetSeeingTicker > 0;
            if (bl != bl2) {
                this.targetSeeingTicker = 0;
            }

            if (bl) {
                ++this.targetSeeingTicker;
            } else {
                --this.targetSeeingTicker;
            }

            if (d < (double)this.squaredRange && this.targetSeeingTicker >= 20) {
                this.actor.getNavigation().stop();
                ++this.combatTicks;
            } else {
                this.actor.getNavigation().startMovingTo(livingEntity, this.speed);
                this.combatTicks = -1;
            }

            if (this.combatTicks >= 20) {
                if ((double)this.actor.getRandom().nextFloat() < 0.3D) {
                    this.movingToLeft = !this.movingToLeft;
                }

                if ((double)this.actor.getRandom().nextFloat() < 0.3D) {
                    this.backward = !this.backward;
                }

                this.combatTicks = 0;
            }

            if (this.combatTicks > -1) {
                if (d > (double)(this.squaredRange * 0.75F)) {
                    this.backward = false;
                } else if (d < (double)(this.squaredRange * 0.25F)) {
                    this.backward = true;
                }

                this.actor.getMoveControl().strafeTo(this.backward ? -0.5F : 0.5F, this.movingToLeft ? 0.5F : -0.5F);
                this.actor.lookAtEntity(livingEntity, 30.0F, 30.0F);
            } else {
                this.actor.getLookControl().lookAt(livingEntity, 30.0F, 30.0F);
            }

            if (bl && --this.cooldown < 0) {
                this.actor.attack(livingEntity, 1.0F);
                this.cooldown = this.attackInterval;
            }
        }
    }
}
