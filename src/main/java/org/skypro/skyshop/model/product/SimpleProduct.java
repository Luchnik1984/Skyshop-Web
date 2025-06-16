package org.skypro.skyshop.model.product;
import java.util.UUID;

public class SimpleProduct extends Product{
    public int costOfProduct;

    public SimpleProduct(String productName, int costOfProduct, UUID id) {
        super(productName, id);
        this.costOfProduct = costOfProduct;
               if (costOfProduct<=0){
            throw new IllegalArgumentException("Цена продукта не является положительным числом. Недопустимая цена!");
        }
    }


    @Override
    public int getCostOfProduct() {
        return costOfProduct;
    }

    @Override
    public String toString() {
        return "< продукт: "+super.getProductName()+  " > :" +
                " < стоимость: "+getCostOfProduct()+ " руб >";

    }

    @Override
    public boolean isSpecial() {
        return false;
    }
}
