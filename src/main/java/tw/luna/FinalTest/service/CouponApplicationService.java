package tw.luna.FinalTest.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.luna.FinalTest.model.Coupon;
import tw.luna.FinalTest.model.DiscountType;

@Service
public class CouponApplicationService {

    private final CouponValidationService couponValidationService;

    public CouponApplicationService(CouponValidationService couponValidationService) {
        this.couponValidationService = couponValidationService;
    }

    @Transactional
    public Map<String, Object> applyCouponToCart(Long cartId, String couponCode, int totalAmount) {
        Coupon coupon = couponValidationService.validateCouponForCart(couponCode, cartId);

        int percentageDiscount = 0;
        int amountDiscount = 0;
        int finalAmount = totalAmount;

        if (coupon.getDiscountType() == DiscountType.percentage) {
            percentageDiscount = (totalAmount * coupon.getDiscountValue()) / 100;
            finalAmount = totalAmount - percentageDiscount;
        } else if (coupon.getDiscountType() == DiscountType.amount) {
            amountDiscount = coupon.getDiscountValue();
            finalAmount = totalAmount - amountDiscount;
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "優惠券已成功應用");
        response.put("cartTotal", totalAmount);
        response.put("percentageDiscount", percentageDiscount);
        response.put("amountDiscount", amountDiscount);
        response.put("finalAmount", finalAmount);

        return response;
    }
}