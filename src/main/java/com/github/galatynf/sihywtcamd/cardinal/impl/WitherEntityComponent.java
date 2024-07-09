package com.github.galatynf.sihywtcamd.cardinal.impl;

import com.github.galatynf.sihywtcamd.cardinal.api.WitherEntityComponentAPI;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;

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
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        this.halfHealthReached = tag.getBoolean("HalfHealthReached");
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        tag.putBoolean("HalfHealthReached", this.halfHealthReached);
    }
}
