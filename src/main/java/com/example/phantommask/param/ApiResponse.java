package com.example.phantommask.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T>  {
    private String msg;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Builder.Default
    private LocalDateTime time = LocalDateTime.now();
    private String code;
    private T data;
}
