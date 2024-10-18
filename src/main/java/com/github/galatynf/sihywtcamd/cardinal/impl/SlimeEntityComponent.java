package com.github.galatynf.sihywtcamd.cardinal.impl;

import com.github.galatynf.sihywtcamd.cardinal.api.SlimeEntityComponentAPI;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;

public class SlimeEntityComponent implements SlimeEntityComponentAPI {
    private int mergeDelay = 50;

    @Override
    public boolean canMerge() {
        return this.mergeDelay == 0;
    }

    @Override
    public boolean hasMerged() {
        return this.mergeDelay < 0;
    }

    @Override
    public void setMerged() {
        this.mergeDelay = -1;
    }

    @Override
    public void updateMerged() {
        if (this.mergeDelay > 0) {
            this.mergeDelay--;
        }
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        this.mergeDelay = tag.getInt("MergeDelay");
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        tag.putInt("MergeDelay", this.mergeDelay);
    }
}
