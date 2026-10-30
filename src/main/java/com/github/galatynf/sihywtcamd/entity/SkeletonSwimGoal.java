package com.github.galatynf.sihywtcamd.entity;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FloatGoal;

public class SkeletonSwimGoal extends FloatGoal {
    private final Mob mob2;

    public SkeletonSwimGoal(Mob mob) {
        super(mob);
        this.mob2 = mob;
    }

    @Override
    public void tick() {
        if (this.mob2.getTarget() == null || this.mob2.position().y < this.mob2.getTarget().position().y) {
            if (this.mob2.getRandom().nextFloat() < 0.3f) {
                this.mob2.getJumpControl().jump();
            }
        }
    }
}
