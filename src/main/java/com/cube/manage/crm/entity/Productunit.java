package com.cube.manage.crm.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "productunit", schema = "cube")
@Data
public class Productunit {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Column(name = "sku")
    private String sku;

    @Column(name = "skid")
    private String skid;

    @Column(name = "status")
    private String status;

    @Column(name = "created_date")
    private Date createdDate;

}
