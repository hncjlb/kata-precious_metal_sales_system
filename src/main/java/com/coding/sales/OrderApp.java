package com.coding.sales;

import com.coding.sales.input.OrderCommand;
import com.coding.sales.input.OrderItemCommand;
import com.coding.sales.manager.MemberMsg;
import com.coding.sales.manager.PreciousMetalStore;
import com.coding.sales.model.Member;
import com.coding.sales.model.PreciousMetal;
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
//<<<<<<<Updated upstream
//        List<PreciousMetal> preciousMetalList = mPreciousMetalStore.getPreciousMetalList();
//        OrderRepresentation orderRepresentation = new OrderRepresentation();
//        //TODO 获取商品列表
//        List<OrderItemCommand> orderItemList = command.getItems();
//        computePrice(orderRepresentation, preciousMetalList, orderItemList);

//=======
        Map<String, Member> mMemberMap = MemberMsg.getInstance().getMemberMsg();
        Map<String, PreciousMetal> mPreciousMetalMap = mPreciousMetalStore.getPreciousMetalMap();
        OrderRepresentation orderRepresentation = new OrderRepresentation();
        //TODO 获取商品列表
        List<OrderItemCommand> orderItemList = command.getItems();
        List<String> discounts = command.getDiscounts();
        computePrice(orderRepresentation, mPreciousMetalMap, orderItemList);
        computeDisCountedPrice(orderRepresentation, orderItemList, mPreciousMetalMap, discounts);
//>>>>>>>Stashed changes

        return result;
    }

    /**
     * 计算商品折扣
     *
     * @param orderRepresentation
     * @param orderItemList
     * @param mPreciousMetalMap
     * @param discounts
     * @return
     */
    private float computeDisCountedPrice(OrderRepresentation orderRepresentation, List<OrderItemCommand> orderItemList, Map<String, PreciousMetal> mPreciousMetalMap, List<String> discounts) {
        if (orderRepresentation == null || orderItemList.size() <= 0 || discounts.size() <= 0) {
            return 0.00f;
        }
        //遍历会员购买清单
        for (OrderItemCommand orderItem : orderItemList) {
            //从在售清单获取商品信息
            PreciousMetal preciousMetal = mPreciousMetalMap.get(orderItem.getProduct());
            if (null == preciousMetal) {
                continue;
            }
            //获取商品可使用的打折卡
            List<String> discountCoupons = preciousMetal.getDiscountCoupons();
            //遍历用户上送的打折卡类型
            for (String discount : discounts) {
                //该商品是否支持用户手中的打折卡
                if (discountCoupons.contains(discount)) {

                }
                //计算该商品打折后的金额


            }
        }
        return 0;
    }

    /**
     * TODO 计算商品价格
     *
     * @param orderItemList
     */
    public void computePrice(OrderRepresentation orderRepresentation, Map<String, PreciousMetal> preciousMetalMap, List<OrderItemCommand> orderItemList) {
        if (null == orderItemList || orderItemList.size() <= 0) {
            return;
        }
        BigDecimal totalPrice = new BigDecimal(0);
        List<OrderItemRepresentation> orderItemRepresentations = new ArrayList<OrderItemRepresentation>();
        for (OrderItemCommand orderItem : orderItemList) {
            if (null == orderItem) {
                continue;
            }
            PreciousMetal preciousMetal = pickProductById(preciousMetalMap, orderItem.getProduct());
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
