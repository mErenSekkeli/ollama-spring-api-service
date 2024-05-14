package org.erensekkeli.chatbotservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.erensekkeli.chatbotservice.controller.contract.CustomerControllerContract;
import org.erensekkeli.chatbotservice.dto.CustomerDTO;
import org.erensekkeli.chatbotservice.request.CustomerSaveRequest;
import org.erensekkeli.chatbotservice.request.CustomerUpdateRequest;
import org.erensekkeli.chatbotservice.general.RestResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customer")
@Validated
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

    @PutMapping("/{id}")
    @Operation(summary = "Update Customer", description = "Updates an existing customer")
    public ResponseEntity<RestResponse<CustomerDTO>> updateCustomer(@PathVariable @Positive Long id,
                                                                    @RequestBody CustomerUpdateRequest request) {
        return ResponseEntity.ok(RestResponse.of(customerControllerContract.updateCustomer(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Customer", description = "Deletes an existing customer")
    public ResponseEntity<RestResponse<String>> deleteCustomer(@PathVariable @Positive Long id) {
        customerControllerContract.deleteCustomer(id);
        return ResponseEntity.ok(RestResponse.of("Customer deleted successfully"));
    }

}
