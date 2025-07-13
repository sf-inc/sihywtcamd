package com.github.galatynf.sihywtcamd.cardinal.impl;

import com.github.galatynf.sihywtcamd.cardinal.api.ZombifiedPiglinComponentAPI;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;

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
    public void readData(ReadView tag) {
        this.setBrute(tag.getBoolean("IsBrute", false));
    }

    @Override
    public void writeData(WriteView tag) {
        tag.putBoolean("IsBrute", this.isBrute);
    }
}
