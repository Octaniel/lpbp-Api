package com.bsoftware.lpbp.repository.filter;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
public class PresencaFilter {
    @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime localDateTime;
    @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime localDateTime2;
}
