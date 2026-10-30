package com.github.galatynf.sihywtcamd.cardinal.impl;

import com.github.galatynf.sihywtcamd.cardinal.api.WitherEntityComponentAPI;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

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
    public void readData(ValueInput readView) {
        this.halfHealthReached = readView.getBooleanOr("HalfHealthReached", false);
    }

    @Override
    public void writeData(ValueOutput writeView) {
        writeView.putBoolean("HalfHealthReached", this.halfHealthReached);
    }
}
