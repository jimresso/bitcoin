package com.app.BootCoin.service;

import com.app.BootCoin.model.BootCoinWallet;
import io.reactivex.rxjava3.core.Single;
import org.springframework.http.ResponseEntity;

public interface BootCoinWalletService {
  Single<ResponseEntity<Void>> register(BootCoinWallet wallet);
}
