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

public class PromotionManager {
    private static Map<String, PreciousMetal> sPreciousMetalMap;

    public static void computePromotions(OrderRepresentation result) {
        if (null == result || null == result.getOrderItems() || result.getOrderItems().size() <= 0) {
            return;
        }
        sPreciousMetalMap = PreciousMetalStore.getInstance().getPreciousMetalMap();

        List<OrderItemRepresentation> orderItems = result.getOrderItems();
        for (OrderItemRepresentation orderItem : orderItems) {
            if (!sPreciousMetalMap.containsKey(orderItem.getProductNo())) {
                continue;
            }
            System.out.println("orderItem.no:"+orderItem.getProductNo());
            PreciousMetal preciousMetal = sPreciousMetalMap.get(orderItem.getProductNo());
            if (null == preciousMetal.getPromotions() || preciousMetal.getPromotions().size() <= 0) {
                System.out.println("getPromotions:"+preciousMetal.getPromotions());
                return;
            }
            List<String> promotions = preciousMetal.getPromotions();
            float maxPromotion = computeMaxPromotion(promotions, orderItem);
            System.out.println("maxPromotion:"+maxPromotion);
            compareAndDiscount(result, orderItem, maxPromotion);
        }
    }

    private static void compareAndDiscount(OrderRepresentation result, OrderItemRepresentation orderItem, float maxPromotion) {
        boolean hasAdd = false;
        if (null != result.getDiscounts() && result.getDiscounts().size() > 0) {
            List<DiscountItemRepresentation> discounts = result.getDiscounts();
            for (DiscountItemRepresentation discount : discounts) {
                if (discount.getProductNo().equals(orderItem.getProductNo()) && maxPromotion > discount.getDiscount().floatValue()) {
                    BigDecimal tmp = new BigDecimal(maxPromotion).subtract(discount.getDiscount());
                    discount.setDiscount(new BigDecimal(maxPromotion));
                    hasAdd = true;
                    break;
                }
            }
            if (!hasAdd) {
                result.getDiscounts().add(new DiscountItemRepresentation(orderItem.getProductNo(), orderItem.getProductName(), new BigDecimal(maxPromotion)));
            }
        } else {
            System.out.println("222222222");
            List<DiscountItemRepresentation> discounts = new ArrayList<DiscountItemRepresentation>();
            DiscountItemRepresentation discount = new DiscountItemRepresentation(orderItem.getProductNo(), orderItem.getProductName(), new BigDecimal(maxPromotion));
            discounts.add(discount);
            result.setDiscounts(discounts);
        }
    }

    private static float computeMaxPromotion(List<String> promotions, OrderItemRepresentation orderItem) {
        float maxPromotion = 0.0f;
        for (String promotion : promotions) {
            if ("每满3000元减350".equals(promotion) && orderItem.getSubTotal().floatValue() >= 3000 && maxPromotion < 350) {
                maxPromotion = 350;
            } else if ("每满2000元减30".equals(promotion) && orderItem.getSubTotal().floatValue() >= 2000 && maxPromotion < 30) {
                maxPromotion = 30;
            } else if ("每满1000元减10".equals(promotion) && orderItem.getSubTotal().floatValue() >= 1000 && maxPromotion < 10) {
                maxPromotion = 10;
            } else if ("第3件半价".equals(promotion) && orderItem.getAmount().intValue() >= 3 && maxPromotion < orderItem.getPrice().divide(new BigDecimal(0.5)).floatValue()) {
                maxPromotion = orderItem.getPrice().multiply(new BigDecimal(0.5)).floatValue();
                System.out.println("第三件半件"+maxPromotion);
            } else if ("满3送1".equals(promotion) && orderItem.getAmount().intValue() >= 4 && maxPromotion < orderItem.getPrice().floatValue()) {
                maxPromotion = orderItem.getPrice().floatValue();
                System.out.println("满3送1"+maxPromotion);
            }
        }
        return maxPromotion;
    }
}
