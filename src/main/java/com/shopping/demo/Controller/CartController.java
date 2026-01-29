package com.shopping.demo.Controller;

import com.shopping.demo.DTO.CartDTO;
import com.shopping.demo.DTO.CartItemDTO;
import com.shopping.demo.Entity.Product;
import com.shopping.demo.Service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;
import java.util.Optional;

@Controller
public class CartController {
    private final ProductService productService;

    public CartController(ProductService productService) {
        this.productService = productService;

    }

    @PostMapping("/cart/items")
    public String addToCart(@RequestParam Integer productId, @RequestParam Integer quantity, HttpSession session, RedirectAttributes redirectAttributes){
        CartDTO cart=(CartDTO) session.getAttribute("cart");

        if (cart == null) {

            cart = new CartDTO();
        }

        Optional<Product> tempproduct =productService.getProductById(productId);

        if(tempproduct.isEmpty()){
            redirectAttributes.addFlashAttribute("errorMessage", "找不到該商品！");
            return "redirect:/products";
        } //沒商品回商品頁
            Product product=tempproduct.get();

        Map<Integer, CartItemDTO> items = cart.getItems();
        // 拿cart裡東西
        CartItemDTO cartItem = items.get(productId);
        //檢查cart裡有沒有這個商品

        int currentQuantityInCart = 0;

        if (cartItem != null) {
            currentQuantityInCart = cartItem.getQuantity();
        } //檢查當個商品數量

        //買的數量+cart裡數量<=庫存
        if ((quantity + currentQuantityInCart) <= product.getStock()) {
        if(cartItem==null){ //如果購物車沒這個商品
            cartItem = new CartItemDTO();
            cartItem.setPrice(product.getPrice());
            cartItem.setQuantity(quantity);
            cartItem.setProductName(product.getName());
            cartItem.setProductId(product.getId());
        }else {
            cartItem.setQuantity(cartItem.getQuantity()+quantity);
            //商品加上數量
        }

        //放到MAP
        items.put(productId, cartItem);
        redirectAttributes.addFlashAttribute("successMessage", "已成功加入購物車！");
        } else {
            //不夠

            redirectAttributes.addFlashAttribute("errorMessage", "庫存不足！(目前庫存:"+product.getStock() +")");
        }

        session.setAttribute("cart",  cart);
        return "redirect:/productlist";
    }



    @GetMapping("/cart/remove")
    public String removeFromCart(@RequestParam("id")Integer id,HttpSession session) {
        CartDTO cart=(CartDTO) session.getAttribute("cart");

        if (cart != null && cart.getItems() != null) {

            cart.getItems().remove(id);

    }
         return "redirect:/cart";
    }

    @PostMapping("/cart/update")
    public String updateCart(@RequestParam("productId")Integer productId,
                             @RequestParam("quantity")Integer quantity,
                             HttpSession session,
                             RedirectAttributes redirectAttributes){
        Optional<Product> currentproduct=productService.getProductById(productId);

        if (currentproduct.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "商品不存在！");
            return "redirect:/cart";
        }

        Product product=currentproduct.get();
        CartDTO cart = (CartDTO) session.getAttribute("cart");
        if (cart != null && cart.getItems() != null) {
        if(quantity>product.getStock()) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "庫存不足！目前剩餘數量：" + product.getStock());
        }else {
            CartItemDTO item=cart.getItems().get(productId);
            if (item != null) {
                item.setQuantity(quantity);
            }
            }
        }
        return "redirect:/cart";
    }

    @GetMapping("/cart")
    public String showCartPage(HttpSession session, Model model){
        CartDTO cart = (CartDTO) session.getAttribute("cart");
        if (cart == null) {
            cart = new CartDTO();
        }
        model.addAttribute("cart", cart);

        return "cart";
    }


}

