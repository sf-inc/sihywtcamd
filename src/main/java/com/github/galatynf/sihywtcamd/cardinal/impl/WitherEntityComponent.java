package com.github.galatynf.sihywtcamd.cardinal.impl;

import com.github.galatynf.sihywtcamd.cardinal.api.WitherEntityComponentAPI;
import net.minecraft.nbt.NbtCompound;

public class WitherEntityComponent implements WitherEntityComponentAPI {
    private boolean halfHealthReached = false;

    @Override
    public boolean wasHalfHealthReached() {
        return this.halfHealthReached;
    }

    @Override
    public void setHalfHealthReached() {
        this.halfHealthReached = true;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.halfHealthReached = tag.getBoolean("HalfHealthReached");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putBoolean("HalfHealthReached", this.halfHealthReached);
    }
}
