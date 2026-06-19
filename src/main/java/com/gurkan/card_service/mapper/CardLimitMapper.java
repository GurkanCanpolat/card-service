package com.gurkan.card_service.mapper;

import com.gurkan.card_service.dto.CardLimitResponse;
import com.gurkan.card_service.entity.CardLimit;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardLimitMapper {
    CardLimitResponse toResponse(CardLimit cardLimit);
}
