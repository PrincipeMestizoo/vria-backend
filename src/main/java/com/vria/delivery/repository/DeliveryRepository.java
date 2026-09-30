package com.vria.delivery.repository;

import com.vria.delivery.enums.StateDelivery;
import com.vria.delivery.model.Delivery;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    @Override
    @EntityGraph(attributePaths = "user")
    List<Delivery> findAll();

    @EntityGraph(attributePaths = "user")
    List<Delivery> findByState(StateDelivery state);
}
