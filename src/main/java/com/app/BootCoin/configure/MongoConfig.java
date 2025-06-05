package com.app.BootCoin.configure;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoDatabase;
import com.mongodb.reactivestreams.client.MongoCollection;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {

  @Bean
  public MongoDatabase reactiveMongoDatabase(MongoClient client,
                                             @Value("${spring.data.mongodb.database}") String dbName) {
    return client.getDatabase(dbName);
  }

  @Bean
  @Qualifier("bootCoinWalletCollection")
  public MongoCollection<Document> bootCoinWalletCollection(MongoDatabase database) {
    return database.getCollection("wallets");
  }

  @Bean
  @Qualifier("exchangeRateCollection")
  public MongoCollection<Document> exchangeRateCollection(MongoDatabase database) {
    return database.getCollection("exchangeRate");
  }

  @Bean
  @Qualifier("bootCoinExchangeRequest")
  public MongoCollection<Document> bootCoinExchangeRequest(MongoDatabase database) {
    return database.getCollection("bootCoinExchangeRequest");
  }
}