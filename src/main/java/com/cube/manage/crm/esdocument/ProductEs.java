package com.cube.manage.crm.esdocument;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.util.Date;

@Document(indexName = "product")
@Setting(settingPath = "static/es-settings.json")
@Data
public class ProductEs {

    @Field(type = FieldType.Keyword)
    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String name;

    @Field(type = FieldType.Keyword)
    private Integer brandId;

    @Field(type = FieldType.Keyword)
    private Date createdDate;

    private String imgUrl;

    private String description;
}
