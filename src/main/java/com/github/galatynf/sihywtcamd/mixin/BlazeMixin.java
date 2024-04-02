package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlazeEntity.class)
public abstract class BlazeMixin extends MobEntityMixin {
    @Shadow protected abstract boolean isFireActive();

    protected BlazeMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void onTryAttackSuccess(Entity target, CallbackInfoReturnable<Boolean> cir) {
        if (ModConfig.get().nether.blaze.fireAttack && this.isFireActive()) {
            target.setOnFireFor(5);
        }
    }
}
