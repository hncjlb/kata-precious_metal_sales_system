package com.coding.sales;

import com.coding.sales.input.OrderCommand;
import com.coding.sales.input.OrderItemCommand;
import com.coding.sales.manager.MemberMsg;
import com.coding.sales.manager.PreciousMetalStore;
import com.coding.sales.manager.DiscountManager;
import com.coding.sales.manager.PromotionManager;
import com.coding.sales.model.Member;
import com.coding.sales.model.PreciousMetal;
import com.coding.sales.output.DiscountItemRepresentation;
import com.coding.sales.output.OrderItemRepresentation;
import com.coding.sales.output.OrderRepresentation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 销售系统的主入口
 * 用于打印销售凭证
 */
public class OrderApp {

    private PreciousMetalStore mPreciousMetalStore;
    private Map<String, PreciousMetal> mPreciousMetalMap;
    private Map<String, Member> mMemberMap;

    public OrderApp() {
        mPreciousMetalStore = PreciousMetalStore.getInstance();
        mPreciousMetalMap = mPreciousMetalStore.getPreciousMetalMap();
        mMemberMap = MemberMsg.getInstance().getMemberMsg();
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("参数不正确。参数1为销售订单的JSON文件名，参数2为待打印销售凭证的文本文件名.");
        }

        String jsonFileName = args[0];
        String txtFileName = args[1];

        String orderCommand = FileUtils.readFromFile(jsonFileName);
        OrderApp app = new OrderApp();
        String result = app.checkout(orderCommand);
        FileUtils.writeToFile(result, txtFileName);
    }

    public String checkout(String orderCommand) {
        OrderCommand command = OrderCommand.from(orderCommand);
        OrderRepresentation result = checkout(command);

        return result.toString();
    }

    public OrderRepresentation checkout(OrderCommand command) {
        OrderRepresentation result = new OrderRepresentation();

        List<OrderItemCommand> orderItemList = command.getItems();
        List<String> discounts = command.getDiscounts();

        Member member = getOrderMemberMsg(mMemberMap, command.getMemberId());
        initCurrentOrderMemberMsg(result, member, command);
        computePrice(result, orderItemList);
        DiscountManager.computeDiscountedPrice(result, orderItemList, discounts);

        PromotionManager.computePromotions(result);

        computeTotalDiscount(result);
        computeDeal(result);
        computeMemberIntegral(result, member);
        computeMemberLevel(result, member);
        return result;
    }

    private void computeDeal(OrderRepresentation result) {
        result.setReceivables(result.getTotalPrice().subtract(result.getTotalDiscountPrice()));
    }

    public void computeTotalDiscount(OrderRepresentation order) {
        if (null == order || null == order.getDiscounts() || order.getDiscounts().size() <= 0) {
            return;
        }

        List<DiscountItemRepresentation> discounts = order.getDiscounts();
        BigDecimal totalDiscount = new BigDecimal(0);
        for (DiscountItemRepresentation discount : discounts) {
            totalDiscount = totalDiscount.add(discount.getDiscount());
        }
        order.setTotalDiscountPrice(totalDiscount);
    }


    /**
     * 获取订单会员信息
     *
     * @param mMemberMap
     * @param memberId
     * @return
     */
    private Member getOrderMemberMsg(Map<String, Member> mMemberMap, String memberId) {
        return mMemberMap.get(memberId);
    }

    /**
     * 初始化当前订单
     *
     * @param member
     */
    public void initCurrentOrderMemberMsg(OrderRepresentation orderRepresentation, Member member, OrderCommand command) {
        if (orderRepresentation == null) {
            return;
        }
        orderRepresentation.setMemberNo(member.getCardNo());
        orderRepresentation.setOrderId(command.getOrderId());
        orderRepresentation.setCreateTime(new Date(command.getCreateTime()));
        orderRepresentation.setMemberName(member.getName());
    }

    /**
     * 计算商品价格
     *
     * @param orderItemList
     */
    public void computePrice(OrderRepresentation orderRepresentation, List<OrderItemCommand> orderItemList) {
        if (null == orderItemList || orderItemList.size() <= 0) {
            return;
        }
        BigDecimal totalPrice = new BigDecimal(0);
        List<OrderItemRepresentation> orderItemRepresentations = new ArrayList<OrderItemRepresentation>();
        for (OrderItemCommand orderItem : orderItemList) {
            if (null == orderItem) {
                continue;
            }
            PreciousMetal preciousMetal = pickProductById(mPreciousMetalMap, orderItem.getProduct());
            if (null == preciousMetal) {
                continue;
            }
            BigDecimal subTotal = new BigDecimal(preciousMetal.getPrice());
            subTotal = subTotal.multiply(orderItem.getAmount());
            OrderItemRepresentation orderItemRepresentation = new OrderItemRepresentation(preciousMetal.getId(), preciousMetal.getName(), new BigDecimal(preciousMetal.getPrice()), orderItem.getAmount(), subTotal);
            orderItemRepresentations.add(orderItemRepresentation);
            totalPrice = totalPrice.add(subTotal);
        }
        orderRepresentation.setItems(orderItemRepresentations);
        orderRepresentation.setTotalPrice(totalPrice);
    }

    /**
     * 计算用户积分
     */
    private void computeMemberIntegral(OrderRepresentation orderRepresentation, Member member) {
        MemberIntegral memberIntegral = new MemberIntegral();
        memberIntegral.computeMemberIntegral(orderRepresentation, member, orderRepresentation.getReceivables());
    }

    /**
     * 计算用户等级
     *
     * @param orderRepresentation
     * @param member
     */
    private void computeMemberLevel(OrderRepresentation orderRepresentation, Member member) {
        MemberLevel memberLevel = new MemberLevel();
        memberLevel.computeMemberLevel(orderRepresentation, Integer.valueOf(member.getIntegral()),
                orderRepresentation.getMemberPoints());
    }

    private PreciousMetal pickProductById(Map<String, PreciousMetal> preciousMetalMap, String productId) {
        if (null == productId || productId.isEmpty()) {
            return null;
        }
        return preciousMetalMap.get(productId);
    }

}
