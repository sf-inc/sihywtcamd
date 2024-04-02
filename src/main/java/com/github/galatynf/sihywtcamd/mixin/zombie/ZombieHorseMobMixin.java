package com.github.galatynf.sihywtcamd.mixin.zombie;

import com.github.galatynf.sihywtcamd.entity.ZombieHorseTrapTriggerGoal;
import com.github.galatynf.sihywtcamd.imixin.ZombieHorseIMixin;
import com.github.galatynf.sihywtcamd.mixin.MobEntityMixin;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.ZombieHorseEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ZombieHorseEntity.class)
public abstract class ZombieHorseMobMixin extends MobEntityMixin implements ZombieHorseIMixin {
    @Unique
    private final ZombieHorseTrapTriggerGoal trapTriggerGoal = new ZombieHorseTrapTriggerGoal((ZombieHorseEntity)(Object) this);
    @Unique
    private boolean trapped = false;
    @Unique
    private int trapTime = -1;

    protected ZombieHorseMobMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void onTickMovement(CallbackInfo ci) {
        if (this.trapped && this.trapTime++ >= 18000) {
            this.discard();
        }
    }

    @Override
    protected void readModDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        this.sihywtcamd$setTrapped(nbt.getBoolean("ZombieTrap"));
        this.trapTime = nbt.getInt("ZombieTrapTime");
    }

    @Override
    protected void writeModDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("ZombieTrap", this.trapped);
        nbt.putInt("ZombieTrapTime", this.trapTime);
    }

    @Override
    public void sihywtcamd$setTrapped(boolean trapped) {
        if (trapped == this.trapped) {
            return;
        }
        this.trapped = trapped;
        if (trapped) {
            this.goalSelector.add(1, this.trapTriggerGoal);
        } else {
            this.goalSelector.remove(this.trapTriggerGoal);
        }
    }
}
