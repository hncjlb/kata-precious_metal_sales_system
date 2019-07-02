package com.coding.sales.model;

public class DiscountManager {
    public static final String DISCOUNT_9 = "9折券";
    public static final String DISCOUNT_95 = "95折券";

    public static float getDiscountRate(String discount) {
        if (DiscountManager.DISCOUNT_9.equals(discount)) {
            return 0.9f;
        } else if (DiscountManager.DISCOUNT_95.equals(discount)) {
            return 0.95f;
        } else {
            return 1f;
        }
    }
}
