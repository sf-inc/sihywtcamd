package com.github.galatynf.sihywtcamd.cardinal.implem;

import com.github.galatynf.sihywtcamd.cardinal.api.EnderDragonEntityComponentAPI;
import net.minecraft.nbt.NbtCompound;

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
    public void readFromNbt(NbtCompound tag) {
        this.summonedCrystals = tag.getInt("summoned_crystals");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt("summoned_crystals", this.summonedCrystals);
    }
}
