package com.github.galatynf.sihywtcamd.mixin.vex;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Vex.class)
public abstract class VexMixin extends Monster {
    protected VexMixin(EntityType<? extends Monster> entityType, Level world) {
        super(entityType, world);
    }

    @Unique
    @Nullable
    private LivingEntity lastOwner;

    @Shadow @Nullable public abstract LivingEntity getOwner();

    @Inject(method = "tick", at = @At("HEAD"))
    private void killWithMaster(CallbackInfo ci) {
        if (ModConfig.get().overworld.vex.dieWithEvoker
                && this.level() instanceof ServerLevel world) {
            LivingEntity owner = this.getOwner();
            if (owner != null) {
                this.lastOwner = owner;
            }
            if (this.lastOwner != null && this.lastOwner.isDeadOrDying()) {
                this.kill(world);
            }
        }
    }
}
