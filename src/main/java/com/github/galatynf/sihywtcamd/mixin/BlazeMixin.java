package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BlazeEntity.class)
public abstract class BlazeMixin extends HostileEntity {
    @Shadow protected abstract boolean isFireActive();

    protected BlazeMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public boolean tryAttack(Entity target) {
        boolean success = super.tryAttack(target);
        if (ModConfig.get().nether.blaze.fireAttack && success
                && this.isFireActive() && !((LivingEntity) target).isBlocking()) {
            target.setOnFireFor(5);
        }
        return success;
    }
}
