package com.coding.sales;

import com.coding.sales.input.OrderCommand;
import com.coding.sales.input.OrderItemCommand;
import com.coding.sales.manager.PreciousMetalStore;
import com.coding.sales.model.DiscountManager;
import com.coding.sales.model.PreciousMetal;
import com.coding.sales.output.DiscountItemRepresentation;
import com.coding.sales.output.OrderItemRepresentation;
import com.coding.sales.output.OrderRepresentation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 销售系统的主入口
 * 用于打印销售凭证
 */
public class OrderApp {

    private PreciousMetalStore mPreciousMetalStore;
    private Map<String, PreciousMetal> mPreciousMetalMap;

    public OrderApp() {
        mPreciousMetalStore = PreciousMetalStore.getInstance();
        mPreciousMetalMap = mPreciousMetalStore.getPreciousMetalMap();
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

    OrderRepresentation checkout(OrderCommand command) {
        OrderRepresentation result = new OrderRepresentation();
        List<OrderItemCommand> orderItemList = command.getItems();
        List<String> discounts = command.getDiscounts();
        computePrice(result, orderItemList);
        computeDisCountedPrice(result, orderItemList, discounts);
//        computePromotions(result, orderItemList);
        return result;
    }

    /**
     * 计算商品折扣
     *
     * @param orderRepresentation
     * @param orderItemList
     * @param discounts
     * @return
     */
    public void computeDisCountedPrice(OrderRepresentation orderRepresentation, List<OrderItemCommand> orderItemList, List<String> discounts) {
        if (orderRepresentation == null || orderItemList.size() <= 0 || null == discounts || discounts.size() <= 0) {
            return;
        }
        BigDecimal totalDiscount = new BigDecimal(0);
        //遍历打折卡
        List<DiscountItemRepresentation> discountItemRepresentations = new ArrayList<DiscountItemRepresentation>();
        for (String discount : discounts) {
            //遍历会员购买清单
            System.out.println("orderItems.size:" + orderRepresentation.getOrderItems().size());
            for (OrderItemRepresentation orderItem : orderRepresentation.getOrderItems()) {
                PreciousMetal preciousMetal = pickProductById(mPreciousMetalMap, orderItem.getProductNo());
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
                for (String usableDiscount : discounts) {
                    System.out.println("usableDiscount:" + usableDiscount);
                    if (usableDiscount.equals(discount)) {
                        float discountRate = DiscountManager.getDiscountRate(discount);
                        System.out.println("subtotal:" + orderItem.getSubTotal()+"; rate:"+discountRate);
                        BigDecimal subDiscount = orderItem.getSubTotal().multiply(new BigDecimal(discountRate));
                        DiscountItemRepresentation discountItem = new DiscountItemRepresentation(orderItem.getProductNo(), orderItem.getProductName(), subDiscount);
                        discountItemRepresentations.add(discountItem);
                        totalDiscount = totalDiscount.add(subDiscount);
                    }
                }
            }
        }
        orderRepresentation.setDiscounts(discountItemRepresentations);
        orderRepresentation.setTotalDiscountPrice(totalDiscount);
    }


    /**
     * TODO 计算商品价格
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

    private PreciousMetal pickProductById(Map<String, PreciousMetal> preciousMetalMap, String productId) {
        if (null == productId || productId.isEmpty()) {
            return null;
        }
        return preciousMetalMap.get(productId);
    }
}
