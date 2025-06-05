package com.app.BootCoin.repository;

import com.app.BootCoin.execption.BusinessException;
import com.app.BootCoin.model.BootCoinExchangeRequest;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.reactivestreams.client.MongoCollection;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class BootCoinExchangeRequestRepository {

  private final MongoCollection<Document> col;

  public BootCoinExchangeRequestRepository(
          @Qualifier("bootCoinExchangeRequest") MongoCollection<Document> col
  ) {
    this.col = col;
  }

  private Document toDocument(BootCoinExchangeRequest r) {
    var d = new Document()
            .append("docType",      r.getDocType().name())
            .append("docNumber",    r.getDocNumber())
            .append("phoneNumber",  r.getPhoneNumber())
            .append("email",        r.getEmail())
            .append("amount",       r.getAmount())
            .append("paymentMode",  r.getPaymentMode().name())
            .append("status",       r.getStatus().name())
            .append("createdAt",    r.getCreatedAt())
            .append("acceptedAt",   r.getAcceptedAt());
    if (r.getId() != null) d.append("_id", new ObjectId(r.getId()));
    return d;
  }

  private BootCoinExchangeRequest toRequest(Document d) {
    return BootCoinExchangeRequest.builder()
            .id(d.getObjectId("_id").toHexString())
            .docType( BootCoinExchangeRequest.DocType.valueOf(d.getString("docType")))
            .docNumber(d.getString("docNumber"))
            .phoneNumber(d.getString("phoneNumber"))
            .email(d.getString("email"))
            .amount(d.getDouble("amount"))
            .paymentMode(BootCoinExchangeRequest.PaymentMode.valueOf(d.getString("paymentMode")))
            .status(BootCoinExchangeRequest.Status.valueOf(d.getString("status")))
            .createdAt(d.get("createdAt", LocalDateTime.class))
            .acceptedAt(d.get("acceptedAt", LocalDateTime.class))
            .build();
  }

  public Single<BootCoinExchangeRequest> saveRx(BootCoinExchangeRequest r) {
    var doc = toDocument(r);
    return Single.<BootCoinExchangeRequest>create(emitter ->
            col.insertOne(doc).subscribe(new Subscriber<InsertOneResult>() {
              Subscription s;
              @Override public void onSubscribe(Subscription s) { this.s = s; s.request(1); }
              @Override public void onNext(InsertOneResult ir)       { /* no-op */ }
              @Override public void onError(Throwable t)             { emitter.onError(t); }
              @Override public void onComplete() {
                r.setId(doc.getObjectId("_id").toHexString());
                emitter.onSuccess(r);
              }
            })
    );
  }

  public Maybe<BootCoinExchangeRequest> findByIdRx(String id) {
    return Maybe.<BootCoinExchangeRequest>create(emitter ->
            col.find(Filters.eq("_id", new ObjectId(id)))
                    .limit(1)
                    .subscribe(new Subscriber<Document>() {
                      Subscription s;
                      @Override public void onSubscribe(Subscription s) { this.s = s; s.request(1); }
                      @Override public void onNext(Document d) { emitter.onSuccess(toRequest(d)); s.cancel(); }
                      @Override public void onError(Throwable t) { emitter.onError(t); }
                      @Override public void onComplete() { emitter.onComplete(); }
                    })
    );
  }

  public Single<BootCoinExchangeRequest> findByIdSingle(String id) {
    return Single.create(emitter ->
            col.find(Filters.eq("_id", new ObjectId(id)))
                    .limit(1)
                    .subscribe(new Subscriber<Document>() {
                      private Subscription s;
                      @Override public void onSubscribe(Subscription s) {
                        this.s = s;
                        s.request(1);
                      }
                      @Override public void onNext(Document d) {
                        emitter.onSuccess(toRequest(d));
                        s.cancel();
                      }
                      @Override public void onError(Throwable t) {
                        emitter.onError(t);
                      }
                      @Override public void onComplete() {
                        emitter.onError(new BusinessException("Request no encontrado"));
                      }
                    })
    );
  }
}