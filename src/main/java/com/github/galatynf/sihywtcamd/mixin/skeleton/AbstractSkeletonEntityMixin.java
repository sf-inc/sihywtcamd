package com.github.galatynf.sihywtcamd.mixin.skeleton;

import com.github.galatynf.sihywtcamd.mixin.EntityMixin;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.github.galatynf.sihywtcamd.Sihywtcamd.SKELETON_BABY;

@Mixin(AbstractSkeletonEntity.class)
public abstract class AbstractSkeletonEntityMixin extends EntityMixin {
    @Override
    protected void onTrackedDataSet(TrackedData<?> data, CallbackInfo ci) {
        if (SKELETON_BABY.equals(data)) {
            this.calculateDimensions();
        }
    }
}
