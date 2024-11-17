package com.cube.manage.crm.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "Order_status", schema = "cube")
@Data
public class OrderStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "description", nullable = false)
    private String description;

}
