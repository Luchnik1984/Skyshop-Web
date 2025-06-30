package org.skypro.skyshop.model.exeptions;

import java.util.UUID;

public class NoSuchProductException extends RuntimeException{
    private final UUID productId;
    public NoSuchProductException( UUID productId1) {
               this.productId = productId1;
    }
    public UUID getProductId(){
        return productId;
    }

}
