package com.shopping.demo.Service;

import com.shopping.demo.DAO.OrderItemRepository;
import com.shopping.demo.DAO.OrderRepository;
import com.shopping.demo.DAO.ProductRepository;
import com.shopping.demo.DAO.UserRepository;
import com.shopping.demo.DTO.*;
import com.shopping.demo.Entity.Order;
import com.shopping.demo.Entity.OrderItem;
import com.shopping.demo.Entity.Product;
import com.shopping.demo.Entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
    public final OrderRepository orderRepository;
    public final OrderItemRepository orderItemRepository;
    public final ProductRepository productRepository;
    public final UserRepository userRepository;
    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }


        public List<OrderDTO> getOrdersByUserId(Integer userId){
        List<Order> orderlist=orderRepository.findOrdersByUserIdWithDetails(userId);
        List<OrderDTO> orderDTOS=new ArrayList<>();

        for(Order order:orderlist){
            OrderDTO orderDTO=new OrderDTO();

            orderDTO.setOrderId(order.getId());
            orderDTO.setOrderDate(order.getOrderDate());
            orderDTO.setTotalAmount(order.getTotalPrice());

            for(OrderItem orderItem:order.getOrderItems()){
                OrderItemDTO orderItemDTO=new OrderItemDTO();
                orderItemDTO.setPrice(orderItem.getPrice());
                orderItemDTO.setQuantity(orderItem.getQuantity());
                orderItemDTO.setProductName(orderItem.getProduct().getName());
                orderDTO.getOrderItemDTOList().add(orderItemDTO);

            }

            orderDTOS.add(orderDTO);


        }
        return orderDTOS;


    }
        public OrderDTO getOrderById(Integer orderId){
        Optional<Order> orderOptional=orderRepository.findOrderByOrderId(orderId);
            if (orderOptional.isEmpty()) {

                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
            }
            Order order=orderOptional.get();
            OrderDTO orderDTO=new OrderDTO();
            orderDTO.setOrderId(order.getId());
            orderDTO.setOrderDate(order.getOrderDate());
            orderDTO.setTotalAmount(order.getTotalPrice());

            List<OrderItemDTO> orderItemDTOs = order.getOrderItems().stream().map(orderItem -> {
                OrderItemDTO itemDTO = new OrderItemDTO();
                itemDTO.setProductName(orderItem.getProduct().getName());
                itemDTO.setQuantity(orderItem.getQuantity());
                itemDTO.setPrice(orderItem.getPrice());
                return itemDTO;
            }).collect(Collectors.toList());

            orderDTO.setOrderItemDTOList(orderItemDTOs);

            return orderDTO;


        }

    @Transactional
    public Order createOrder(Integer userId, CartDTO cart) {

        // 找User
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("找不到使用者"));

        // 建立新Order
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now()); // 6. 【新增】import java.time.LocalDateTime

        //遍歷 cart 加入 items

        Integer totalAmount = 0;
        for (CartItemDTO cartItem : cart.getItems().values()) {


            //   悲觀鎖 防止超賣
            Product product = productRepository.findByIdForUpdate(cartItem.getProductId())
                    .orElseThrow(() -> new RuntimeException("找不到商品: " + cartItem.getProductName()));

            //  檢查並扣庫存
            if (product.getStock() < cartItem.getQuantity()) {
                // rollback
                throw new RuntimeException("庫存不足: " + product.getName());
            }
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product); // 存回更新後的庫存

            // OrderItem Entity
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order); // 建立雙向關聯
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(product.getPrice()); //product 的當前價格

            // 將 OrderItem 加入 Order 的 List 中

            order.getOrderItems().add(orderItem);

            // 計算總金額
            totalAmount += (product.getPrice() * cartItem.getQuantity());
        }

        // 設定訂單總金額
        order.setTotalPrice(totalAmount);

        // 儲存 Order (因為 CascadeType.ALL, OrderItem 會自動一起儲存)
        return orderRepository.save(order);
    }
}
