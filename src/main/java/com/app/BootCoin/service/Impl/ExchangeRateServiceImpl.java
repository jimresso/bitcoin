package com.app.BootCoin.service.Impl;

import com.app.BootCoin.model.ExchangeRate;
import com.app.BootCoin.repository.ExchangeRateRepository;
import com.app.BootCoin.service.ExchangeRateService;
import io.reactivex.rxjava3.core.Single;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class ExchangeRateServiceImpl implements ExchangeRateService {
  private final ExchangeRateRepository repo;
  private final ReactiveRedisTemplate<String, ExchangeRate> redis;

  private static final String KEY = "bootcoin:exchangeRate";

  @Override
  public Single<ExchangeRate> setRate(double rateBuy, double rateSell) {
    ExchangeRate rate = ExchangeRate.builder()
            .buyRate(rateBuy)
            .sellRate(rateSell)
            .updatedAt(LocalDateTime.now())
            .build();

    return repo.saveRateRx(rate)
            .flatMap(saved ->
                    Single.fromPublisher(redis.opsForValue().set(KEY, saved))
                            .flatMap(success -> Single.just(saved))
            );
  }

  @Override
  public Single<ExchangeRate> getRate() {
    return Single.fromPublisher(redis.opsForValue().get(KEY))
            .flatMap(cached -> {
              if (cached != null) {
                return Single.just(cached);
              }
              return repo.findLatestRateRx()
                      .toSingle()
                      .flatMap(rate ->
                              Single.fromPublisher(redis.opsForValue().set(KEY, rate))
                                      .flatMap(ignored -> Single.just(rate))
                      );
            });
  }
}
