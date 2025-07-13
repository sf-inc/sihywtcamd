package com.github.galatynf.sihywtcamd.cardinal.impl;

import com.github.galatynf.sihywtcamd.cardinal.api.EnderDragonEntityComponentAPI;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;

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
    public void readData(ReadView tag) {
        this.summonedCrystals = tag.getInt("SummonedCrystals", 0);
    }

    @Override
    public void writeData(WriteView tag) {
        tag.putInt("SummonedCrystals", this.summonedCrystals);
    }
}
