package com.example.procurement.assistant;

import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    private final InvoiceRepository repository;

    public InvoiceController(InvoiceRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public Invoice addInvoice(@RequestBody Invoice invoice) {
        return repository.save(invoice);
    }

    @GetMapping
    public List<Invoice> getAllInvoices() {
        return repository.findAll();
    }

    @GetMapping("/vendor/{vendorId}")
    public List<Invoice> getByVendor(@PathVariable Long vendorId) {
        return repository.findByVendorId(vendorId);
    }
}