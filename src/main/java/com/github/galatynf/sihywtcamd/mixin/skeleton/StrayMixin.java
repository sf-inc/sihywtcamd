package com.github.galatynf.sihywtcamd.mixin.skeleton;

import com.github.galatynf.sihywtcamd.cardinal.MyComponents;
import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.StrayEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(StrayEntity.class)
public class StrayMixin {
    @ModifyVariable(method = "createArrowProjectile", at = @At("TAIL"))
    private PersistentProjectileEntity arrowSlownessIncreased(PersistentProjectileEntity projectileEntity) {
        if (ModConfig.get().skeletons.stray.betterSlowness && projectileEntity instanceof ArrowEntity) {
            ((ArrowEntity) projectileEntity).addEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 600, 1));
        }
        if (ModConfig.get().skeletons.stray.frozenArrows) {
            MyComponents.ARROW_COMPONENT.get(projectileEntity).setFrozen();
        }

        return projectileEntity;
    }
}
