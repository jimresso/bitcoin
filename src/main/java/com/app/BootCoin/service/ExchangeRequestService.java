package com.app.BootCoin.service;

import com.app.BootCoin.model.BootCoinExchangeRequest;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

import java.util.Optional;

public interface ExchangeRequestService {
  Single<BootCoinExchangeRequest> createRequest(BootCoinExchangeRequest req);

  Single<BootCoinExchangeRequest> acceptRequest(String id);

}
