package com.cube.manage.crm.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ImageUrlResponse {

    private Integer id;
    private String author;
    private Integer width;
    private Integer height;
    private String url;
    private String download_url;
}
