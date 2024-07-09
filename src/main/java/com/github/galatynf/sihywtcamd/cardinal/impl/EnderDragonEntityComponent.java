package com.github.galatynf.sihywtcamd.cardinal.impl;

import com.github.galatynf.sihywtcamd.cardinal.api.EnderDragonEntityComponentAPI;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;

public class EnderDragonEntityComponent implements EnderDragonEntityComponentAPI {
    private int summonedCrystals = 0;

    @Override
    public int getNumberOfSummonedCrystals() {
        return this.summonedCrystals;
    }

    @Override
    public void incrementNumberOfSummonedCrystals() {
        this.summonedCrystals++;
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        this.summonedCrystals = tag.getInt("SummonedCrystals");
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        tag.putInt("SummonedCrystals", this.summonedCrystals);
    }
}
