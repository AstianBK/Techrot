package com.the_blood_knight.techrot.common.item;

public class ToxicCanister extends ItemBase{
    boolean isFill=false;
    public ToxicCanister(String name,boolean isFill) {
        super(name);
        this.isFill = isFill;
    }
}
