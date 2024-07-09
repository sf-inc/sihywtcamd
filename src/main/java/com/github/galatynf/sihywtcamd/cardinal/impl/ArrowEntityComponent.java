package com.github.galatynf.sihywtcamd.cardinal.impl;

import com.github.galatynf.sihywtcamd.cardinal.api.ArrowEntityComponentAPI;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;

public class ArrowEntityComponent implements ArrowEntityComponentAPI {
    private boolean isFrozen = false;

    @Override
    public boolean isFrozen() {
        return this.isFrozen;
    }

    @Override
    public void setFrozen() {
        this.isFrozen = true;
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        this.isFrozen = tag.getBoolean("IsFrozen");
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        tag.putBoolean("IsFrozen", this.isFrozen);
    }
}
