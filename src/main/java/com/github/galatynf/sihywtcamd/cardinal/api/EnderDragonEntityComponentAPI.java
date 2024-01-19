package com.github.galatynf.sihywtcamd.cardinal.api;

import dev.onyxstudios.cca.api.v3.component.Component;

public interface EnderDragonEntityComponentAPI extends Component {
    int getNumberOfSummonedCrystals();
    void incrementNumberOfSummonedCrystals();
}
