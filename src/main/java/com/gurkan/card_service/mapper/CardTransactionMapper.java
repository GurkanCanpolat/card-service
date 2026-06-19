package com.gurkan.card_service.mapper;

import com.gurkan.card_service.dto.TransactionResponse;
import com.gurkan.card_service.entity.CardTransaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardTransactionMapper {
    TransactionResponse toResponse(CardTransaction transaction);
}
