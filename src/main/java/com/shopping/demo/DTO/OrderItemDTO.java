package com.shopping.demo.DTO;

import java.math.BigDecimal;

public class OrderItemDTO {
    private String productName;
    private Integer quantity;
    private int price;

    public OrderItemDTO() {
    }

    public OrderItemDTO(String productName, Integer quantity, int price) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
