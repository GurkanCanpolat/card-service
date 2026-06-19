package com.gurkan.card_service.service;

import com.gurkan.card_service.dto.CardLimitResponse;
import com.gurkan.card_service.dto.UpdateCardLimitRequest;
import com.gurkan.card_service.entity.Card;
import com.gurkan.card_service.entity.CardLimit;
import com.gurkan.card_service.exception.CardLimitNotFoundException;
import com.gurkan.card_service.exception.CardNotFoundException;
import com.gurkan.card_service.mapper.CardLimitMapper;
import com.gurkan.card_service.repository.CardLimitRepository;
import com.gurkan.card_service.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardLimitService {
    private final CardLimitRepository cardLimitRepository;
    private final CardRepository cardRepository;
    private final CardLimitMapper cardLimitMapper;

    @Transactional
    public CardLimitResponse updateCardLimit(     UUID cardId,
                                                  UpdateCardLimitRequest request){
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException(cardId));

        CardLimit cardLimit = cardLimitRepository.findByCardId(card.getId())
                .orElseThrow(() -> new CardLimitNotFoundException(card.getId()));

        cardLimit.setTotalLimit(request.totalLimit());
        cardLimit.setAvailableLimit(request.totalLimit());
        cardLimit.setUpdatedAt(Instant.now());

        CardLimit updatedLimit = cardLimitRepository.save(cardLimit);

        return cardLimitMapper.toResponse(updatedLimit);
    }

    public CardLimitResponse getCardLimit(UUID cardId) {
        cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException(cardId));

        CardLimit cardLimit = cardLimitRepository.findByCardId(cardId)
                .orElseThrow(() -> new CardLimitNotFoundException(cardId));

        return cardLimitMapper.toResponse(cardLimit);
    }
}
