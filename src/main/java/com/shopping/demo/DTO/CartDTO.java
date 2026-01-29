package com.shopping.demo.DTO;

import java.util.HashMap;
import java.util.Map;

public class CartDTO {
    private Map<Integer,CartItemDTO> items=new HashMap<>();

    public Integer getTotalAmount(){
        return items.values().stream()
                .mapToInt(CartItemDTO::getSubtotal)
                .sum();
    }

    public CartDTO() {
    }

    public CartDTO(Map<Integer, CartItemDTO> items) {
        this.items = items;
    }

    public Map<Integer, CartItemDTO> getItems() {
        return items;
    }

    public void setItems(Map<Integer, CartItemDTO> items) {
        this.items = items;
    }
}
