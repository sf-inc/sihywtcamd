package com.github.galatynf.sihywtcamd.cardinal.impl;

import com.github.galatynf.sihywtcamd.cardinal.api.SkeletonEntityComponentAPI;
import net.minecraft.nbt.NbtCompound;

public class SkeletonEntityComponent implements SkeletonEntityComponentAPI {
    private boolean isSpectral = false;

    @Override
    public boolean isSpectral() {
        return this.isSpectral;
    }

    @Override
    public void setSpectral() {
        this.isSpectral = true;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.isSpectral = tag.getBoolean("IsSpectral");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putBoolean("IsSpectral", this.isSpectral);
    }
}
