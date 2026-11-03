package com.example.procurement.assistant;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorRepository extends JpaRepository<Vendor, Long> {
     Optional<Vendor> findFirstByName(String name);
}
