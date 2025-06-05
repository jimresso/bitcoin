package com.app.BootCoin.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import com.app.BootCoin.model.ExchangeRate;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.InsertOneResult;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import com.mongodb.reactivestreams.client.MongoCollection;

import java.time.LocalDateTime;

@Repository
public class ExchangeRateRepository {

  private final MongoCollection<Document> exchangeRateCollection;

  public ExchangeRateRepository(
          @Qualifier("exchangeRateCollection") MongoCollection<Document> exchangeRateCollection
  ) {
    this.exchangeRateCollection = exchangeRateCollection;
  }
  private Document toDocument(ExchangeRate r) {
    Document d = new Document()
            .append("rateBuy", r.getBuyRate())
            .append("rateSell", r.getSellRate())
            .append("updatedAt", r.getUpdatedAt());
    if (r.getId() != null) {
      d.append("_id", new ObjectId(r.getId()));
    }
    return d;
  }

  private ExchangeRate toExchangeRate(Document d) {
    return ExchangeRate.builder()
            .id(d.getObjectId("_id").toHexString())
            .buyRate(d.getDouble("rateBuy"))
            .updatedAt(LocalDateTime.from(d.getDate("updatedAt").toInstant()))
            .build();
  }

  public Maybe<ExchangeRate> findLatestRateRx() {
    return Maybe.create(emitter -> {
      exchangeRateCollection.find()
              .sort(Sorts.descending("updatedAt"))
              .limit(1)
              .subscribe(new Subscriber<Document>() {
                private Subscription s;
                @Override public void onSubscribe(Subscription s) {
                  this.s = s; s.request(1);
                }
                @Override public void onNext(Document d) {
                  emitter.onSuccess(toExchangeRate(d));
                  s.cancel();
                }
                @Override public void onError(Throwable t) {
                  emitter.onError(t);
                }
                @Override public void onComplete() {
                  emitter.onComplete();
                }
              });
    });
  }

  public Single<ExchangeRate> saveRateRx(ExchangeRate rate) {
    Document d = toDocument(rate);
    return Single.create(emitter ->
            exchangeRateCollection.insertOne(d)
                    .subscribe(new Subscriber<InsertOneResult>() {
                      private Subscription s;
                      @Override public void onSubscribe(Subscription s) {
                        this.s = s; s.request(1);
                      }
                      @Override public void onNext(InsertOneResult r) {
                      }
                      @Override public void onError(Throwable t) {
                        emitter.onError(t);
                      }
                      @Override public void onComplete() {
                        rate.setId(d.getObjectId("_id").toHexString());
                        emitter.onSuccess(rate);
                      }
                    })
    );
  }
}
