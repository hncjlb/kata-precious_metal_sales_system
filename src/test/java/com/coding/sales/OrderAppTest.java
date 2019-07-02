package com.coding.sales;

import com.coding.sales.input.OrderItemCommand;
import com.coding.sales.manager.PreciousMetalStore;
import com.coding.sales.model.PreciousMetal;
import com.coding.sales.output.OrderItemRepresentation;
import com.coding.sales.output.OrderRepresentation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

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
        PreciousMetalStore mPreciousMetalStore = PreciousMetalStore.getInstance();
        List<PreciousMetal> preciousMetalList = mPreciousMetalStore.getPreciousMetalList();

        app.computePrice(orderRepresentation,preciousMetalList, orderItemCommandList);

        assertEquals(new BigDecimal(998.00), orderRepresentation.getTotalPrice());
    }
}
