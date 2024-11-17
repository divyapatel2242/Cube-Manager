package com.cube.manage.crm.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "productunit", schema = "cube")
@Data
public class Productunit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "sku", nullable = false)
    private String sku;

    @Column(name = "skid", nullable = false)
    private String skid;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "warehouse", nullable = false)
    private String warehouse;

    @Column(name = "created_date", nullable = false)
    private Date createdDate;

    @Column(name = "updated_date")
    private Date updatedDate;


}
