package com.app.BootCoin.controller;

import com.app.BootCoin.model.BootCoinExchangeRequest;
import com.app.BootCoin.service.ExchangeRequestService;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/exchange")
@RequiredArgsConstructor
public class ExchangeRequestController {
  private final ExchangeRequestService svc;

  @PostMapping
  public Single<ResponseEntity<BootCoinExchangeRequest>> create(
          @RequestBody BootCoinExchangeRequest req
  ) {
    return svc.createRequest(req)
            .map(r -> ResponseEntity.status(201).body(r));
  }

  @PutMapping("/{id}/accept")
  public Single<ResponseEntity<BootCoinExchangeRequest>> accept(
          @PathVariable String id
  ) {
    return svc.acceptRequest(id)
            .map(ResponseEntity::ok);
  }
}