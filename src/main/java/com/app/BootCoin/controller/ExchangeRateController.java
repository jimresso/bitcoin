package com.app.BootCoin.controller;

import com.app.BootCoin.model.ExchangeRate;
import com.app.BootCoin.model.RateRequest;
import com.app.BootCoin.service.ExchangeRateService;
import io.reactivex.rxjava3.core.Single;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rates")
@RequiredArgsConstructor
public class ExchangeRateController {
  private final ExchangeRateService svc;

  @PostMapping
  public Single<ResponseEntity<Void>> setRate(@RequestBody RateRequest req) {
    return svc.setRate(req.getRateBuy(), req.getRateSell())
            .map(__ -> ResponseEntity.status(201).build());
  }

  @GetMapping
  public Single<ResponseEntity<ExchangeRate>> getRate() {
    return svc.getRate()
            .map(ResponseEntity::ok);
  }
}
