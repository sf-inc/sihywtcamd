package com.github.galatynf.sihywtcamd.cardinal.impl;

import com.github.galatynf.sihywtcamd.cardinal.api.SlimeEntityComponentAPI;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;

public class SlimeEntityComponent implements SlimeEntityComponentAPI {
    private boolean hasMerged = false;

    public boolean hasMerged() {
        return this.hasMerged;
    }

    @Override
    public void setMerged(boolean hasMerged) {
        this.hasMerged = hasMerged;
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        this.setMerged(tag.getBoolean("HasMerged"));
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        tag.putBoolean("HasMerged", this.hasMerged);
    }
}
