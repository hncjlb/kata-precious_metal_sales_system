package com.coding.sales;

import com.coding.sales.input.OrderItemCommand;
import com.coding.sales.manager.DiscountManager;
import com.coding.sales.model.Member;
import com.coding.sales.output.OrderRepresentation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(Parameterized.class)
public class OrderAppTest {
    @Parameterized.Parameters
    public static Collection<Object[]> parameters() {
        Object[][] data = new Object[][]{
                {"sample_command.json", "sample_result.txt"},
        };

        return Arrays.asList(data);
    }

    private String commandFileName;
    private String expectedResultFileName;

    public OrderAppTest(String commandFileName, String expectedResultFileName) {
        this.commandFileName = commandFileName;
        this.expectedResultFileName = expectedResultFileName;
    }

    @Test
    public void should_checkout_order() {
        String orderCommand = FileUtils.readFromFile(getResourceFilePath(commandFileName));
        OrderApp app = new OrderApp();
        String actualResult = app.checkout(orderCommand);

        String expectedResult = FileUtils.readFromFile(getResourceFilePath(expectedResultFileName));

        assertEquals(expectedResult, actualResult);
    }

    private String getResourceFilePath(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        return file.getAbsolutePath();
    }

    @Test
    public void should_total_998_when_buy_one_product_001001() {
        OrderApp app = new OrderApp();
        OrderRepresentation orderRepresentation = new OrderRepresentation();
        List<OrderItemCommand> orderItemCommandList = new ArrayList<OrderItemCommand>();
        OrderItemCommand orderItemCommand = new OrderItemCommand("001001", new BigDecimal(1));
        orderItemCommandList.add(orderItemCommand);

        app.computePrice(orderRepresentation, orderItemCommandList);

        assertEquals(new BigDecimal(998.00), orderRepresentation.getTotalPrice());
    }

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

        app.computeDisCountedPrice(orderRepresentation, orderItemCommandList, discounts);

        assertNull(orderRepresentation.getDiscounts());
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

        app.computeDisCountedPrice(orderRepresentation, orderItemCommandList, discounts);

        assertEquals(new BigDecimal(1242.00), orderRepresentation.getTotalDiscountPrice());
    }

    @Test
    public void when_input_member_name_mading_output_result_mading() {
        OrderApp app = new OrderApp();
        OrderRepresentation orderRepresentation = new OrderRepresentation();
        Member member = new Member("马丁", "普卡", "6236609999", "9860");
    }
}
