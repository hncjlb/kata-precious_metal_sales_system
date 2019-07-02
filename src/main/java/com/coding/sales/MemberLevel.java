package com.coding.sales;

import com.coding.sales.output.OrderRepresentation;

/**
 * 用户等级
 */
public class MemberLevel {

    /**
     * 计算用户等级
     *
     * @param orderRepresentation
     * @param historyIntegral     历史积分
     * @param cumulativeIntegral  累计积分
     */
    public void computeMemberLevel(OrderRepresentation orderRepresentation, int historyIntegral, int cumulativeIntegral) {
        String oldLevel = getMemberLevelFromIntegral(historyIntegral);
        String newLevel = getMemberLevelFromIntegral(cumulativeIntegral);
        orderRepresentation.setOldMemberType(oldLevel);
        if (!oldLevel.equals(newLevel)) {
            orderRepresentation.setNewMemberType(newLevel);
        }
    }

    /**
     * 获取用户等级通过积分
     *
     * @param integral
     * @return
     */
    public String getMemberLevelFromIntegral(int integral) {
        if (integral < 10000) {
            return "普卡";
        } else if (integral < 50000) {
            return "金卡";
        } else if (integral < 100000) {
            return "白金卡";
        } else {
            return "钻石卡";
        }
    }

}
