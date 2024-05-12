package org.erensekkeli.chatbotservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.erensekkeli.chatbotservice.controller.contract.CustomerControllerContract;
import org.erensekkeli.chatbotservice.dto.CustomerDTO;
import org.erensekkeli.chatbotservice.dto.CustomerSaveRequest;
import org.erensekkeli.chatbotservice.general.RestResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customer")
@Validated
@Slf4j
public class CustomerController {

    private final CustomerControllerContract customerControllerContract;

    public CustomerController(CustomerControllerContract customerControllerContract) {
        this.customerControllerContract = customerControllerContract;
    }

    @GetMapping
    @Operation(summary = "Get All Customers", description = "Retrieves all active customers")
    public ResponseEntity<RestResponse<List<CustomerDTO>>> getCustomer() {
        List<CustomerDTO> allCustomers = customerControllerContract.getAllCustomers();
        return ResponseEntity.ok(RestResponse.of(allCustomers));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<CustomerDTO>> getCustomerById(@PathVariable @Positive Long id) {
        CustomerDTO customerById = customerControllerContract.getCustomerById(id);
        return ResponseEntity.ok(RestResponse.of(customerById));
    }

    @PostMapping
    @Operation(summary = "Save Customer", description = "Saves a new customer")
    public ResponseEntity<RestResponse<CustomerDTO>> saveCustomer(@Valid @RequestBody CustomerSaveRequest request) {
        CustomerDTO savedCustomer = customerControllerContract.saveCustomer(request);
        return ResponseEntity.ok(RestResponse.of(savedCustomer));
    }
}