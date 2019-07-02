package com.coding.sales;

import com.coding.sales.model.Member;
import com.coding.sales.output.OrderRepresentation;

import java.math.BigDecimal;

/**
 * 用户积分
 */
public class MemberIntegral {


    /**
     * 计算用户积分
     *
     * @param orderRepresentation
     * @param member
     * @param orderPayMoney
     */
    public void computeMemberIntegral(OrderRepresentation orderRepresentation, Member member, String orderPayMoney) {
        int increaseIntegral = getOrderIncreaseIntegral(member, orderPayMoney);
        int cumulativeIntegral = getOrderCumulativeIntegral(member, increaseIntegral);
        orderRepresentation.setMemberPointsIncreased(increaseIntegral);
        orderRepresentation.setMemberPoints(cumulativeIntegral);

        //计算用户登陆
        MemberLevel memberLevel = new MemberLevel();
        memberLevel.computeMemberLevel(orderRepresentation, Integer.valueOf(member.getIntegral()), cumulativeIntegral);
    }

    /**
     * 计算订单共累计积分
     * 历史积分+增长积分
     *
     * @param member
     * @param increaseIntegral 增长积分
     * @return
     */
    public int getOrderCumulativeIntegral(Member member, int increaseIntegral) {
        return new BigDecimal(member.getIntegral()).add(new BigDecimal(increaseIntegral)).intValue();
    }

    /**
     * 获取当前订单增长积分
     * 支付金额 * 会员等级积分准则
     *
     * @param member
     * @param orderPayMoney
     * @return
     */
    public int getOrderIncreaseIntegral(Member member, String orderPayMoney) {
        String memberIntegralBenchmark = getMemberIntegralBenchmark(member);
        BigDecimal payMoneyBig = new BigDecimal(orderPayMoney);
        BigDecimal benchmarkBig = new BigDecimal(memberIntegralBenchmark);
        BigDecimal increaseIntegralBig = payMoneyBig.multiply(benchmarkBig);
        return increaseIntegralBig.setScale(0, BigDecimal.ROUND_DOWN).intValue();
    }

    /**
     * 获取会员积分等级准则
     *
     * @param member
     */
    public String getMemberIntegralBenchmark(Member member) {
        String level = member.getLevel();
        if (level.equals("普卡")) {
            return "1";
        } else if (level.equals("金卡")) {
            return "1.5";
        } else if (level.equals("白金卡")) {
            return "1.8";
        } else if (level.equals("钻石卡")) {
            return "2";
        }
        return "1";
    }

}
