package com.cube.manage.crm.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "`order`", schema = "cube")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "customer_id", nullable = false)
    private Integer customerId;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "order_date", nullable = false)
    private Date orderDate;

    @Column(name = "updated_date")
    private Date updatedDate;

    @Column(name = "order_status_id", nullable = false)
    private Integer orderStatusId;

    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    @Column(name = "payable_price", nullable = false)
    private Double payablePrice;

    @Column(name = "pol", nullable = false)
    private Double pol = 0.0;

    @Column(name = "credit", nullable = false)
    private Double credit;

}
