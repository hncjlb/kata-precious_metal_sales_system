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
     * @param receivables
     */
    public void computeMemberIntegral(OrderRepresentation orderRepresentation, Member member, BigDecimal receivables) {
        int increaseIntegral = getOrderIncreaseIntegral(member, receivables);
        int cumulativeIntegral = getOrderCumulativeIntegral(member, increaseIntegral);
        orderRepresentation.setMemberPointsIncreased(increaseIntegral);
        orderRepresentation.setMemberPoints(cumulativeIntegral);
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
     * @param receivables
     * @return
     */
    public int getOrderIncreaseIntegral(Member member, BigDecimal receivables) {
        String memberIntegralBenchmark = getMemberIntegralBenchmark(member);
        BigDecimal benchmarkBig = new BigDecimal(memberIntegralBenchmark);
        BigDecimal increaseIntegralBig = receivables.multiply(benchmarkBig);
        return increaseIntegralBig.setScale(0, BigDecimal.ROUND_UP).intValue();
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
