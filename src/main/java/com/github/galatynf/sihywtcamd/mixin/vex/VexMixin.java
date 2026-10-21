package com.github.galatynf.sihywtcamd.mixin.vex;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VexEntity.class)
public abstract class VexMixin extends HostileEntity {
    protected VexMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Unique
    @Nullable
    private MobEntity lastOwner;

    @Shadow @Nullable public abstract MobEntity getOwner();

    @Inject(method = "tick", at = @At("HEAD"))
    private void killWithMaster(CallbackInfo ci) {
        if (ModConfig.get().overworld.vex.dieWithEvoker
                && this.getEntityWorld() instanceof ServerWorld world) {
            MobEntity owner = this.getOwner();
            if (owner != null) {
                this.lastOwner = owner;
            }
            if (this.lastOwner != null && this.lastOwner.isDead()) {
                this.kill(world);
            }
        }
    }
}
