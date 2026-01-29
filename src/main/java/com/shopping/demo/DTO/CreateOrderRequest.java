package com.shopping.demo.DTO;

import java.util.List;

public class CreateOrderRequest {
    private List<BuyItem> buyItemsList;

    public List<BuyItem> getBuyItemsList() {
        return buyItemsList;
    }

    public void setBuyItemsList(List<BuyItem> buyItemsList) {
        this.buyItemsList = buyItemsList;
    }
}
