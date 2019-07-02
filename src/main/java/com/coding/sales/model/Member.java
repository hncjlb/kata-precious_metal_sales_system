package com.coding.sales.model;

/**
 * 会员
 */
public class Member {

    /**
     * 会员姓名
     */
    private String name;
    /**
     * 会员等级
     */
    private String level;
    /**
     * 会员卡号
     */
    private String cardNo;
    /**
     * 会员积分
     */
    private String integral;

    public Member(String name, String level, String cardNo, String integral) {
        this.name = name;
        this.level = level;
        this.cardNo = cardNo;
        this.integral = integral;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }
}
