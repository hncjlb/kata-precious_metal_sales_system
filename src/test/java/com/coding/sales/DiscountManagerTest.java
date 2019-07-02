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

public class DiscountManagerTest {
    @Test
    public void should_discount_is_null_when_buy_one_product_001001_has_discount_9() {
        OrderApp app = new OrderApp();
        OrderRepresentation orderRepresentation = new OrderRepresentation();
        List<OrderItemCommand> orderItemCommandList = new ArrayList<OrderItemCommand>();
        OrderItemCommand orderItemCommand = new OrderItemCommand("001001", new BigDecimal(1));
        orderItemCommandList.add(orderItemCommand);
        List<String> discounts = new ArrayList<String>();
        discounts.add(DiscountManager.DISCOUNT_9);
        app.computePrice(orderRepresentation, orderItemCommandList);

        DiscountManager.computeDiscountedPrice(orderRepresentation, orderItemCommandList, discounts);

        assertEquals(new BigDecimal(0), orderRepresentation.getTotalDiscountPrice());
    }

    @Test
    public void should_discount_is_1242_when_buy_one_product_001002_has_discount_9() {
        OrderApp app = new OrderApp();
        OrderRepresentation orderRepresentation = new OrderRepresentation();
        List<OrderItemCommand> orderItemCommandList = new ArrayList<OrderItemCommand>();
        OrderItemCommand orderItemCommand = new OrderItemCommand("001002", new BigDecimal(1));
        orderItemCommandList.add(orderItemCommand);
        List<String> discounts = new ArrayList<String>();
        discounts.add(DiscountManager.DISCOUNT_9);
        app.computePrice(orderRepresentation, orderItemCommandList);

        DiscountManager.computeDiscountedPrice(orderRepresentation, orderItemCommandList, discounts);
        app.computeTotalDiscount(orderRepresentation);

        assertEquals(new BigDecimal(1242.00), orderRepresentation.getTotalDiscountPrice());
    }
}
