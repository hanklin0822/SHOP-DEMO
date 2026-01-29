package com.shopping.demo.DAO;

import com.shopping.demo.Entity.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("Select p from Product p WHERE p.id = :id")
    Optional<Product> findByIdForUpdate(@Param("id")Integer id);

}
