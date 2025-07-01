package org.skypro.skyshop.model.exeptions;

import java.util.Objects;

public final class ShopError {
    private final String code;
    private final String massage;

    public ShopError(String code, String massage) {
        this.code = Objects.requireNonNull(code);
        this.massage = Objects.requireNonNull(massage);
    }

    public String getCode() {
        return code;
    }

    public String getMassage() {
        return massage;
    }
}
