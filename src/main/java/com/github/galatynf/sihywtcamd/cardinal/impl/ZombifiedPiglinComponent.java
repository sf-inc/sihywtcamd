package com.github.galatynf.sihywtcamd.cardinal.impl;

import com.github.galatynf.sihywtcamd.cardinal.api.ZombifiedPiglinComponentAPI;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;

public class ZombifiedPiglinComponent implements ZombifiedPiglinComponentAPI {
    private boolean isBrute = false;

    @Override
    public boolean isBrute() {
        return this.isBrute;
    }

    @Override
    public void setBrute(boolean isBrute) {
        this.isBrute = isBrute;
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        this.setBrute(tag.getBoolean("IsBrute"));
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        tag.putBoolean("IsBrute", this.isBrute);
    }
}
