package com.TheMFG.E_Commerce.service.Interface;

import com.TheMFG.E_Commerce.model.ProductOrder;
import com.TheMFG.E_Commerce.model.request.OrderRequest;

import java.util.List;

public interface OrderService {
    public void saveOrder(Integer userid, OrderRequest orderRequest);

    public List<ProductOrder> getOrdersByUser(Integer userId);

    public Boolean updateOrderStatus(Integer id,String status);

    public List<ProductOrder> getAllOrders();
}
