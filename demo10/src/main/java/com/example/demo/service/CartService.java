package com.example.demo.service;

import com.example.demo.models.*;
import com.example.demo.rep.CartItemRepository;
import com.example.demo.rep.CartRepository;

import com.example.demo.rep.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final BookService bookService;
    @Transactional
    public Cart getCartForUser(User user) {
        return cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });
    }

    @Transactional
    public void addItemToCart(User user, Long bookId, int quantity) {
        Cart cart = getCartForUser(user);
        Book book = bookService.getBookById(bookId);
        cart.addItem(book,quantity);
        cartRepository.save(cart);
    }

    @Transactional
    public void updateCartItemQuantity(Long itemId, int quantity) {
        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        cartItem.setQuantity(quantity); // Оновлюємо кількість
        cartItemRepository.save(cartItem); // Зберігаємо зміни
    }

    @Transactional
    public void removeItemFromCart(User user, Long bookId) {
        Cart cart = getCartForUser(user);
        cart.removeItem(bookId);
        cartRepository.save(cart);
    }

    @Transactional
    public void clearCart(User user) {
        Cart cart = getCartForUser(user);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Transactional
    public void submitCart(User user) {
        Cart cart = getCartForUser(user);

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDate.now());
        for (CartItem item : cart.getItems()) {
            item.setOrder(order);
        }
        List<CartItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cart.getItems()) {
            CartItem orderItem = new CartItem();
            orderItem.setBook(cartItem.getBook());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setOrder(order);
            orderItems.add(orderItem);
        }
        order.setItems(orderItems);
        order.setTotalPrice(orderItems.stream()
                .mapToInt(CartItem::getTotalPrice)
                .sum());

        orderRepository.save(order);

        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
