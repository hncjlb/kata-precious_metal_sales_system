package com.coding.sales;

import com.coding.sales.input.OrderCommand;
import com.coding.sales.input.OrderItemCommand;
import com.coding.sales.manager.PreciousMetalStore;
import com.coding.sales.model.PreciousMetal;
import com.coding.sales.output.OrderRepresentation;

import java.util.ArrayList;
import java.util.List;

/**
 * 销售系统的主入口
 * 用于打印销售凭证
 */
public class OrderApp {
    private PreciousMetalStore mPreciousMetalStore;
    private List<PreciousMetal> mPreciousMetalList;

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
        OrderRepresentation result = null;
        mPreciousMetalStore = PreciousMetalStore.getInstance();
        mPreciousMetalList = mPreciousMetalStore.getPreciousMetalList();
        OrderRepresentation orderRepresentation = new OrderRepresentation();
        //TODO 获取商品列表
        List<OrderItemCommand> orderItemList = command.getItems();
        float originPrice = computePrice(orderRepresentation, orderItemList);


        return result;
    }

    /**
     * TODO 计算商品价格
     *
     * @param orderItemList
     */
    public float computePrice(OrderRepresentation orderRepresentation, List<OrderItemCommand> orderItemList) {
        if (null == orderItemList || orderItemList.size() <= 0) {
            return 0.00f;
        }
        float price = 0.00f;
        for (OrderItemCommand orderItem : orderItemList) {
            if (null == orderItem) {
                continue;
            }
            PreciousMetal preciousMetal = pickProductById(orderItem.getProduct());
            preciousMetal.getPrice();
        }
        return 0.00f;
    }

    private PreciousMetal pickProductById(String productId) {
        if (null == productId || productId.isEmpty()) {
            return null;
        }
        for (PreciousMetal preciousMetal : mPreciousMetalList) {
            if (preciousMetal.getId().equals(productId)) {
                return preciousMetal;
            }
        }
        return null;
    }
}
