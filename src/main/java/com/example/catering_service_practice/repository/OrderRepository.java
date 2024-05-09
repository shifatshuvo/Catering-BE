package com.example.catering_service_practice.repository;

import com.example.catering_service_practice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUserId(Long userId);

    @Query("SELECT o FROM Order o WHERE o.user.id = :userId")
    List<Order> getAllByUserId(@Param("userId") Long userId);

    void deleteByPkgId(int packageId);
    void deleteByUserId(Long userId);
//    void deleteByOrderId(Long orderId);
}
