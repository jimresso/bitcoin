package com.app.BootCoin.configure;

import com.app.BootCoin.model.ExchangeRate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

  @Bean @Primary
  public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory() {
    return new LettuceConnectionFactory("localhost", 6379);
  }

  @Bean
  public ReactiveRedisTemplate<String, ExchangeRate> reactiveRedisTemplate(
          ReactiveRedisConnectionFactory factory,
          ObjectMapper objectMapper  // tu ObjectMapper ya “customizado” con JavaTimeModule
  ) {
    StringRedisSerializer keySer = new StringRedisSerializer();
    Jackson2JsonRedisSerializer valSer =
            new Jackson2JsonRedisSerializer<>(objectMapper, ExchangeRate.class);
    RedisSerializationContext.SerializationPair<String> keyPair =
            RedisSerializationContext.SerializationPair.fromSerializer(keySer);
    RedisSerializationContext.SerializationPair<ExchangeRate> valPair =
            RedisSerializationContext.SerializationPair.fromSerializer(valSer);

    RedisSerializationContext<String, ExchangeRate> ctx =
            RedisSerializationContext.<String, ExchangeRate>newSerializationContext(keyPair)
                    .key(keyPair)
                    .value(valPair)
                    .hashKey(keyPair)
                    .hashValue(valPair)
                    .build();

    return new ReactiveRedisTemplate<>(factory, ctx);
  }
}