package com.github.galatynf.sihywtcamd.cardinal.impl;

import com.github.galatynf.sihywtcamd.cardinal.api.EnderDragonEntityComponentAPI;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

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
    public void readData(ValueInput readView) {
        this.summonedCrystals = readView.getIntOr("SummonedCrystals", 0);
    }

    @Override
    public void writeData(ValueOutput writeView) {
        writeView.putInt("SummonedCrystals", this.summonedCrystals);
    }
}
