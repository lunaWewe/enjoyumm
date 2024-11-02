package tw.luna.FinalTest.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import tw.luna.FinalTest.model.Coupon;
import tw.luna.FinalTest.model.UserCoupon;
import tw.luna.FinalTest.repository.CouponRepository;
import tw.luna.FinalTest.repository.UserCouponRepository;

@Service
public class CouponValidationService {

    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;

    public CouponValidationService(CouponRepository couponRepository, UserCouponRepository userCouponRepository) {
        this.couponRepository = couponRepository;
        this.userCouponRepository = userCouponRepository;
    }

    public Coupon validateCoupon(String code, long userId) {
        Coupon coupon = couponRepository.findCouponByCode(code);

        if (coupon == null) {
            throw new IllegalArgumentException("優惠券不存在");
        }

        if (!coupon.isActive()) {
            throw new IllegalArgumentException("優惠券已被禁用");
        }

        if (coupon.getExpiryDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("優惠券已過期");
        }

        UserCoupon userCoupon = userCouponRepository.findUserCouponByUserIdAndCouponId(userId, coupon.getCouponId());
        if (userCoupon == null) {
            throw new IllegalArgumentException("用戶沒有該優惠券");
        }

        if (userCoupon.isUsed()) {
            throw new IllegalArgumentException("優惠券已被使用");
        }

        return coupon;
    }

    public Coupon validateCouponForCart(String couponCode, Long cartId) {
        Coupon coupon = couponRepository.findCouponByCode(couponCode);
        if (coupon == null) {
            throw new RuntimeException("找不到優惠券");
        }

        UserCoupon userCoupon = userCouponRepository.findByUserIdAndCouponId(cartId, coupon.getCouponId());
        if (userCoupon == null) {
            throw new RuntimeException("用戶無此優惠券");
        } else if (userCoupon.isUsed()) {
            throw new RuntimeException("優惠券已被使用");
        }

        if (!coupon.isActive() || coupon.getExpiryDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("優惠券無效或已過期");
        }

        return coupon;
    }

}