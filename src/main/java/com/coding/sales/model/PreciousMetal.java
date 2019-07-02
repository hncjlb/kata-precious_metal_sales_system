package com.coding.sales.model;

import java.util.List;

/**
 * 贵金属商品
 */
public class PreciousMetal {

    /**
     * 贵金属名称
     */
    private String name;
    /**
     * 贵金属编号
     */
    private String id;
    /**
     * 单位
     */
    private String unit;
    /**
     * 价格
     */
    private float price;
    /**
     * 可使用优惠券
     */
    private List<String> discountCoupons;
    /**
     * 可参与的促销活动
     */
    private List<String> promotions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public List<String> getDiscountCoupons() {
        return discountCoupons;
    }

    public void setDiscountCoupons(List<String> discountCoupons) {
        this.discountCoupons = discountCoupons;
    }

    public List<String> getPromotions() {
        return promotions;
    }

    public void setPromotions(List<String> promotions) {
        this.promotions = promotions;
    }
}
