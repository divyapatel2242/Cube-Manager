package com.cube.manage.crm.enums;

public enum ProductUnitEnum {

    IN_STOCK("IN_STOCK"),
    ORDERED("ORDERED"),
    DAMAGED("DAMAGED"),
    MISSING("MISSING");

    public final String value;

    ProductUnitEnum(String value){
        this.value = value;
    }

    public String getValue(ProductUnitEnum productUnitEnum){
        return productUnitEnum.value;
    }

}
