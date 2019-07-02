package com.coding.sales;

import com.coding.sales.input.OrderCommand;
import com.coding.sales.input.OrderItemCommand;
import com.coding.sales.manager.PreciousMetalStore;
import com.coding.sales.model.PreciousMetal;
import com.coding.sales.output.OrderItemRepresentation;
import com.coding.sales.output.OrderRepresentation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 销售系统的主入口
 * 用于打印销售凭证
 */
public class OrderApp {
    private PreciousMetalStore mPreciousMetalStore;

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
        List<PreciousMetal> preciousMetalList = mPreciousMetalStore.getPreciousMetalList();
        OrderRepresentation orderRepresentation = new OrderRepresentation();
        //TODO 获取商品列表
        List<OrderItemCommand> orderItemList = command.getItems();
        computePrice(orderRepresentation, preciousMetalList, orderItemList);


        return result;
    }

    /**
     * TODO 计算商品价格
     *
     * @param orderItemList
     */
    public void computePrice(OrderRepresentation orderRepresentation, List<PreciousMetal> preciousMetalList, List<OrderItemCommand> orderItemList) {
        if (null == orderItemList || orderItemList.size() <= 0) {
            return;
        }
        BigDecimal totalPrice = new BigDecimal(0);
        List<OrderItemRepresentation> orderItemRepresentations = new ArrayList<OrderItemRepresentation>();
        for (OrderItemCommand orderItem : orderItemList) {
            if (null == orderItem) {
                continue;
            }
            PreciousMetal preciousMetal = pickProductById(preciousMetalList, orderItem.getProduct());
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

    private PreciousMetal pickProductById(List<PreciousMetal> preciousMetalList, String productId) {
        if (null == productId || productId.isEmpty()) {
            return null;
        }
        for (PreciousMetal preciousMetal : preciousMetalList) {
            if (preciousMetal.getId().equals(productId)) {
                return preciousMetal;
            }
        }
        return null;
    }
}
