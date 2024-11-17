package com.cube.manage.crm.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "productItem", schema = "cube")
@Data
public class ProductItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "sku", nullable = false)
    private String sku;

    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Column(name = "size", nullable = false)
    private String size;

    @Column(name = "created_date", nullable = false)
    private Date createdDate;

    @Column(name = "updated_date")
    private Date updatedDate;

}
