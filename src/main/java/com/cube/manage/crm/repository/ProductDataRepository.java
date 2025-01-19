package com.cube.manage.crm.repository;

import com.cube.manage.crm.dbutil.JDBCAccess;
import com.cube.manage.crm.dto.ProductDetailDto;
import com.cube.manage.crm.entity.Product;
//import com.cube.manage.crm.esrepo.ProductEsRepository;
import com.cube.manage.crm.esdocument.ProductEs;
import com.cube.manage.crm.response.ProductResponseData;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ProductDataRepository {

    @Autowired
    private ProductRepository productRepository;

//    @Autowired
//    private ProductEsRepository productEsRepository;

    @Autowired
    private JDBCAccess jdbcAccess;

    public ProductResponseData fetchProductResponseData(String id) {
        ProductResponseData productResponseData = new ProductResponseData();
//        ProductEs productEs = productEsRepository.findById(id).orElse(null);
//        if(Objects.nonNull(productEs)){
//            BeanUtils.copyProperties(productEs, productResponseData);
//            productResponseData.setId(Integer.parseInt(productEs.getId()));
//            //ToDo:Search brand as well
//            return productResponseData;
//        }
        Product product = productRepository.findById(Integer.valueOf(id)).orElse(null); //ToDo make query to fetch both brand and product
        if(Objects.nonNull(product)){
            BeanUtils.copyProperties(product, productResponseData);
            productResponseData.setId(product.getId());
            //ToDo:Search brand as well
            return productResponseData;
        }
        return null;
    }

    public List<ProductResponseData> fetchProductResponseDataForPage(Integer pageNo, Integer pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("pageNo", pageNo);
        params.put("pageSize", pageSize);
        String query = "select p.id as id, p.name as name, p.retail_price as cost, p.img_url as imgurl, b.name as brand_name from cube.product p inner join cube.brand b on b.id=p.brand_id order by p.id desc limit :pageNo, :pageSize";
        return jdbcAccess.findUsingNamedParameter(query, params, (rs, rowNum) -> {
            ProductResponseData productResponseData = new ProductResponseData();
            productResponseData.setName(rs.getString("name"));
            productResponseData.setId(rs.getInt("id"));
            productResponseData.setCost(rs.getDouble("cost"));
            productResponseData.setImgUrl(rs.getString("imgurl"));
            productResponseData.setBrandName(rs.getString("brand_name"));
            return productResponseData;
        });
    }

    public Integer totalSizeOfProduct(){
        return jdbcAccess.findInteger("select count(id) from cube.product;");
    }

    public List<ProductDetailDto> fetchProductData(String id) {
        Map<String, Object> param = new HashMap<>();
        param.put("productId",id);
        String sql = "SELECT \n" +
                "    p.id AS id,\n" +
                "    p.name AS name,\n" +
                "    p.img_url AS img_url,\n" +
                "    p.mrp AS product_saling_price,\n" +
                "    p.retail_price AS product_cost,\n" +
                "    p.description AS description,\n" +
                "    pi.size AS size,\n" +
                "    pi.sku AS sku,\n" +
                "    b.name AS brand_name,\n" +
                "    i.available_quantity AS available_quantity,\n" +
                "    SUM(oi.quantity) AS sku_count,\n" +
                "    sum(oi.quantity) * (p.mrp - p.retail_price) as profit,\n" +
                "    o.order_date as sale_date,\n" +
                "    os.status AS status,\n" +
                "    os.description AS status_description\n" +
                "FROM\n" +
                "    cube.product p\n" +
                "        INNER JOIN\n" +
                "    cube.product_item pi ON p.id = pi.product_id\n" +
                "        INNER JOIN\n" +
                "    cube.brand b ON p.brand_id = b.id\n" +
                "        INNER JOIN\n" +
                "    cube.inventory i ON i.sku = pi.sku\n" +
                "        LEFT JOIN\n" +
                "    cube.order_item oi ON oi.sku = pi.sku\n" +
                "        LEFT JOIN\n" +
                "    cube.order o ON o.id = oi.order_id\n" +
                "        LEFT JOIN\n" +
                "    cube.order_status os ON os.id = o.order_status_id\n" +
                "WHERE\n" +
                "    p.id = :productId \n" +
                "GROUP BY pi.sku, o.order_date order by o.order_date;\n";

        return jdbcAccess.findUsingNamedParameter(sql, param, (rs,rowNum) -> {
            ProductDetailDto productDetailDto = new ProductDetailDto();
            productDetailDto.setProductCost(rs.getDouble("product_cost"));
            productDetailDto.setBrandName(rs.getString("brand_name"));
            productDetailDto.setName(rs.getString("name"));
            productDetailDto.setProductSellingPrice(rs.getDouble("product_saling_price"));
            productDetailDto.setId(rs.getInt("id"));
            productDetailDto.setProfit(rs.getDouble("profit"));
            productDetailDto.setDescription(rs.getString("description"));
            productDetailDto.setAvailableQuantity(rs.getInt("available_quantity"));
            productDetailDto.setSize(rs.getInt("size"));
            productDetailDto.setSku(rs.getString("sku"));
            productDetailDto.setSkuCount(rs.getInt("sku_count"));
            productDetailDto.setStatus(rs.getString("status"));
            productDetailDto.setStatusDescription(rs.getString("status_description"));
            productDetailDto.setImg(rs.getString("img_url"));
            productDetailDto.setSaleDate(rs.getDate("sale_date"));
            return productDetailDto;
        });
    }
}
