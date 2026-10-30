package com.example.procurement.assistant;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/vendors")
public class VendorController {

    private final VendorRepository repository;

    public VendorController(VendorRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public Vendor addVendor(@RequestBody Vendor vendor) {
        return repository.save(vendor);
    }

    @GetMapping
    public List<Vendor> getAllVendors() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Vendor getVendor(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vendor not found"));
    }

    @PutMapping("/{id}")
    public Vendor updateVendor(@PathVariable Long id, @RequestBody Vendor updated) {
        Vendor vendor = getVendor(id);
        vendor.setName(updated.getName());
        vendor.setEmail(updated.getEmail());
        return repository.save(vendor);
    }

    @DeleteMapping("/{id}")
    public void deleteVendor(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
