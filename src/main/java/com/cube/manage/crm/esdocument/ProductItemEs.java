package com.cube.manage.crm.esdocument;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.util.Date;

@Data
@Document(indexName = "product_item")
@Setting(settingPath = "static/es-settings.json")
public class ProductItemEs {

    @Field(type = FieldType.Keyword)
    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String sku;

    @Field(type = FieldType.Keyword)
    private Integer productId;

    @Field(type = FieldType.Keyword)
    private Date createdDate;

    private String size;

}
