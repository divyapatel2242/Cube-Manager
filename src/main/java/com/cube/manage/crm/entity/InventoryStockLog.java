package com.cube.manage.crm.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "inventory_stock_log")
@Data
public class InventoryStockLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "sku", nullable = false)
    private String sku;

    @Column(name = "inventory_id", nullable = false)
    private Integer inventoryId;

    @Column(name = "instock_quantity", nullable = false)
    private Integer inStockQuantity = 0;

    @Column(name = "instock_date")
    private Date inStockDate;

    @Column(name = "outofstock_date")
    private Date outOfStockDate;

}
