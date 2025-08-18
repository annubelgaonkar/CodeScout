package dev.anuradha.githubreposearcher.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RateLimitException extends RuntimeException{
    private final long retryAfterSeconds;

    public RateLimitException(String message, long retryAfterSeconds){
        super(message);
        this.retryAfterSeconds = retryAfterSeconds;
    }

}
