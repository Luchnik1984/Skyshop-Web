package org.skypro.skyshop.controller;

import org.skypro.skyshop.model.exeptions.NoSuchProductException;
import org.skypro.skyshop.model.exeptions.ShopError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ShopControllerAdvice {

    @ExceptionHandler(NoSuchProductException.class)
    public ResponseEntity<ShopError> handleNoSuchProduct(NoSuchProductException ex){
        ShopError error = new ShopError(
                "PRODUCT_NOT_FOUND",
                "Товар с ID " + ex.getProductId() + " не найден в каталоге"
        );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }
}
