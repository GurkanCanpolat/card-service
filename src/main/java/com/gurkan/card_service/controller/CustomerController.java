package com.gurkan.card_service.controller;

import com.gurkan.card_service.dto.CardResponse;
import com.gurkan.card_service.dto.CreateCustomerRequest;
import com.gurkan.card_service.dto.CustomerResponse;
import com.gurkan.card_service.service.CardService;
import com.gurkan.card_service.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/customers")
public class CustomerController {
    private  final CustomerService customerService;
    private final CardService cardService;
    @PostMapping
    @ResponseStatus (HttpStatus.CREATED)
    public CustomerResponse createCustomer(@Valid @RequestBody CreateCustomerRequest createCustomerRequest) {
        return customerService.createCustomer(createCustomerRequest);
    }

    @GetMapping("/{customerId}/cards")
    public List<CardResponse> getCardsByCustomerId(@PathVariable UUID customerId) {
        return cardService.getCardsByCustomerId(customerId);
    }
}
