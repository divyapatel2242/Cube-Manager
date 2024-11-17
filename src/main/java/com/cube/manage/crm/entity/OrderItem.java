package com.cube.manage.crm.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "orderItem", schema = "cube")
@Data
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "order_id", nullable = false)
    private Integer orderId;

    @Column(name = "sku", nullable = false)
    private String sku;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "amount", nullable = false)
    private Double amount;

}
