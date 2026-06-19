package com.gurkan.card_service.mapper;

import com.gurkan.card_service.dto.CustomerResponse;
import com.gurkan.card_service.entity.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerResponse toResponse(Customer customer);
}
