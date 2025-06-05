package com.app.BootCoin.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BootCoinExchangeRequest {
  @Id
  private String id;

  @NotNull
  private DocType    docType;

  @NotBlank
  private String     docNumber;

  @NotBlank
  @Pattern(regexp = "\\d{7,15}")
  private String     phoneNumber;

  @NotBlank @Email
  private String     email;

  @Positive
  private double     amount;

  @NotNull
  private PaymentMode paymentMode;

  private Status      status;
  private LocalDateTime createdAt;
  private LocalDateTime acceptedAt;

  public enum DocType       { DNI, CEX, PASAPORTE }
  public enum PaymentMode   { YANKI, TRANSFER }
  public enum Status        { PENDING, ACCEPTED }

}
