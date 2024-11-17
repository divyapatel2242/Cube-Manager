package com.cube.manage.crm.enums;

public enum WarehousesEnum {
    AMD("AMD"),
    MUM("MUM"),
    DEL("DEL");

    public final String value;

    WarehousesEnum(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
