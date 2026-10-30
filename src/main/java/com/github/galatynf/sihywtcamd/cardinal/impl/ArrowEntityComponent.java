package com.github.galatynf.sihywtcamd.cardinal.impl;

import com.github.galatynf.sihywtcamd.cardinal.api.ArrowEntityComponentAPI;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

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
    public void readData(ValueInput readView) {
        this.isFrozen = readView.getBooleanOr ("IsFrozen", false);
    }

    @Override
    public void writeData(ValueOutput writeView) {
        writeView.putBoolean("IsFrozen", this.isFrozen);
    }
}
