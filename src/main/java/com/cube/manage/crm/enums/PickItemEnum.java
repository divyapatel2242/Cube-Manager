package com.cube.manage.crm.enums;

public enum PickItemEnum {
    PICKED("PICKED"),
    ASSIGNED("ASSIGNED");

    public final String value;

    PickItemEnum(String value){
        this.value = value;
    }

    public String getValue(PickItemEnum pickItemEnum){
        return pickItemEnum.value;
    }


}
