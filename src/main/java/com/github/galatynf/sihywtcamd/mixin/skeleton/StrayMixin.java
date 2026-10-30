package com.github.galatynf.sihywtcamd.mixin.skeleton;

import com.github.galatynf.sihywtcamd.cardinal.MyComponents;
import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.monster.skeleton.Stray;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Stray.class)
public class StrayMixin {
    @ModifyReturnValue(method = "getArrow", at = @At("RETURN"))
    private AbstractArrow arrowSlownessIncreased(AbstractArrow projectileEntity) {
        if (ModConfig.get().skeletons.stray.frozenArrows) {
            MyComponents.ARROW_COMPONENT.get(projectileEntity).setFrozen();
        }

        return projectileEntity;
    }
}
