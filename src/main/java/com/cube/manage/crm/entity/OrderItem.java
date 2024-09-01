package com.cube.manage.crm.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "orderItem", schema = "cube")
@Data
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "sku")
    private String sku;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "amount")
    private Double amount;

}
