package com.github.galatynf.sihywtcamd.cardinal.impl;

import com.github.galatynf.sihywtcamd.Sihywtcamd;
import com.github.galatynf.sihywtcamd.cardinal.api.SkeletonEntityComponentAPI;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;

public class SkeletonEntityComponent implements SkeletonEntityComponentAPI {
    private final Object provider;
    private boolean isSpectral = false;

    public SkeletonEntityComponent(Object provider) {
        this.provider = provider;
    }

    @Override
    public boolean isSpectral() {
        return this.isSpectral;
    }

    @Override
    public void setSpectral() {
        this.isSpectral = true;
        if (Sihywtcamd.DEBUG && provider instanceof LivingEntity livingEntity) {
            livingEntity.setCustomName(Text.of("Spectral"));
            livingEntity.setCustomNameVisible(true);
        }
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        this.isSpectral = tag.getBoolean("IsSpectral");
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        tag.putBoolean("IsSpectral", this.isSpectral);
    }
}
