package com.cube.manage.crm.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "productItem", schema = "cube")
@Data
public class ProductItem {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Column(name = "sku")
    private String sku;

    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "size")
    private String size;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "updated_date")
    private Date updatedDate;

}
