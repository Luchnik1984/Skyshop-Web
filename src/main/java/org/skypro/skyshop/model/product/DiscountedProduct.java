package org.skypro.skyshop.model.product;

import java.util.UUID;

public class DiscountedProduct extends Product {
    public int basePrice;
    public int discountInWholePercentages;


    public DiscountedProduct(UUID id, String productName, int basePrice, int discountInWholePercentages) {
        super(id, productName);
        if (basePrice <= 0) {
            throw new IllegalArgumentException("Цена продукта не является положительным числом. Недопустимая цена!");
        }
        if (discountInWholePercentages < 0 || discountInWholePercentages > 100) {
            throw new IllegalArgumentException("Введён недопустимый процент скидки!");
        }
        this.basePrice = basePrice;
        this.discountInWholePercentages = discountInWholePercentages;
    }

    @Override
    public int getCostOfProduct() {
        return (int) (basePrice * (1 - (discountInWholePercentages / 100D)));
    }

    @Override
    public String toString() {
        return
                "< продукт со скидкой: " + super.getProductName() + " > :" +
                        " < стоимость: " + getCostOfProduct() + " руб >" +
                        " (< скидка " + discountInWholePercentages + " >%)";
    }

    @Override
    public boolean isSpecial() {
        return true;
    }
}
