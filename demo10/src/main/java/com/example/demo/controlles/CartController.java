package com.example.demo.controlles;

import com.example.demo.models.Cart;
import com.example.demo.models.User;
import com.example.demo.service.CartService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final UserService userService;


    @PostMapping("/cart/add/{bookId}")
    public String addToCart(@PathVariable Long bookId, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        cartService.addItemToCart(user, bookId, 1);
        return "redirect:/cart/view";
    }

    @GetMapping("/cart/view")
    public String viewCart(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        Cart cart = cartService.getCartForUser(user);
        model.addAttribute("cart", cart);
        model.addAttribute("totalPrice", cart.getTotalPrice());
        return "cart-view";
    }

    @PostMapping("/cart/remove/{id}")
    public String removeFromCart(@PathVariable Long id, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        cartService.removeItemFromCart(user, id);
        return "redirect:/cart/view";
    }

    @PostMapping("/cart/clear")
    public String clearCart(Principal principal) {
        User user = userService.findByEmail(principal.getName());
        cartService.clearCart(user);
        return "redirect:/cart/view";
    }

    @PostMapping("/cart/update/{itemId}")
    public String updateCartItem(@PathVariable Long itemId, @RequestParam int quantity) {
        cartService.updateCartItemQuantity(itemId, quantity);
        return "redirect:/cart/view";
    }

    @PostMapping("/cart/submit")
    public String submitCart(Principal principal) {
        User user = userService.findByEmail(principal.getName());
        cartService.submitCart(user);

        return "redirect:/";
    }
}