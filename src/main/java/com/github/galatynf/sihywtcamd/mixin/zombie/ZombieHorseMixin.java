package com.github.galatynf.sihywtcamd.mixin.zombie;

import com.github.galatynf.sihywtcamd.entity.ZombieHorseTrapTriggerGoal;
import com.github.galatynf.sihywtcamd.imixin.ZombieHorseIMixin;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.ZombieHorseEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ZombieHorseEntity.class)
public abstract class ZombieHorseMixin extends AbstractHorseEntity implements ZombieHorseIMixin {
    @Unique
    private final ZombieHorseTrapTriggerGoal trapTriggerGoal = new ZombieHorseTrapTriggerGoal((ZombieHorseEntity)(Object) this);
    @Unique
    private boolean trapped = false;
    @Unique
    private int trapTime = -1;

    protected ZombieHorseMixin(EntityType<? extends AbstractHorseEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (this.trapped && this.trapTime++ >= 18000) {
            this.discard();
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("ZombieTrap", this.trapped);
        nbt.putInt("ZombieTrapTime", this.trapTime);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.sihywtcamd$setTrapped(nbt.getBoolean("ZombieTrap"));
        this.trapTime = nbt.getInt("ZombieTrapTime");
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
