package io.qifan.infrastructure.common.exception;

import io.qifan.infrastructure.common.constants.BaseEnum;
import lombok.Data;

@Data
public class BusinessException extends RuntimeException {
    BaseEnum resultCode;

    public BusinessException(BaseEnum resultCode) {
        super(resultCode.getName());
        this.resultCode = resultCode;
    }

    public BusinessException(BaseEnum resultCode, String msg) {
        super(msg);
        this.resultCode = resultCode;
    }
}
