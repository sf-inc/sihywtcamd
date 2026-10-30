package com.github.galatynf.sihywtcamd.cardinal.impl;

import com.github.galatynf.sihywtcamd.cardinal.api.ZombifiedPiglinComponentAPI;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

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
    public void readData(ValueInput readView) {
        this.setBrute(readView.getBooleanOr("IsBrute", false));
    }

    @Override
    public void writeData(ValueOutput writeView) {
        writeView.putBoolean("IsBrute", this.isBrute);
    }
}
