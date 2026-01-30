package com.github.galatynf.sihywtcamd.mixin.zombie;

import com.github.galatynf.sihywtcamd.entity.ZombieHorseTrapTriggerGoal;
import com.github.galatynf.sihywtcamd.imixin.ZombieHorseIMixin;
import com.github.galatynf.sihywtcamd.mixin.MobEntityMixin;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.ZombieHorseEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
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
    protected void readModData(ReadView readView, CallbackInfo ci) {
        this.sihywtcamd$setTrapped(readView.getBoolean("ZombieTrap", false));
        this.trapTime = readView.getInt("ZombieTrapTime", 0);
    }

    @Override
    protected void writeModData(WriteView writeView, CallbackInfo ci) {
        writeView.putBoolean("ZombieTrap", this.trapped);
        writeView.putInt("ZombieTrapTime", this.trapTime);
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
