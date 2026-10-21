package com.github.galatynf.sihywtcamd.mixin.skeleton;

import com.github.galatynf.sihywtcamd.cardinal.MyComponents;
import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.mob.StrayEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(StrayEntity.class)
public class StrayMixin {
    @ModifyReturnValue(method = "createArrowProjectile", at = @At("RETURN"))
    private PersistentProjectileEntity arrowSlownessIncreased(PersistentProjectileEntity projectileEntity) {
        if (ModConfig.get().skeletons.stray.frozenArrows) {
            MyComponents.ARROW_COMPONENT.get(projectileEntity).setFrozen();
        }

        return projectileEntity;
    }
}
