package com.example.catering_service_practice.service;


import com.example.catering_service_practice.model.Package;
import com.example.catering_service_practice.repository.OrderRepository;
import com.example.catering_service_practice.repository.PackageRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
public class PackageService {

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private OrderRepository orderRepository;


    // save pack
    public void storedPackage(Package pkg) {
        packageRepository.save(pkg);
    }


    // update pack
    public boolean updatePackage(Package pkg, int id) {

        Optional<Package> optionalPackage = packageRepository.findById(id);
        if (optionalPackage.isEmpty()) {
            return false; //Package not found!!
        }

        // set fields
        Package pack = new Package();
        pack.setId(id);
        pack.setName(pkg.getName());
        pack.setPrice(pkg.getPrice());
        pack.setImgUrl(pkg.getImgUrl());
        pack.setDescription(pkg.getDescription());

        packageRepository.save(pack);
        return true; // Package updated successfully
    }


    // get all pack
    public List<Package> getAllPackage() {
        List<Package> packages = packageRepository.findAll();
        return  packages;
    }


    // get one pack
    public Optional<Package> getOnePackage(@PathVariable Integer id) {
        Optional<Package> optionalPackage = packageRepository.findById(id);
        return optionalPackage;
    }


    // delete a pack
    @Transactional
    public void deletePackageById(int packageId) {
        // Delete orders associated with the package
        orderRepository.deleteByPkgId(packageId);

        // Delete the package itself
        packageRepository.deleteById(packageId);
    }







//    public void deletePackageById(int id) {
//        // First, find the package by its ID
//        Optional<Package> packageOptional = packageRepository.findById(id);
//
//        // If the package exists, delete orders associated with it
//        packageOptional.ifPresent(pkg -> {
//            orderRepository.deleteByPkg(pkg);
//        });
//
//        // Then, delete the package itself
//        packageRepository.deleteById(id);
//    }






//    public void deletePackageById(int id) {
//        // First, delete the orders associated with the package
//        orderRepository.deleteById(id);
//
//        // Then, delete the package itself
//        packageRepository.deleteById(id);
//
////        orderRepository.deleteById((long) id);
////        packageRepository.deleteById(id);
//    }
}
