package com.app.BootCoin.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class BootCoinWallet {
  @Id
  private String id;
  @NotNull
  private DocType docType;
  @NotBlank
  @Size(min = 4, max = 20)
  private String docNumber;
  @NotBlank @Pattern(regexp = "\\d{7,15}")
  private String phoneNumber;
  @NotBlank @Email
  private String email;
  public enum DocType { DNI, CEX, PASAPORTE }

}