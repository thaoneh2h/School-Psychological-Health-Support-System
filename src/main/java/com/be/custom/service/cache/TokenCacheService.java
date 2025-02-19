package com.be.custom.service.cache;

import com.be.custom.dto.cache.TokenDto;
import com.be.custom.dto.cache.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TokenCacheService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public String generateToken() {
        String uuid = UUID.randomUUID().toString();
        String timeNow = String.valueOf(System.currentTimeMillis());
        return uuid + timeNow;
    }

    public void updateCache(TokenDto token) {
        log.info("Update token temp cache");
        Gson gson = new Gson();
        if (TypeToken.ACCESS_TOKEN == token.getTypeToken()) {
            redisTemplate.opsForValue().set(token.getToken(), gson.toJson(token), 1, TimeUnit.DAYS);
        } else {
            redisTemplate.opsForValue().set(token.getToken(), gson.toJson(token), 30, TimeUnit.DAYS);
        }
    }

    public Optional<TokenDto> getTokenFromCache(String token, TypeToken typeToken) {
        if (token == null) {
            return Optional.empty();
        }
        TokenDto tokenDto = null;
        Object json = redisTemplate.opsForValue().get(token);
        if (json != null) {
            try {
                tokenDto = new Gson().fromJson(String.valueOf(json), TokenDto.class);
                if (!tokenDto.getTypeToken().equals(typeToken)) {
                    return Optional.empty();
                }
            } catch (JsonSyntaxException e) {
                log.error("Json is wrong format!. Json is: {}", json);
            }
        }
        if (tokenDto == null) {
            return Optional.empty();
        } else {
            return Optional.of(tokenDto);
        }
    }


}
