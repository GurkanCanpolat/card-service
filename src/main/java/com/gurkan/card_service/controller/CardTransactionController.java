package com.gurkan.card_service.controller;

import com.gurkan.card_service.dto.CreateTransactionRequest;
import com.gurkan.card_service.dto.TransactionResponse;
import com.gurkan.card_service.service.CardTransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class CardTransactionController {
    private final CardTransactionService transactionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse createTransaction(
            @Valid @RequestBody CreateTransactionRequest request
    ) {
        return transactionService.createTransaction(request);
    }

    @GetMapping("/card/{cardId}")
    public List<TransactionResponse> getTransactionsByCardId(
            @PathVariable UUID cardId
    ) {
        return transactionService.getTransactionsByCardId(cardId);
    }

    @PostMapping("/{transactionId}/reverse")
    public TransactionResponse reverseTransaction(
            @PathVariable UUID transactionId
    ) {
        return transactionService.reverseTransaction(transactionId);
    }
}
