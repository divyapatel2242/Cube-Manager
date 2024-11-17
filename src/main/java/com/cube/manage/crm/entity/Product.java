package com.cube.manage.crm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "product", schema = "cube")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "brand_id", nullable = false)
    private Integer brandId;

    @Column(name = "mrp", nullable = false)
    private Double mrp;

    @Column(name = "retail_price", nullable = false)
    private Double retailPrice;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "img_url", nullable = false)
    private String imgUrl;

    @Column(name = "description")
    private String description;

    @Column(name = "created_date", nullable = false)
    private Date createdDate;

    @Column(name = "updated_date")
    private Date updatedDate;

}
