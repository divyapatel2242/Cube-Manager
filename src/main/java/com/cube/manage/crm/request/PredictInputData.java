package com.cube.manage.crm.request;

import lombok.Data;

@Data
public class PredictInputData {
    private Double pclass;
    private Double age;
    private Double fare;
    private String gender;
    private String boarded;
}
