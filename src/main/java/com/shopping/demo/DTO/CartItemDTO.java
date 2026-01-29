package com.shopping.demo.DTO;


public class CartItemDTO{
    private Integer productId;
    private String productName;
    private Integer quantity;
    private Integer price;


    public Integer getSubtotal(){
        return price*quantity;
    }
    public CartItemDTO() {
    }

    public CartItemDTO(Integer productId, String productName, Integer quantity, Integer price) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
