package com.example.catering_service_practice.controller;

//import com.example.catering_service_practice.model.Package;
import com.example.catering_service_practice.model.Package;
import com.example.catering_service_practice.repository.PackageRepository;
import com.example.catering_service_practice.service.PackageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/packages")
public class PackageController {


    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private PackageService packageService;


    // save a pack
    @PostMapping
    private void savePackage(@RequestBody @Valid Package pkg) {
        packageService.storedPackage(pkg);
    }


    // get all pack
    @GetMapping
    public List<Package> getAllPackage() {
        return packageService.getAllPackage();
    }


    // get a pack
    @GetMapping("/{id}")
    public Optional<Package> getOnePackage(@PathVariable Integer id) {
        return packageService.getOnePackage(id);
    }


    // update a pack
    @PutMapping("/{id}")
    public ResponseEntity<String> updatePackage(@PathVariable Integer id, @RequestBody Package pkg) {
        if (packageService.updatePackage(pkg, id)) {
            return ResponseEntity.status(HttpStatus.OK).body("Package updated successfully");
        } else {
            return ResponseEntity.badRequest().body("Package not found or invalid data");
        }
    }


    // delete a pack
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePackageById(@PathVariable int id) {
        try {
            packageService.deletePackageById(id);
            return ResponseEntity.ok("Package deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete Package");
        }
    }


}


















//@DeleteMapping("/{id}")
//public ResponseEntity<String> deletePackageById(@PathVariable int id) {
//    try {
//        packageService.deletePackageById(id);
//        return ResponseEntity.ok("Package deleted successfully");
//    } catch (Exception e) {
//        e.printStackTrace();
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete Package");
//    }
//}