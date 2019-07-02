package com.coding.sales.model;

/**
 * 促销活动
 */
public class Promotions {

    /**
     * 促销活动ID
     */
    private String id;
    /**
     * 促销活动类型
     */
    private String type;

    public Promotions(String id, String type) {
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
