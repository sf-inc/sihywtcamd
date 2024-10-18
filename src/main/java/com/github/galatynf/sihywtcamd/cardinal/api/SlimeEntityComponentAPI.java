package com.github.galatynf.sihywtcamd.cardinal.api;

import org.ladysnake.cca.api.v3.component.Component;

public interface SlimeEntityComponentAPI extends Component {
    boolean canMerge();
    boolean hasMerged();

    void setMerged();
    void updateMerged();
}
