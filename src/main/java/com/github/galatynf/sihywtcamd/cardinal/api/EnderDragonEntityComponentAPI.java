package com.github.galatynf.sihywtcamd.cardinal.api;

import org.ladysnake.cca.api.v3.component.Component;

public interface EnderDragonEntityComponentAPI extends Component {
    int getNumberOfSummonedCrystals();
    void incrementNumberOfSummonedCrystals();
}
