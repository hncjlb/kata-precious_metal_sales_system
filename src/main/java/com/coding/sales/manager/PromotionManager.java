package com.coding.sales.manager;

import com.coding.sales.input.OrderItemCommand;
import com.coding.sales.model.PreciousMetal;
import com.coding.sales.output.DiscountItemRepresentation;
import com.coding.sales.output.OrderItemRepresentation;
import com.coding.sales.output.OrderRepresentation;

import java.math.BigDecimal;
import java.util.*;

public class PromotionManager {
    public static final String type1 = "每满3000元减350";
    public static final String type2 = "每满2000元减30";
    public static final String type3 = "每满1000元减10";
    public static final String type4 = "第3件半价";
    public static final String type5 = "满3送1";


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
            System.out.println(promotions.size());
            Map<String,Float> promotionResult = computeMaxPromotion(promotions, orderItem);
            for(String key : promotionResult.keySet()) {
                float maxPromotion = promotionResult.get(key);
                System.out.println("maxPromotion:"+maxPromotion);
                compareAndDiscount(result, orderItem, maxPromotion);
                if(!result.getDiscountCards().contains(key)) {
                    result.getDiscountCards().add(key);
                }
            }
        }
    }

    private static void compareAndDiscount(OrderRepresentation result, OrderItemRepresentation orderItem, float maxPromotion) {
        boolean hasAdd = false;
        if (null != result.getDiscounts() && result.getDiscounts().size() > 0) {
            List<DiscountItemRepresentation> discounts = result.getDiscounts();
            for (DiscountItemRepresentation discount : discounts) {
                System.out.println("discount:"+discount.getProductName()+"; "+discount.getDiscount());
                if (discount.getProductNo().equals(orderItem.getProductNo()) && maxPromotion > discount.getDiscount().floatValue()) {
                    discount.setDiscount(new BigDecimal(maxPromotion));
                    hasAdd = true;
                    break;
                }
            }
            if (!hasAdd) {
                result.getDiscounts().add(new DiscountItemRepresentation(orderItem.getProductNo(), orderItem.getProductName(), new BigDecimal(maxPromotion)));
            }
        } else {
            List<DiscountItemRepresentation> discounts = new ArrayList<DiscountItemRepresentation>();
            DiscountItemRepresentation discount = new DiscountItemRepresentation(orderItem.getProductNo(), orderItem.getProductName(), new BigDecimal(maxPromotion));
            discounts.add(discount);
            result.setDiscounts(discounts);
        }
    }

    private static Map<String,Float> computeMaxPromotion(List<String> promotions, OrderItemRepresentation orderItem) {
        float maxPromotion = 0.0f;
        String maxPromotionStr = null;
        for (String promotion : promotions) {
            if (type1.equals(promotion) && orderItem.getSubTotal().floatValue() >= 3000 && maxPromotion < 350) {
                maxPromotion = 350;
                maxPromotionStr = type1;
            } else if (type2.equals(promotion) && orderItem.getSubTotal().floatValue() >= 2000 && maxPromotion < 30) {
                maxPromotion = 30;
                maxPromotionStr = type2;
            } else if (type3.equals(promotion) && orderItem.getSubTotal().floatValue() >= 1000 && maxPromotion < 10) {
                maxPromotion = 10;
                maxPromotionStr = type3;
            } else if (type4.equals(promotion) && orderItem.getAmount().intValue() >= 3 && maxPromotion < orderItem.getPrice().divide(new BigDecimal(0.5)).floatValue()) {
                maxPromotion = orderItem.getPrice().multiply(new BigDecimal(0.5)).floatValue();
                maxPromotionStr = type4;
                System.out.println("第三件半件"+maxPromotion);
            } else if ("满3送1".equals(promotion) && orderItem.getAmount().intValue() >= 4 && maxPromotion < orderItem.getPrice().floatValue()) {
                maxPromotion = orderItem.getPrice().floatValue();
                maxPromotionStr = type5;
                System.out.println("满3送1"+maxPromotion);
            }
        }
        Map<String,Float>  result = new HashMap<String, Float>();
        result.put(maxPromotionStr,maxPromotion);
        System.out.println("str:"+maxPromotionStr+"; va:"+maxPromotion);
        return result;
    }
}
