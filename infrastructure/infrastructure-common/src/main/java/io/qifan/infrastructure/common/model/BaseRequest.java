package io.qifan.infrastructure.common.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BaseRequest implements Request {
    private String id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Integer version;
}
