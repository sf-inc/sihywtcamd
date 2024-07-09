package com.github.galatynf.sihywtcamd.cardinal.api;

import org.ladysnake.cca.api.v3.component.Component;

public interface SlimeEntityComponentAPI extends Component {
    boolean hasMerged();
    void setMerged(boolean hasMerged);
}
