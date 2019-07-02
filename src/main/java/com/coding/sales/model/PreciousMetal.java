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
    private String numbering;
    /**
     * 单位
     */
    private String unit;
    /**
     * 价格
     */
    private String price;
    /**
     * 可使用优惠券
     */
    private List<DiscountCoupon> discountCoupons;
    /**
     * 可参与的促销活动
     */
    private List<Promotions> promotions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumbering() {
        return numbering;
    }

    public void setNumbering(String numbering) {
        this.numbering = numbering;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public List<DiscountCoupon> getDiscountCoupons() {
        return discountCoupons;
    }

    public void setDiscountCoupons(List<DiscountCoupon> discountCoupons) {
        this.discountCoupons = discountCoupons;
    }

    public List<Promotions> getPromotions() {
        return promotions;
    }

    public void setPromotions(List<Promotions> promotions) {
        this.promotions = promotions;
    }
}
