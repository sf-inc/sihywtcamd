package com.github.galatynf.sihywtcamd.entity;

import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.mob.MobEntity;

public class SkeletonSwimGoal extends SwimGoal {
    private final MobEntity mob2;

    public SkeletonSwimGoal(MobEntity mob) {
        super(mob);
        this.mob2 = mob;
    }

    @Override
    public void tick() {
        if (this.mob2.getTarget() == null || this.mob2.getPos().y < this.mob2.getTarget().getPos().y) {
            if (this.mob2.getRandom().nextFloat() < 0.3f) {
                this.mob2.getJumpControl().setActive();
            }
        }
    }
}
