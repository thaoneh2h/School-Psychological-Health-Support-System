package com.be.custom.service.cache;

import com.be.custom.dto.cache.TokenDto;
import com.be.custom.dto.cache.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TokenCacheService {
    private final Map<String, String> tokenCache = new ConcurrentHashMap<>();

    public String generateToken() {
        String uuid = UUID.randomUUID().toString();
        String timeNow = String.valueOf(System.currentTimeMillis());
        return uuid + timeNow;
    }

    public void updateCache(TokenDto token) {
        log.info("Update token temp cache");
        Gson gson = new Gson();
        String tokenJson = gson.toJson(token);
        
        tokenCache.put(token.getToken(), tokenJson);
        
        if (TypeToken.ACCESS_TOKEN == token.getTypeToken()) {
            scheduleTokenRemoval(token.getToken(), 1, TimeUnit.DAYS);
        } else {
            scheduleTokenRemoval(token.getToken(), 30, TimeUnit.DAYS);
        }
    }

    public Optional<TokenDto> getTokenFromCache(String token, TypeToken typeToken) {
        if (token == null) {
            return Optional.empty();
        }

        String tokenJson = tokenCache.get(token);
        if (tokenJson != null) {
            try {
                TokenDto tokenDto = new Gson().fromJson(tokenJson, TokenDto.class);
                if (!tokenDto.getTypeToken().equals(typeToken)) {
                    return Optional.empty();
                }
                return Optional.of(tokenDto);
            } catch (JsonSyntaxException e) {
                log.error("Json is wrong format!. Json is: {}", tokenJson);
            }
        }
        return Optional.empty();
    }

    private void scheduleTokenRemoval(String token, long delay, TimeUnit timeUnit) {
        new Thread(() -> {
            try {
                Thread.sleep(timeUnit.toMillis(delay));
                tokenCache.remove(token);
                log.info("Token {} has been removed from cache.", token);
            } catch (InterruptedException e) {
                log.error("Error while removing token from cache: {}", e.getMessage());
            }
        }).start();
    }
}