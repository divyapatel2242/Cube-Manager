package com.cube.manage.crm.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "orderPickItem", schema = "cube")
@Data
public class OrderPickItem {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "sku")
    private String sku;

    @Column(name = "skid")
    private String skid;

    @Column(name = "status")
    private String status;

    @Column(name = "created_date")
    private Date createdDate;

}
