package com.TheMFG.E_Commerce.service.Interface;

import com.TheMFG.E_Commerce.model.ProductOrder;
import com.TheMFG.E_Commerce.model.request.OrderRequest;

public interface OrderService {
    public void saveOrder(Integer userid, OrderRequest orderRequest);
}
