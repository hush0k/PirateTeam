package com.hush0k.pirateTeam.market.dto.response;

public record ReceiptDto(
       String name,
       int quantity,
       int totalSum,
       boolean isSuccess
) {}
