package com.github.galatynf.sihywtcamd.mixin.zombie;

import com.github.galatynf.sihywtcamd.entity.ZombieHorseTrapTriggerGoal;
import com.github.galatynf.sihywtcamd.imixin.ZombieHorseIMixin;
import com.github.galatynf.sihywtcamd.mixin.MobEntityMixin;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.equine.ZombieHorse;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ZombieHorse.class)
public abstract class ZombieHorseMobMixin extends MobEntityMixin implements ZombieHorseIMixin {
    @Unique
    private final ZombieHorseTrapTriggerGoal trapTriggerGoal = new ZombieHorseTrapTriggerGoal((ZombieHorse)(Object) this);
    @Unique
    private boolean trapped = false;
    @Unique
    private boolean trapHorse = false;
    @Unique
    private int trapTime = -1;

    protected ZombieHorseMobMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void onTickMovement(CallbackInfo ci) {
        if (this.trapped && this.trapTime++ >= 18000) {
            this.discard();
        }
    }

    @Override
    protected void readModData(ValueInput readView, CallbackInfo ci) {
        this.sihywtcamd$setTrapped(readView.getBooleanOr("ZombieTrap", false));
        this.trapHorse = readView.getBooleanOr("ZombieTrapHorse", this.trapped);
        this.trapTime = readView.getIntOr("ZombieTrapTime", 0);
    }

    @Override
    protected void writeModData(ValueOutput writeView, CallbackInfo ci) {
        writeView.putBoolean("ZombieTrap", this.trapped);
        writeView.putBoolean("ZombieTrapHorse", this.trapHorse);
        writeView.putInt("ZombieTrapTime", this.trapTime);
    }

    @Override
    public void sihywtcamd$setTrapped(boolean trapped) {
        if (trapped == this.trapped) {
            return;
        }
        this.trapped = trapped;
        if (trapped) {
            this.goalSelector.addGoal(1, this.trapTriggerGoal);
        } else {
            this.goalSelector.removeGoal(this.trapTriggerGoal);
        }
    }

    @Override
    public void sihywtcamd$setTrapHorse(boolean trapHorse) {
        this.trapHorse = trapHorse;
    }

    @Override
    public boolean sihywtcamd$isTrapHorse() {
        return this.trapHorse;
    }
}
