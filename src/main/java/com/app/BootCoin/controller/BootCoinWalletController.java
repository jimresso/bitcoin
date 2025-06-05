package com.app.BootCoin.controller;


import com.app.BootCoin.model.BootCoinWallet;
import com.app.BootCoin.service.BootCoinWalletService;
import io.reactivex.rxjava3.core.Single;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bootcoin/wallets")
@RequiredArgsConstructor
public class BootCoinWalletController {

  private final BootCoinWalletService service;

  @PostMapping(value = "/register")
  public Single<ResponseEntity<Void>> register( @Valid @RequestBody BootCoinWallet wallet) {

    return service.register(wallet);
  }


}
