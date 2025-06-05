package com.app.BootCoin.service;


import com.app.BootCoin.model.ExchangeRate;
import io.reactivex.rxjava3.core.Single;


public interface ExchangeRateService {
  Single<ExchangeRate> setRate(double rateBuy, double rateSell);

  Single <ExchangeRate> getRate();
}
