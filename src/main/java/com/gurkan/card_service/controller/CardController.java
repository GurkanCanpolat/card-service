package com.gurkan.card_service.controller;

import com.gurkan.card_service.dto.*;
import com.gurkan.card_service.service.CardLimitService;
import com.gurkan.card_service.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;
    private final CardLimitService cardLimitService;

    @Operation(
            summary = "Create a new card",
            description = "Creates a new card for a customer"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Card created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CardResponse createCard(@Valid @RequestBody CreateCardRequest request) {
        return cardService.createCard(request);
    }

    @GetMapping("/{id}")
    public CardResponse getCardById(@PathVariable UUID id) {
        return cardService.getCardById(id);
    }

    @GetMapping("/customer/{customerId}/active-cards")
    public List<CardResponse> getActiveCardsByCustomerId(@PathVariable UUID customerId) {
        return cardService.getActiveCardsByCustomerId(customerId);
    }

    @GetMapping("/customer/{customerId}/active")
    public List<CardResponse> getCardsByCustomerIdAndStatuses(@PathVariable UUID customerId) {
        return cardService.getCardsByCustomerIdAndStatuses(customerId);
    }

    @PatchMapping("/{cardId}/status")
    public CardResponse updateCardStatus(@PathVariable UUID cardId, @RequestBody UpdateCardStatusRequest request) {
        return cardService.updateCardStatus(cardId, request);
    }
    @PatchMapping("/{cardId}/limit")
    public CardLimitResponse updateCardLimit(
            @PathVariable UUID cardId,
            @Valid @RequestBody UpdateCardLimitRequest request
    ) {
        return cardLimitService.updateCardLimit(cardId, request);
    }

    @GetMapping("/{cardId}/limit")
    public CardLimitResponse getCardLimit(@PathVariable UUID cardId) {
        return cardLimitService.getCardLimit(cardId);
    }
}
