package com.cube.manage.crm.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "Order_status", schema = "cube")
@Data
public class OrderStatus {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Column(name = "status")
    private String status;

    @Column(name = "description")
    private String description;

}
