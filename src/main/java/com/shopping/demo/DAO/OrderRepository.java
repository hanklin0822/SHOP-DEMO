package com.shopping.demo.DAO;


import com.shopping.demo.DTO.OrderDTO;
import com.shopping.demo.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer> {

    @Query("SELECT DISTINCT o FROM Order o " + // 加上 DISTINCT
            "LEFT JOIN FETCH o.orderItems oi " +
            "LEFT JOIN FETCH oi.product p " +
            "WHERE o.user.id = :userId")
    List<Order> findOrdersByUserIdWithDetails(@Param("userId") Integer userId);

    @Query("SELECT DISTINCT o FROM Order o " + //  加上 DISTINCT
            "LEFT JOIN FETCH o.orderItems oi " +
            "LEFT JOIN FETCH oi.product p " +
            "WHERE o.id = :orderId")
    Optional<Order> findOrderByOrderId(Integer orderId);
}
