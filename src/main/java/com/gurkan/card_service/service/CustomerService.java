package com.gurkan.card_service.service;

import com.gurkan.card_service.dto.CardResponse;
import com.gurkan.card_service.dto.CreateCustomerRequest;
import com.gurkan.card_service.dto.CustomerResponse;
import com.gurkan.card_service.entity.Customer;
import com.gurkan.card_service.mapper.CustomerMapper;
import com.gurkan.card_service.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerResponse createCustomer(CreateCustomerRequest createCustomerRequest) {
        Customer customer = Customer.builder()
                .id(UUID.randomUUID())
                .fullName(createCustomerRequest.fullName())
                .email(createCustomerRequest.email())
                .phoneNumber(createCustomerRequest.phoneNumber())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toResponse(savedCustomer);
    }
}
