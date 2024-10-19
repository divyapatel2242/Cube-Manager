package com.cube.manage.crm.service;

import com.cube.manage.crm.entity.*;
import com.cube.manage.crm.enums.PickItemEnum;
import com.cube.manage.crm.enums.ProductUnitEnum;
import com.cube.manage.crm.repository.*;
import com.cube.manage.crm.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderAllocationService {

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

    @Transactional
    public void assignSkidsToOrder(OrderRequest orderRequest) {
        List<Productunit> allocattingProductunits = productUnitRepository.fetchAllocatingUnits(orderRequest.getSku(), ProductUnitEnum.IN_STOCK.value,orderRequest.getQuantity());
        Order order = createOrder(orderRequest);
        createOrderItem(orderRequest, order);
        createOrderPickItem(allocattingProductunits, orderRequest, order);
    }

    private void createOrderPickItem(List<Productunit> allocattingProductunits, OrderRequest orderRequest, Order order) {
       List<OrderPickItem> orderPickItemList = new ArrayList<>();
       for (Productunit productunit : allocattingProductunits){
           OrderPickItem orderPickItem = new OrderPickItem();
           orderPickItem.setOrderId(order.getId());
           orderPickItem.setSku(orderRequest.getSku());
           orderPickItem.setCreatedDate(new Date());
           orderPickItem.setSkid(productunit.getSkid());
           orderPickItem.setStatus(PickItemEnum.ASSIGNED.value);
           orderPickItemList.add(orderPickItem);
           productunit.setStatus(ProductUnitEnum.ORDERED.value);
           productunit.setUpdatedDate(new Date());
       }
       orderPickItemRepository.saveAll(orderPickItemList);
       productUnitRepository.saveAll(allocattingProductunits);
    }

    private OrderItem createOrderItem(OrderRequest orderRequest, Order order) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(order.getId());
        orderItem.setAmount(orderRequest.getPayableAmount());
        orderItem.setQuantity(orderRequest.getQuantity());
        orderItem.setSku(orderRequest.getSku());
        orderItemRepository.save(orderItem);
        return orderItem;
    }

    private Order createOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderDate(new Date());
        order.setOrderStatusId(orderStatusRepository.fetchStatusId("OD"));
        order.setAddress(orderRequest.getAddress() +" "+ orderRequest.getCity() +" "+ orderRequest.getState() +" "+ orderRequest.getPinCode());
        order.setCustomerId(orderRequest.getCustomerId());
        order.setPayablePrice(orderRequest.getPayableAmount());
        order.setTotalPrice(orderRequest.getPayableAmount());
        order.setCredit(orderRequest.getPayViaCredits());
        orderRepository.save(order);
        return order;
    }

    @Transactional
    public void updateInventory(OrderRequest orderRequest) {
        Inventory inventory = inventoryRepository.fetchInventoryBySku(orderRequest.getSku());
        inventory.setUpdatedDate(new Date());
        inventory.setAvailableQuantity(inventory.getAvailableQuantity() - orderRequest.getQuantity());
        inventoryRepository.save(inventory);
    }
}
