package shop.service.impl;

import org.springframework.stereotype.Service;
import shop.model.ShoppingCart;
import shop.model.products.FactoryProduct;
import shop.service.ShoppingCartService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private List<ShoppingCart> shoppingCarts;

    public ShoppingCartServiceImpl() {
        shoppingCarts = new ArrayList<>();

        shoppingCarts.add(new ShoppingCart("Alexander",
                FactoryProduct.create("milk", 22.35),
                LocalDate.of(2020 , 05 , 2)));

        shoppingCarts.add(new ShoppingCart("Natalia",
                FactoryProduct.create("butter", 45),
                LocalDate.of(2020 , 06 , 2)));

        shoppingCarts.add(new ShoppingCart("Alexander",
                FactoryProduct.create("bread", 20.11),
                LocalDate.of(2020 , 06 , 3)));

        shoppingCarts.add(new ShoppingCart("Alexander",
                FactoryProduct.create("milk", 33.50),
                LocalDate.of(2020 , 05 , 2)));
    }

    @Override
    public List<ShoppingCart> query(String owner) {
        if (owner == null) {
            throw new IllegalArgumentException("this null");
        }
        List<ShoppingCart> result = new ArrayList<>();
        for (ShoppingCart cart : shoppingCarts) {
            if (cart.getOwner().equals(owner)) {
                result.add(cart);
            }
        }
        return result;

    }

    @Override
    public double totalValue(String owner) {
        if (owner == null) {
            throw new IllegalArgumentException("this null");
        }
        double amount = 0;
        for (ShoppingCart cart : shoppingCarts) {
            if (cart.getOwner().equals(owner)) {
                amount += cart.getProduct().getPrice();
            }
        }
        return amount;
    }

    @Override
    public List<ShoppingCart> findByDate(LocalDate date) {
        return shoppingCarts
                .stream()
                .filter(cart -> Objects.equals(cart.getDate(), date))
                .collect(Collectors.toList());
    }


}