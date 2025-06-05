package com.app.BootCoin.repository;

import com.app.BootCoin.model.BootCoinWallet;
import com.mongodb.reactivestreams.client.MongoCollection;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;

@Repository
public class BootCoinWalletRepository {
  private final MongoCollection<Document> walletCollection;

  public BootCoinWalletRepository(
          @Qualifier("bootCoinWalletCollection") MongoCollection<Document> walletCollection
  ) {
    this.walletCollection = walletCollection;
  }

  private Document toDocument(BootCoinWallet w) {
    Document d = new Document()
            .append("docType", w.getDocType().name())
            .append("docNumber", w.getDocNumber())
            .append("phoneNumber", w.getPhoneNumber())
            .append("email", w.getEmail());
    if (w.getId() != null) {
      d.append("_id", new ObjectId(w.getId()));
    }
    return d;
  }

  private BootCoinWallet toWallet(Document d) {
    return BootCoinWallet.builder()
            .id(d.getObjectId("_id").toHexString())
            .docType(BootCoinWallet.DocType.valueOf(d.getString("docType")))
            .docNumber(d.getString("docNumber"))
            .phoneNumber(d.getString("phoneNumber"))
            .email(d.getString("email"))
            .build();
  }

  public Maybe<BootCoinWallet> findByDocNumberRx(final String docNumber) {
    return Maybe.create(emitter -> {
      walletCollection.find(Filters.eq("docNumber", docNumber))
              .limit(1)
              .subscribe(new Subscriber<Document>() {
                private Subscription s;
                @Override public void onSubscribe(Subscription s) {
                  this.s = s; s.request(1);
                }
                @Override public void onNext(Document doc) {
                  emitter.onSuccess(toWallet(doc));
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

  public Single<BootCoinWallet> saveRx(final BootCoinWallet w) {
    Document doc = toDocument(w);
    return Single.create(emitter ->
            walletCollection.insertOne(doc)
                    .subscribe(new Subscriber<InsertOneResult>() {
                      private Subscription s;
                      @Override public void onSubscribe(Subscription s) {
                        this.s = s; s.request(1);
                      }
                      @Override public void onNext(InsertOneResult r) { /* nothing */ }
                      @Override public void onError(Throwable t) {
                        emitter.onError(t);
                      }
                      @Override public void onComplete() {
                        // rellenamos el id recién generado
                        w.setId(doc.getObjectId("_id").toHexString());
                        emitter.onSuccess(w);
                      }
                    })
    );
  }


}
