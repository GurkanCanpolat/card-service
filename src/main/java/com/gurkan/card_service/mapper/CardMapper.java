package com.gurkan.card_service.mapper;


import com.gurkan.card_service.dto.CardResponse;
import com.gurkan.card_service.entity.Card;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardMapper {
    CardResponse toResponse(Card card);
}
