package com.cube.manage.crm.service;

import com.cube.manage.crm.entity.*;
import com.cube.manage.crm.enums.PickItemEnum;
import com.cube.manage.crm.enums.ProductUnitEnum;
//import com.cube.manage.crm.esrepo.CustomerEsRepository;
//import com.cube.manage.crm.esrepo.ProductEsRepository;
//import com.cube.manage.crm.esrepo.ProductItemEsRepository;
import com.cube.manage.crm.repository.*;
import com.cube.manage.crm.response.ImageUrlResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DataCreationService {

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CustomerRepository customerRepository;

//    @Autowired
//    private CustomerEsRepository customerEsRepository;

    @Autowired
    private ProductUnitRepository productUnitRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderPickItemRepository orderPickItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private Random random;

    @Autowired
    private InventoryStockLogRepository inventoryStockLogRepository;

    @Autowired
    private OrderHelperService orderHelperService;

    @Autowired
    private OrderAllocationService orderAllocationService;

    @Autowired
    private ProductItemRepository productItemRepository;

//    @Autowired
//    private ProductEsRepository productEsRepository;

    @Autowired
    private ProductDataRepository productDataRepository;

//    @Autowired
//    private ProductItemEsRepository productItemEsRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private InventoryService inventoryService;

    private final String BRANDS = "ADIDAS,REBOOK,NIKE,ZUDIO,PENTALOONS,SKY,BLOOM,MYNTRA,MEESHO";

    public void createBrands() {
        List<String> brands = Arrays.asList(BRANDS.split(","));
        addBrandinBulk(brands);
    }

    public void addBrandinBulk(List<String> brandRequestList) {
        List<Brand> brandList = new ArrayList<>();
        for (String brandRequest : brandRequestList){
            Brand brand = new Brand();
            brand.setName(brandRequest);
            brand.setCreatedDate(new Date());
            brandList.add(brand);
        }
        brandRepository.saveAll(brandList);
    }


    public void createCusomers() {
        List<Customer> customersList = new ArrayList<>();
        for(int i=0; i <= 10000 ; i++){
            String address = createAddress();
            String customerType = createCustomerType();
            String email = createEmail();
            String mobile = createMobileno();
            String name = createName();
            Customer customer = new Customer();
            customer.setCustomerType(customerType);
            customer.setAddress(address);
            customer.setEmail(email);
            customer.setMobile(mobile);
            customer.setName(name);
            customer.setCreatedDate(new Date());
            customersList.add(customer);
        }
        for(Customer customer : customersList){
            try{
                customerRepository.save(customer);
            } catch (Exception e){
                System.out.println(e);
            }
        }

    }

    private String createName() {
        String firstName = RandomStringUtils.randomAlphabetic(3,9);
        String lastName = RandomStringUtils.randomAlphabetic(3,9);
        return firstName + " " + lastName;
    }

    private String createMobileno() {
        String number = String.valueOf(RandomUtils.nextLong(1000000000, Long.valueOf("9999999999")));
        return number;
    }

    private String createEmail() {
        String name = RandomStringUtils.randomAlphabetic(5, 15);
        String number = RandomStringUtils.randomNumeric(1,5);
        return name+number+"@gmail.com";
    }

    private String createCustomerType() {
        List<String> listofType = Arrays.asList("Premium","Regular");
        Random rand = new Random();
        String randomElement = listofType.get(rand.nextInt(listofType.size()));
        return randomElement;
    }

    private String createAddress() {
        return RandomStringUtils.randomAlphabetic(15, 50);
    }

    /*
     * productunit
     * inventory_log
     * invetory
     * */
    public void createInventory() {
        List<ProductItem> productItemList = productItemRepository.fetchAllProductItem();
        for (ProductItem productItem : productItemList){
            if(isSkuAlreadyPresent(productItem.getSku())){
                continue;
            }
            Inventory inventory = addDetailInInventory(productItem);
            List<Productunit> productunitList = addDetailsInProductUnit(inventory, productItem);
            addDetailInInvetoryStockLog(inventory, productunitList);
        }

    }

    private boolean isSkuAlreadyPresent(String sku) {
        Inventory inventory = inventoryRepository.fetchInventoryBySku(sku);
        if(Objects.isNull(inventory)){
            return false;
        }
        return true;
    }

    private void addDetailInInvetoryStockLog(Inventory inventory, List<Productunit> productunitList) {
        InventoryStockLog inventoryStockLog = new InventoryStockLog();
        inventoryStockLog.setInventoryId(inventory.getId());
        inventoryStockLog.setInStockDate(inventory.getCreatedDate());
        inventoryStockLog.setInStockQuantity(productunitList.size());
        inventoryStockLog.setSku(inventory.getSku());
        inventoryStockLogRepository.save(inventoryStockLog);
    }

    private List<Productunit> addDetailsInProductUnit(Inventory inventory, ProductItem productItem) {
        Integer countOfSkids = fetchCountOfSkid();
        List<String> skids = fetchSkidsForCount(countOfSkids);
        List<Productunit> productunitList = new ArrayList<>();
        for(String skid : skids){
            Productunit productunit = new Productunit();
            productunit.setWarehouse(inventory.getWarehouse());
            productunit.setCreatedDate(inventory.getCreatedDate());
            productunit.setSkid(skid);
            productunit.setStatus(ProductUnitEnum.IN_STOCK.value);
            productunit.setSku(inventory.getSku());
            productunitList.add(productunit);
        }
        inventory.setAvailableQuantity(skids.size());
        productUnitRepository.saveAll(productunitList);
        inventory.setId(inventoryRepository.save(inventory).getId());
        return productunitList;
    }

    private List<String> fetchSkidsForCount(Integer countOfSkids) {
        List<String> listOfSkids = new ArrayList<>();
        for(int i=0; i < countOfSkids; i++){
            String skid = fetchValidSkid(listOfSkids, 1);
            if (Objects.isNull(skid))
                continue;
            listOfSkids.add(skid);
        }
        return listOfSkids;
    }

    private String fetchValidSkid(List<String> listOfSkids, int thCycle) {
        if(thCycle <= 3) {
            String skid = RandomStringUtils.randomNumeric(5,7);
            if(listOfSkids.contains(skid))
                return fetchValidSkid(listOfSkids, ++thCycle);
            else if (!inventoryService.validateSkid(skid))
                return fetchValidSkid(listOfSkids, ++thCycle);
            else
                return skid;
        } else {
            return null;
        }
    }

    private Integer fetchCountOfSkid() {
        List<Integer> listOfCount = Arrays.asList(5,6,7,8,9,10);
        Random random = new Random();
        return listOfCount.get(random.nextInt(listOfCount.size()));
    }

    private Inventory addDetailInInventory(ProductItem productItem) {
        List<String> listOfWarehouse = Arrays.asList("AMD", "MUM", "DEL");
        Random random = new Random();
        String warehouse = listOfWarehouse.get(random.nextInt(listOfWarehouse.size()));
        Inventory inventory = new Inventory();
        inventory.setSku(productItem.getSku());
        Date createdDate = fetchCreatedDate();
        inventory.setCreatedDate(createdDate);
        inventory.setWarehouse(warehouse);
        return inventory;
    }

    private Date fetchCreatedDate() {
        Random random = new Random();
        List<Integer> listOfDays = Arrays.asList(10,15,20,25);
        Integer day = listOfDays.get(random.nextInt(listOfDays.size()));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -day);
        return calendar.getTime();
    }


    public void allocatOrderForSingleProduct(List<ProductItem> productItemList, List<Customer> customerList) {
        for(ProductItem productItem : productItemList){
            Customer customer = customerList.get(random.nextInt(customerList.size()));
            List<Integer> listOfOrderQuantity = Arrays.asList(1,2,3);
            Integer orderQuantity = listOfOrderQuantity.get(random.nextInt(listOfOrderQuantity.size()));
            Inventory inventory = inventoryRepository.fetchInventoryBySku(productItem.getSku());
            Integer finalQuantity = orderQuantity;
            if(orderQuantity >= inventory.getAvailableQuantity()){
                if(inventory.getAvailableQuantity() == 0)
                    return;
                finalQuantity = inventory.getAvailableQuantity();
            }
            List<Productunit> allocattingProductunits = productUnitRepository.fetchAllocatingUnits(productItem.getSku(), ProductUnitEnum.IN_STOCK.value,finalQuantity);
            Order order = createRandomOrder(customer, productItem, finalQuantity);
            createRandomOrderItem(order, finalQuantity, productItem.getSku());
            createRandomOrderPickItem(allocattingProductunits, productItem.getSku(), order);
            updateRandomOrderInventory(inventory, finalQuantity, order);
        }
    }

    public void allocatOrderForMultiProduct(List<ProductItem> productItemList, List<Customer> customerList) {
        try {
            for (int i = 0; i <= 200; i++) {
                Customer customer = customerList.get(random.nextInt(customerList.size()));
                List<ProductItem> randomProductList = randomProductListMethod(productItemList);
                List<Inventory> inventoryList = inventoryRepository.fetchInventoryBySkus(randomProductList.stream().map(ProductItem::getSku).collect(Collectors.toList()));
                Map<String, Integer> skuQuantityMap = createSkuQuantityMap(inventoryList, randomProductList);
                if(skuQuantityMap.isEmpty())
                    continue;
                List<Productunit> allocattingProductunits = new ArrayList<>();
                for (Map.Entry<String, Integer> skuQuantity : skuQuantityMap.entrySet()) {
                    allocattingProductunits.addAll(productUnitRepository.fetchAllocatingUnits(skuQuantity.getKey(), ProductUnitEnum.IN_STOCK.value, skuQuantity.getValue()));
                }
                List<Product> productList = productRepository.fetchProductListByids(randomProductList.stream().map(ProductItem::getProductId).collect(Collectors.toList()));
                Order order = createRandomMultiOrder(customer, randomProductList, skuQuantityMap, productList);
                createRandomMultiOrderItem(order, randomProductList, skuQuantityMap, productList);
                createRandomMultiOrderPickItem(allocattingProductunits, order);
                updateRandomMultiOrderInventory(inventoryList, skuQuantityMap, order);
            }
        } catch (Exception e){
            System.out.println(e);
        }

    }

    private Map<String, Integer> createSkuQuantityMap(List<Inventory> inventoryList, List<ProductItem> randomProductList) {
        Map<String, Integer> skuQuantityMap = new HashMap<>();
        List<Integer> listOfOrderQuantity = Arrays.asList(1,2,3);
        for(Inventory inventory : inventoryList){
            Integer orderQuantity = listOfOrderQuantity.get(random.nextInt(listOfOrderQuantity.size()));
            Integer finalQuantity = orderQuantity;
            if(orderQuantity >= inventory.getAvailableQuantity()){
                if(inventory.getAvailableQuantity() == 0) {
                    randomProductList.stream()
                            .filter(productItem -> productItem.getSku().equalsIgnoreCase(inventory.getSku())) // Find item with id 10
                            .findFirst()
                            .ifPresent(randomProductList::remove);
                    continue;
                }
                finalQuantity = inventory.getAvailableQuantity();
            }
            skuQuantityMap.put(inventory.getSku(), finalQuantity);
        }
        return skuQuantityMap;
    }

    private List<ProductItem> randomProductListMethod(List<ProductItem> productItemList) {
        List<ProductItem> productItemList1 = new ArrayList<>();
        List<Integer> listInteger = Arrays.asList(2,3,4);
        Integer quantity = listInteger.get(random.nextInt(listInteger.size()));
        for(int i=1 ; i<=quantity ; i++){
            ProductItem productItem = productItemList.get(random.nextInt(productItemList.size()));
            productItemList1.add(productItem);
        }
        return productItemList1;
    }


    private Order createRandomOrder(Customer customer, ProductItem productItem, Integer finalQuantity){
        Product product = productRepository.fetchProductByid(productItem.getProductId());
        Order order = new Order();
        order.setOrderDate(fetchOrderDate());
        order.setOrderStatusId(orderStatusRepository.fetchStatusId("OD"));
        order.setAddress(customer.getAddress());
        order.setCustomerId(customer.getId());
        order.setPayablePrice((double)finalQuantity * product.getRetailPrice());
        order.setTotalPrice((double)finalQuantity * product.getRetailPrice());
        order.setCredit(0.0);
        orderRepository.save(order);
        return order;
    }

    private Date fetchOrderDate() {
        List<Integer> listOfDays = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
        Integer day = listOfDays.get(random.nextInt(listOfDays.size()));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -day);
        return calendar.getTime();
    }

    private Order createRandomMultiOrder(Customer customer,  List<ProductItem> randomProductList, Map<String, Integer> skuQuantityMap, List<Product> productList){
        Order order = new Order();
        order.setOrderDate(fetchOrderDate());
        order.setOrderStatusId(orderStatusRepository.fetchStatusId("OD"));
        order.setAddress(customer.getAddress());
        order.setCustomerId(customer.getId());
        Double totalPrice = 0.0;
        for(ProductItem productItem : randomProductList){
            totalPrice += productList.stream()
                    .filter(product -> product.getId().equals(productItem.getProductId())) // Correctly filter by product ID
                    .findFirst()
                    .map(product -> product.getRetailPrice() * (double) skuQuantityMap.get(productItem.getSku())) // Calculate price if product is found
                    .orElse(0.0);
        }
        order.setPayablePrice(totalPrice);
        order.setTotalPrice(totalPrice);
        order.setCredit(0.0);
        orderRepository.save(order);
        return order;
    }

    private OrderItem createRandomOrderItem(Order order, Integer finalQuantity, String sku) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(order.getId());
        orderItem.setAmount(finalQuantity * order.getPayablePrice());
        orderItem.setQuantity(finalQuantity);
        orderItem.setSku(sku);
        orderItemRepository.save(orderItem);
        return orderItem;
    }

    private List<OrderItem> createRandomMultiOrderItem(Order order, List<ProductItem> randomProductList, Map<String, Integer> skuQuantityMap, List<Product> productList) {
        List<OrderItem> orderItemsList = new ArrayList<>();
        for(ProductItem productItem : randomProductList){
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setAmount(
                    (double) skuQuantityMap.get(productItem.getSku()) *
                            productList.stream()
                                    .filter(product -> product.getId().equals(productItem.getProductId())) // Filter by product ID
                                    .findFirst() // Get the first matching product
                                    .map(Product::getRetailPrice) // Get the retail price if the product is found
                                    .orElse(0.0) // Default to 0.0 if no product is found
            );
            orderItem.setQuantity(skuQuantityMap.get(productItem.getSku()));
            orderItem.setSku(productItem.getSku());
            orderItemsList.add(orderItem);
        }
        orderItemRepository.saveAll(orderItemsList);
        return orderItemsList;
    }

    private void createRandomOrderPickItem(List<Productunit> allocattingProductunits, String sku, Order order) {
        List<OrderPickItem> orderPickItemList = new ArrayList<>();
        for (Productunit productunit : allocattingProductunits){
            OrderPickItem orderPickItem = new OrderPickItem();
            orderPickItem.setOrderId(order.getId());
            orderPickItem.setSku(sku);
            orderPickItem.setCreatedDate(order.getOrderDate());
            orderPickItem.setSkid(productunit.getSkid());
            orderPickItem.setStatus(PickItemEnum.ASSIGNED.value);
            orderPickItemList.add(orderPickItem);
            productunit.setStatus(ProductUnitEnum.ORDERED.value);
            productunit.setUpdatedDate(order.getOrderDate());
        }
        orderPickItemRepository.saveAll(orderPickItemList);
        productUnitRepository.saveAll(allocattingProductunits);
    }

    private void createRandomMultiOrderPickItem(List<Productunit> allocattingProductunits, Order order) {
        List<OrderPickItem> orderPickItemList = new ArrayList<>();
        for (Productunit productunit : allocattingProductunits){
            OrderPickItem orderPickItem = new OrderPickItem();
            orderPickItem.setOrderId(order.getId());
            orderPickItem.setSku(productunit.getSku());
            orderPickItem.setCreatedDate(order.getOrderDate());
            orderPickItem.setSkid(productunit.getSkid());
            orderPickItem.setStatus(PickItemEnum.ASSIGNED.value);
            orderPickItemList.add(orderPickItem);
            productunit.setStatus(ProductUnitEnum.ORDERED.value);
            productunit.setUpdatedDate(order.getOrderDate());
        }
        orderPickItemRepository.saveAll(orderPickItemList);
        productUnitRepository.saveAll(allocattingProductunits);
    }

    public void updateRandomOrderInventory(Inventory inventory, Integer finalQuantity, Order order) {
        inventory.setUpdatedDate(order.getOrderDate());
        Integer availableQauntity = inventory.getAvailableQuantity();
        if(finalQuantity >= availableQauntity){
            availableQauntity = 0;
            InventoryStockLog inventoryStockLog = new InventoryStockLog();
            inventoryStockLog.setInventoryId(inventory.getId());
            inventoryStockLog.setSku(inventory.getSku());
            inventoryStockLog.setOutOfStockDate(order.getOrderDate());
            inventoryStockLogRepository.save(inventoryStockLog);
        } else{
            availableQauntity = availableQauntity - finalQuantity;
        }
        inventory.setAvailableQuantity(availableQauntity);
        inventoryRepository.save(inventory);
    }

    public void updateRandomMultiOrderInventory(List<Inventory> inventoryList, Map<String,Integer> skuQtyMap, Order order) {
        for(Inventory inventory : inventoryList) {
            if(inventory.getAvailableQuantity()==0)
                continue;
            inventory.setUpdatedDate(order.getOrderDate());
            Integer availableQauntity = inventory.getAvailableQuantity();
            if (skuQtyMap.get(inventory.getSku()) >= availableQauntity) {
                availableQauntity = 0;
                InventoryStockLog inventoryStockLog = new InventoryStockLog();
                inventoryStockLog.setInventoryId(inventory.getId());
                inventoryStockLog.setSku(inventory.getSku());
                inventoryStockLog.setOutOfStockDate(order.getOrderDate());
                inventoryStockLogRepository.save(inventoryStockLog);
            } else {
                availableQauntity = availableQauntity - skuQtyMap.get(inventory.getSku());
            }
            inventory.setAvailableQuantity(availableQauntity);
            inventoryRepository.save(inventory);
        }
    }

    public void createRandomOrders() {
        List<ProductItem> productItemList = productItemRepository.fetch100ProductItems();
//        List<Customer> customerList = customerRepository.fetch100Customers(0,200);
//        orderAllocationService.allocatOrderForSingleProduct(productItemList, customerList);

        List<Customer> customerList2 = customerRepository.fetch100Customers(200,250);
        allocatOrderForMultiProduct(productItemList, customerList2);
    }

    public void createProducts() throws JsonProcessingException {
        List<String> products = new ArrayList<>();
        List<Integer> listOfBrands = brandRepository.fetchBrandIds();
        List<String> images = createImageUrl();
        for (int i=0; i<993 ; i++){
            String name = createProductName();
            String imgUrl = images.get(i);
            Date createdDate = fetchDate();
            Integer brandId = fetchBrandId(listOfBrands);
            String description = createDescription(name);
            Product product = new Product();
            product.setName(name);
            product.setDescription(description);
            product.setCreatedDate(createdDate);
            product.setBrandId(brandId);
            product.setImgUrl(imgUrl);
            Integer id = productRepository.save(product).getId();
            List<ProductItem> productItemList = new ArrayList<>();
            List<Integer> listOfLimit = Arrays.asList(2,3,4,5);
            Random rand = new Random();
            Integer limit = listOfLimit.get(rand.nextInt(listOfLimit.size()));
            String skuPrefix = RandomStringUtils.randomAlphabetic(2).toUpperCase(Locale.ROOT);
            for(int j=1; j<=limit ; j++){
                List<Integer> listOfSizes = Arrays.asList(26,28,30,32,34,36, 38,40);
                Integer size = listOfSizes.get(rand.nextInt(listOfSizes.size()));
                if(productItemList.stream().anyMatch(productItem -> productItem.getSize().equalsIgnoreCase(String.valueOf(size)))){
                    continue;
                }
                productItemList.add(createProductItems(id, createdDate, skuPrefix, String.valueOf(size)));
            }
            productItemRepository.saveAll(productItemList);
        }
    }

    private ProductItem createProductItems(Integer id, Date createdDate, String skuPrefix, String size) {
        String sku = skuPrefix + "-" + RandomStringUtils.randomNumeric(5,8);
        ProductItem productItem = new ProductItem();
        productItem.setProductId(id);
        productItem.setSize(size);
        productItem.setSku(sku);
        productItem.setCreatedDate(createdDate);
        return productItem;
    }

    private String createDescription(String name) {
        return name + " " + RandomStringUtils.randomAlphabetic(5, 10);
    }

    private Integer fetchBrandId(List<Integer> listOfBrands) {
        Random rand = new Random();
        Integer randomElement = listOfBrands.get(rand.nextInt(listOfBrands.size()));
        return randomElement;
    }

    private Date fetchDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -30);
        return calendar.getTime();
    }

    private List<String> createImageUrl() throws JsonProcessingException {
        List<String> imageUrls = new ArrayList<>();
        for(int i=1; i<=10; i++){
            String response = restTemplate.getForObject("https://picsum.photos/v2/list?page="+i+"&limit=1000", String.class);
            ObjectMapper gson = new ObjectMapper();
            List<ImageUrlResponse> imageUrlResponseList = gson.readValue(response,  new TypeReference<List<ImageUrlResponse>>(){});
            List<String> images = imageUrlResponseList.stream().map(ImageUrlResponse :: getDownload_url).collect(Collectors.toList());
            imageUrls.addAll(images);
        }
        if(imageUrls.size() < 1000){
            String response = restTemplate.getForObject("https://picsum.photos/v2/list?page=11&limit=1000", String.class);
            ObjectMapper gson = new ObjectMapper();
            List<ImageUrlResponse> imageUrlResponseList = gson.readValue(response,  new TypeReference<List<ImageUrlResponse>>(){});
            List<String> images = imageUrlResponseList.stream().map(ImageUrlResponse :: getDownload_url).collect(Collectors.toList());
            imageUrls.addAll(images);
        }
        return imageUrls;
    }

    private String createProductName() {
        List<String> listOfName = Arrays.asList("Jeans","Blouse","Swimwear", "Dress", "Skirt", "Coat", "T-shirt", "Shorts");
        Random rand = new Random();
        String randomElement = listOfName.get(rand.nextInt(listOfName.size()));
        return randomElement;
    }

    public void addProductsPrices() {
        List<String> listOfName = Arrays.asList("Swimwear", "Dress", "Skirt", "Coat", "T-shirt", "Shorts");
        for(String name : listOfName){
            List<Product> productList = productRepository.fetchProductsListFromName(name);
            Double finalPrice = 0.0;
            switch (name){
                case "Jeans":
                    finalPrice = 999.00;
                    break;
                case "Blouse":
                    finalPrice = 250.00;
                    break;
                case "Swimwear":
                    finalPrice = 1000.00;
                    break;
                case "Dress":
                    finalPrice = 1499.00;
                    break;
                case "Skirt":
                    finalPrice = 500.00;
                    break;
                case "Coat":
                    finalPrice = 1500.00;
                    break;
                case "T-shirt":
                    finalPrice = 500.00;
                    break;
                case "Shorts":
                    finalPrice = 300.00;
                    break;
            }
            for(Product product : productList) {
                List<Integer> listOfDiscount = Arrays.asList(5, 10, 15, 20, 25);
                Random random = new Random();
                Integer discount = listOfDiscount.get(random.nextInt(listOfDiscount.size()));
                product.setMrp(finalPrice);
                product.setRetailPrice((double) Math.round(finalPrice - (finalPrice * ((double)discount / 100.00))));
            }
            productRepository.saveAll(productList);
        }
    }
}
