package com.shopping.demo.Controller;

import com.shopping.demo.DTO.CartDTO;
import com.shopping.demo.DTO.CreateOrderRequest;
import com.shopping.demo.DTO.OrderDTO;
import com.shopping.demo.Entity.Order;
import com.shopping.demo.Entity.User;
import com.shopping.demo.Service.JpaUserDetailsService;
import com.shopping.demo.Service.OrderService;
import com.shopping.demo.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class OrderController {

    public final OrderService orderService;
    public final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }


    @PostMapping("/orders")
    public String createOrder(Principal principal
                                , HttpSession session,
                              RedirectAttributes redirectAttributes) {

        // 取得購物車
        CartDTO cart = (CartDTO) session.getAttribute("cart");

        //
        if (cart == null || cart.getItems().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "您的購物車是空的！");
            return "redirect:/cart";
        }

        // 呼叫 OrderService建立訂單


        // 暫時寫死 userId
        User user=userService.getUserByUserName(principal.getName());
        Integer userId=user.getId();

        try {

            Order createdOrder = orderService.createOrder(userId, cart);

            //清空 Session
            session.removeAttribute("cart");


            return "redirect:/orders/" + createdOrder.getId();

        } catch (RuntimeException e) {
            // 處理庫存不足等 例外
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/cart";
        }
    }

    @GetMapping("/users/{userId}/orders")
    @ResponseBody
    public List<OrderDTO>  getOrders(@PathVariable Integer userId){
        return orderService.getOrdersByUserId(userId);

    }

}
