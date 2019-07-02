package com.coding.sales;

import com.coding.sales.input.OrderItemCommand;
import com.coding.sales.manager.DiscountManager;
import com.coding.sales.manager.PromotionManager;
import com.coding.sales.output.OrderRepresentation;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PromotionManagerTest {
    @Test
    public void should_discount_is_null_when_buy_one_product_001001_has_promotion_type1() {
        OrderApp app = new OrderApp();
        OrderRepresentation orderRepresentation = new OrderRepresentation();
        List<OrderItemCommand> orderItemCommandList = new ArrayList<OrderItemCommand>();
        OrderItemCommand orderItemCommand = new OrderItemCommand("001002", new BigDecimal(1));
        orderItemCommandList.add(orderItemCommand);
        List<String> discounts = new ArrayList<String>();
        discounts.add(DiscountManager.DISCOUNT_9);
        app.computePrice(orderRepresentation, orderItemCommandList);
//        app.computeDisCountedPrice(orderRepresentation, orderItemCommandList, discounts);

        PromotionManager.computePromotions(orderRepresentation);

        assertNull(orderRepresentation.getTotalDiscountPrice());
    }

    @Test
    public void should_discount_is_350_when_buy_5_product_002003() {
        OrderApp app = new OrderApp();
        OrderRepresentation orderRepresentation = new OrderRepresentation();
        List<OrderItemCommand> orderItemCommandList = new ArrayList<OrderItemCommand>();
        OrderItemCommand orderItemCommand = new OrderItemCommand("002003", new BigDecimal(5));
        orderItemCommandList.add(orderItemCommand);
        app.computePrice(orderRepresentation, orderItemCommandList);
        List<String> discounts = new ArrayList<String>();
        discounts.add(DiscountManager.DISCOUNT_9);
        app.computePrice(orderRepresentation, orderItemCommandList);
        DiscountManager.computeDiscountedPrice(orderRepresentation, orderItemCommandList, discounts);

        PromotionManager.computePromotions(orderRepresentation);
        app.computeTotalDiscount(orderRepresentation);

        assertEquals(new BigDecimal(350), orderRepresentation.getTotalDiscountPrice());
    }
}
