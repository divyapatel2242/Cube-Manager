package com.cube.manage.crm.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "`order`", schema = "cube")
@Data
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "address")
    private String address;

    @Column(name = "order_date")
    private Date orderDate;

    @Column(name = "updated_date")
    private Date updatedDate;

    @Column(name = "order_status_id")
    private Integer orderStatusId;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "payable_price")
    private Double payablePrice;

    @Column(name = "pol")
    private Double pol;

    @Column(name = "credit")
    private Double credit;

}
