package com.app.BootCoin.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;


import java.time.LocalDateTime;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class ExchangeRate {
  @Id
  private String id;
  private double buyRate;
  private double sellRate;
  private LocalDateTime updatedAt;
}