package com.github.galatynf.sihywtcamd.mixin.skeleton;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.github.galatynf.sihywtcamd.mixin.LivingEntityMixin;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import static com.github.galatynf.sihywtcamd.Sihywtcamd.SKELETON_BABY;

@Mixin(AbstractSkeletonEntity.class)
public abstract class AbstractSkeletonLivingMixin extends LivingEntityMixin {
    public AbstractSkeletonLivingMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected boolean updateBaby(boolean original) {
        return ModConfig.get().skeletons.general.baby || ModConfig.get().skeletons.witherSkeleton.baby
                ? this.getDataTracker().get(SKELETON_BABY)
                : original;
    }
}
