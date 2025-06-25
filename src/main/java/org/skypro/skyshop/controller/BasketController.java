package org.skypro.skyshop.controller;

import org.skypro.skyshop.model.basket.UserBasket;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.service.BasketService;
import org.skypro.skyshop.service.StorageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/basket")
public class BasketController {
    private final BasketService basketService;
    private final StorageService storageService;

    public BasketController(BasketService basketService, StorageService storageService) {
        this.basketService = basketService;
        this.storageService =storageService;
    }
    @GetMapping("/{id}")
    public String addProduct(@PathVariable("id") UUID id){
        basketService.addToBasket(id);
        Product product = storageService.getProductById(id)
                .orElseThrow();
        return "Продукт "+ product.getProductName() +" успешно добавлен.";
    }

    @GetMapping
    public UserBasket getUserBasket(){
        return basketService.getUserBasket();
    }
}
