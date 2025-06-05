package com.app.BootCoin.service.Impl;

import com.app.BootCoin.execption.BusinessException;
import com.app.BootCoin.model.BootCoinWallet;
import com.app.BootCoin.repository.BootCoinWalletRepository;
import com.app.BootCoin.service.BootCoinWalletService;
import io.reactivex.rxjava3.core.Single;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BootCoinWalletServiceImpl implements BootCoinWalletService {

  private final BootCoinWalletRepository repo;

  @Override
  public Single<ResponseEntity<Void>> register(BootCoinWallet wallet) {
    return repo.findByDocNumberRx(wallet.getDocNumber())
            .isEmpty()
            .flatMap(isEmpty -> {
              if (!isEmpty) {
                return Single.error(
                        new BusinessException("A wallet with that document already exists"));
              }
              return repo.saveRx(wallet)
                      .map(saved -> ResponseEntity.status(HttpStatus.CREATED).<Void>build());
            });
  }
}
