package com.cube.manage.crm.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "orderPickItem", schema = "cube")
@Data
public class OrderPickItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "order_id", nullable = false)
    private Integer orderId;

    @Column(name = "sku", nullable = false)
    private String sku;

    @Column(name = "skid", nullable = false)
    private String skid;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "created_date", nullable = false)
    private Date createdDate;

}
