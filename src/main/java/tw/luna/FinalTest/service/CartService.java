package tw.luna.FinalTest.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.luna.FinalTest.dto.cart.CartInsertDto;
import tw.luna.FinalTest.dto.cart.CartSelectDto;
import tw.luna.FinalTest.model.*;
import tw.luna.FinalTest.repository.*;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    CartItemsRepository cartItemsRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    CouponService couponService;

    @Transactional
    public void addToCart(CartInsertDto cartInsertDto, Long userId) {
        Cart cart = cartRepository.findByUsersUserId(userId);
        if (cart == null) {
            // 如果購物車不存在，創建新的購物車
            cart = new Cart();
            cart.setUsers(usersRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("用戶不存在")));
            cartRepository.save(cart); // 保存新購物車
        }
        Product product = productRepository.findProductByName(cartInsertDto.getProductName());

        CartItems cartItem = cartItemsRepository.findByCartCartIdAndProductName(cart.getCartId(), cartInsertDto.getProductName());

        if (cartItem == null) {
            // Product doesn't exist in the cart, add new item
            cartItem = new CartItems();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            if (cartInsertDto.getQuantity() <= product.getStockQuantity()) {
                cartItem.setQuantity(Math.max(cartInsertDto.getQuantity(), 1));
            }
            cartItem.setPrice(product.getPrice());
        } else {
            // Product exists in the cart, update quantity
            if (cartInsertDto.getQuantity() + cartItem.getQuantity() <= product.getStockQuantity()) {
                cartItem.setQuantity(cartItem.getQuantity() + Math.max(cartInsertDto.getQuantity(), 1));
            }
        }
        cartItemsRepository.save(cartItem);
    }

    // 根據用戶 ID 獲取該用戶的購物車項目
    public List<CartSelectDto> getCartItemsByUserId(Long userId) {
        Cart cart = cartRepository.findByUsersUserId(userId);
        List<CartItems> cartItems = cart.getCartItems();
        List<CartSelectDto> result = new ArrayList<>(cartItems.size());

        for (CartItems cartItem : cartItems) {
            Product product = cartItem.getProduct();
            Optional<ProductImage> firstImage = product.getProductImages().stream().findFirst();
            String imageUrl = firstImage.get().getImage();
            // 使用 imageUrl
            CartSelectDto dto = new CartSelectDto(
                    cartItem.getCartitemsId(),
                    cartItem.getPrice(),
                    cartItem.getQuantity(),
                    product.getName(),
                    imageUrl,
                    product.getStockQuantity() // 新增
            );
            result.add(dto);
        }
        return result;
    }

    // 將商品加入購物車 (新增或修改數量)
    @Transactional
    public void updateCart(CartInsertDto cartInsertDto, Long userId) {
        Cart cart = cartRepository.findByUsersUserId(userId);
        if (cart == null) {
            // 如果購物車不存在，創建新的購物車
            cart = new Cart();
            cart.setUsers(usersRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("用戶不存在")));
            cartRepository.save(cart); // 保存新購物車
        }
        Product product = productRepository.findProductByName(cartInsertDto.getProductName());
        CartItems isPresent = cartItemsRepository.findByCartCartIdAndProductName(cart.getCartId(), cartInsertDto.getProductName());
        if (isPresent == null) { // 購物車內目前不存在該商品 -> 新增
            CartItems cartItems = new CartItems();
            cartItems.setCart(cart);
            cartItems.setProduct(product);
            cartItems.setQuantity(Math.max(cartInsertDto.getQuantity(), 1));
            // 單價
            cartItems.setPrice(product.getPrice());
            cartItemsRepository.save(cartItems);
        } else { // 購物車內已存在該商品 -> 更新數量
            isPresent.setQuantity(Math.max(cartInsertDto.getQuantity(), 1));
            cartItemsRepository.save(isPresent);
        }
    }

    // 清空購物車內的所有商品
    @Transactional
    public void deleteAllCartItems(Long userId) {
        Cart cart = cartRepository.findByUsersUserId(userId);
        cartItemsRepository.deleteByCartCartId(cart.getCartId());
    }

    // 刪除購物車內某項商品
    @Transactional
    public void deleteCartItemsByProductId(Long userId, String name) {
        // 找到該用戶的購物車
        Cart cart = cartRepository.findByUsersUserId(userId);
        // 找到購物車中的該商品
        CartItems isPresent = cartItemsRepository.findByCartCartIdAndProductName(cart.getCartId(), name);

        if (isPresent != null) { // 商品存在
            // 刪除商品
            cartItemsRepository.delete(isPresent);
        } else {
            throw new RuntimeException("購物車內無此商品");
        }
    }

    public Cart getCartIdByUserId(Long userId) {
        return cartRepository.findByUsersUserId(userId);
    }

    public int calculateTotalAmount(Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new IllegalArgumentException("Cart not found"));
        return cart.getCartItems().stream()
                .mapToInt(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
}