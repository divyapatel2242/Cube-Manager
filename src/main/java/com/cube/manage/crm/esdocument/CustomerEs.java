package com.cube.manage.crm.esdocument;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

@Document(indexName = "customer")
@Setting(settingPath = "static/es-settings.json")
@Data
public class CustomerEs {

    @Field(type = FieldType.Keyword)
    @Id
    private Integer id;

    @Field(type = FieldType.Keyword)
    private String name;

    private String address;

    @Field(type = FieldType.Keyword)
    private String mobile;

    @Field(type = FieldType.Keyword)
    private String email;

    private String customerType;


}
