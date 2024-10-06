package com.cube.manage.crm.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "inventory", schema = "cube")
@Data
public class Inventory {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Column(name = "sku")
    private String sku;

    @Column(name = "available_quantity")
    private Integer availableQuantity;

    @Column(name = "warehouse")
    private String warehouse;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "updated_date")
    private Date updatedDate;

}
