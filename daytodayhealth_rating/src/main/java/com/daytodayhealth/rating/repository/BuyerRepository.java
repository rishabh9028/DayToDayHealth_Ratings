package com.daytodayhealth.rating.repository;

import com.daytodayhealth.rating.entity.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer, Integer>{

}
