package com.example.procurement.assistant;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invoices")
public class InvoiceAiController {

    private final GeminiService gemini;
    private final VendorRepository vendorRepository;
    private final InvoiceRepository invoiceRepository;

    public InvoiceAiController(GeminiService gemini, VendorRepository vendorRepository,
                               InvoiceRepository invoiceRepository) {
        this.gemini = gemini;
        this.vendorRepository = vendorRepository;
        this.invoiceRepository = invoiceRepository;
    }

    @PostMapping("/extract")
    public Invoice extract(@RequestBody String invoiceText) {
        String prompt = """
            Extract details from this invoice text. Reply with exactly these 4 lines and nothing else:
            vendor: <vendor name>
            invoicenumber: <invoice number>
            amount: <total amount as a number only>
            duedate: <due date in YYYY-MM-DD format>

            Invoice text:
            """ + invoiceText;

        String aiText = gemini.ask(prompt);

        Map<String, String> fields = new HashMap<>();
        for (String line : aiText.split("\n")) {
            int i = line.indexOf(':');
            if (i > 0) {
                fields.put(line.substring(0, i).trim().toLowerCase(), line.substring(i + 1).trim());
            }
        }

        String vendorName = fields.get("vendor");
        Vendor vendor = vendorRepository.findFirstByName(vendorName).orElseGet(() -> {
            Vendor v = new Vendor();
            v.setName(vendorName);
            return vendorRepository.save(v);
        });

        Invoice invoice = new Invoice();
        invoice.setVendor(vendor);
        invoice.setInvoiceNumber(fields.get("invoicenumber"));
        invoice.setAmount(Double.parseDouble(fields.get("amount").replaceAll("[^0-9.]", "")));
        invoice.setDueDate(LocalDate.parse(fields.get("duedate")));
        return invoiceRepository.save(invoice);
    }
}
