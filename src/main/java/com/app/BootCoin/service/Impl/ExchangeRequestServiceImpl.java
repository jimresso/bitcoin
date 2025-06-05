package com.app.BootCoin.service.Impl;

import com.app.BootCoin.execption.BusinessException;
import com.app.BootCoin.model.BootCoinExchangeRequest;
import com.app.BootCoin.repository.BootCoinExchangeRequestRepository;
import com.app.BootCoin.service.ExchangeRequestService;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ExchangeRequestServiceImpl implements ExchangeRequestService {
  private final BootCoinExchangeRequestRepository repo;

  public Single<BootCoinExchangeRequest> createRequest(BootCoinExchangeRequest req) {
    req.setStatus(BootCoinExchangeRequest.Status.PENDING);
    req.setCreatedAt(LocalDateTime.now());
    return repo.saveRx(req);
  }

  public Maybe<BootCoinExchangeRequest> findById(String id) {
    return repo.findByIdRx(id);
  }

  public Single<BootCoinExchangeRequest> acceptRequest(String id) {
    return repo.findByIdSingle(id)
            .flatMap(r -> {
              r.setStatus(BootCoinExchangeRequest.Status.ACCEPTED);
              r.setAcceptedAt(LocalDateTime.now());
              return repo.saveRx(r);
            });
  }
}
