package com.coding.sales.manager;

import com.coding.sales.input.OrderItemCommand;
import com.coding.sales.model.PreciousMetal;
import com.coding.sales.output.DiscountItemRepresentation;
import com.coding.sales.output.OrderItemRepresentation;
import com.coding.sales.output.OrderRepresentation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DiscountManager {
    public static final String DISCOUNT_9 = "9折券";
    public static final String DISCOUNT_95 = "95折券";

    private static Map<String, PreciousMetal> sPreciousMetalMap;

    public static float getDiscountRate(String discount) {
        if (DiscountManager.DISCOUNT_9.equals(discount)) {
            return 0.1f;
        } else if (DiscountManager.DISCOUNT_95.equals(discount)) {
            return 0.05f;
        } else {
            return 0f;
        }
    }

    /**
     * 计算商品折扣
     *
     * @param orderRepresentation
     * @param orderItemList
     * @param discounts
     * @return
     */
    public static void computeDiscountedPrice(OrderRepresentation orderRepresentation, List<OrderItemCommand> orderItemList, List<String> discounts) {
        if (orderRepresentation == null || orderItemList.size() <= 0 || null == discounts || discounts.size() <= 0) {
            return;
        }
        sPreciousMetalMap = PreciousMetalStore.getInstance().getPreciousMetalMap();
        //遍历打折卡
        List<DiscountItemRepresentation> discountItemRepresentations = new ArrayList<DiscountItemRepresentation>();

        List<String> usedDiscountCards = new ArrayList();
        for (String discount : discounts) {
            //遍历会员购买清单
            System.out.println("orderItems.size:" + orderRepresentation.getOrderItems().size());
            for (OrderItemRepresentation orderItem : orderRepresentation.getOrderItems()) {
                PreciousMetal preciousMetal = PreciousMetal.pickProductById(sPreciousMetalMap, orderItem.getProductNo());
                if (null == preciousMetal) {
                    continue;
                }
                //获取商品可使用的打折卡
                List<String> discountCoupons = preciousMetal.getDiscountCoupons();
                if (null == discountCoupons || !discountCoupons.contains(discount)) {
                    continue;
                }
                System.out.println("discountCoupons.size:" + orderRepresentation.getOrderItems().size());
                //遍历用户上送的打折卡类型
                for (String usableDiscount : discountCoupons) {
                    System.out.println("usableDiscount:" + usableDiscount);
                    if (usableDiscount.equals(discount)) {
                        float discountRate = DiscountManager.getDiscountRate(discount);
                        System.out.println("subtotal:" + orderItem.getSubTotal() + "; rate:" + discountRate);
                        BigDecimal subDiscount = orderItem.getSubTotal().multiply(new BigDecimal(discountRate));
                        DiscountItemRepresentation discountItem = new DiscountItemRepresentation(orderItem.getProductNo(), orderItem.getProductName(), subDiscount);
                        boolean hasAdd = compareExistDiscount(discountItemRepresentations, discountItem);
                        if (!hasAdd) {
                            discountItemRepresentations.add(discountItem);
                        }
                        if (!usedDiscountCards.contains(usableDiscount)) {
                            usedDiscountCards.add(usableDiscount);
                        }
                    }
                }
            }
        }

        orderRepresentation.setDiscountCards(usedDiscountCards);
        orderRepresentation.setDiscounts(discountItemRepresentations);
    }

    private static boolean compareExistDiscount(List<DiscountItemRepresentation> discountItemRepresentations, DiscountItemRepresentation discountItem) {
        boolean hasAdd = false;
        for (DiscountItemRepresentation existDiscount : discountItemRepresentations) {
            if (existDiscount.getProductNo().equals(discountItem.getProductNo()) && existDiscount.getDiscount().floatValue() < discountItem.getDiscount().floatValue()) {
                existDiscount.setDiscount(discountItem.getDiscount());
                hasAdd = true;
                break;
            }
        }
        return hasAdd;
    }
}
