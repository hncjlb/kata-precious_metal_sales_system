package com.coding.sales.model;

/**
 * 折扣券
 */
public class DiscountCoupon {

    /**
     * 折扣券ID
     */
    private String id;
    /**
     * 折扣券名称
     */
    private String name;

    public DiscountCoupon(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
