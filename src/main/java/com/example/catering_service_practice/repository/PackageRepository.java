package com.example.catering_service_practice.repository;


import com.example.catering_service_practice.model.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageRepository extends JpaRepository<Package, Integer> {
    Package getPackageById(Integer id);
}
