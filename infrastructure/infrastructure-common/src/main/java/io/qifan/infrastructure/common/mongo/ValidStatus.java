package io.qifan.infrastructure.common.mongo;



import io.qifan.infrastructure.common.constants.BaseEnum;

import java.util.Arrays;

public enum ValidStatus implements BaseEnum {
    /**
     * 有效
     */
    VALID(1, "valid"),
    /**
     * 无效
     */
    INVALID(0, "invalid");
    private final Integer code;
    private final String name;

    ValidStatus(Integer code, String msg) {
        this.code = code;
        this.name = msg;
    }

    public static ValidStatus nameOf(String name) {
        return Arrays.stream(ValidStatus.values()).filter(type -> type.getName().equals(name))
                     .findFirst()
                     .orElseThrow(() -> new RuntimeException("枚举不存在"));
    }

    public static ValidStatus codeOf(Integer code) {
        return Arrays.stream(ValidStatus.values()).filter(type -> type.getCode().equals(code))
                     .findFirst()
                     .orElseThrow(() -> new RuntimeException("枚举不存在"));
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getName() {
        return name;
    }
}