package com.coding.sales.manager;

import com.coding.sales.model.PreciousMetal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PreciousMetalStore {

    private Map<String,PreciousMetal> preciousMetalMap = new HashMap<String, PreciousMetal>();
    private static PreciousMetalStore sInstance = new PreciousMetalStore();

    private PreciousMetalStore(){
        System.out.println("PreciousMetalStore");
        initPreciousMetal();
    }

    public synchronized static PreciousMetalStore getInstance() {
        System.out.println("getInstance");
        return sInstance;
    }

    /**
     * 初始化商品
     */
    private void initPreciousMetal() {
        PreciousMetal preciousMetal1 = new PreciousMetal();
        preciousMetal1.setName("世园会五十国钱币册");
        preciousMetal1.setId("001001");
        preciousMetal1.setPrice(998.00f);
        preciousMetal1.setUnit("册");
        preciousMetalMap.put(preciousMetal1.getId(),preciousMetal1);

        PreciousMetal preciousMetal2 = new PreciousMetal();
        preciousMetal2.setId("001002");
        preciousMetal2.setName("2019北京世园会纪念银章大全40g");
        preciousMetal2.setPrice(1380.00f);
        preciousMetal2.setUnit("盒");
        List<String> discoutCoupons2 = new ArrayList<String>(1);
        discoutCoupons2.add("9折券");
        preciousMetal2.setDiscountCoupons(discoutCoupons2);
        preciousMetalMap.put(preciousMetal2.getId(),preciousMetal2);

        PreciousMetal preciousMetal3 = new PreciousMetal();
        preciousMetal3.setId("003001");
        preciousMetal3.setName("招财进宝");
        preciousMetal3.setPrice(1580.00f);
        preciousMetal3.setUnit("条");
        List<String> discoutCoupons3 = new ArrayList<String>(1);
        discoutCoupons3.add("95折券");
        preciousMetal3.setDiscountCoupons(discoutCoupons3);
        preciousMetalMap.put(preciousMetal3.getId(),preciousMetal3);

        PreciousMetal preciousMetal4 = new PreciousMetal();
        preciousMetal4.setId("003002");
        preciousMetal4.setName("水晶之恋");
        preciousMetal4.setPrice(980.00f);
        preciousMetal4.setUnit("条");
        List<String> promotions4 = new ArrayList<String>(1);
        promotions4.add("第3件半价");
        promotions4.add("满3送1");
        preciousMetal4.setPromotions(promotions4);
        preciousMetalMap.put(preciousMetal4.getId(),preciousMetal4);

        PreciousMetal preciousMetal5 = new PreciousMetal();
        preciousMetal5.setId("002002");
        preciousMetal5.setName("中国经典钱币套装");
        preciousMetal5.setPrice(998.00f);
        preciousMetal5.setUnit("套");
        List<String> promotions5 = new ArrayList<String>(1);
        promotions5.add("每满2000减30");
        promotions5.add("每满1000减10");
        preciousMetal5.setPromotions(promotions5);
        preciousMetalMap.put(preciousMetal5.getId(),preciousMetal5);

        PreciousMetal preciousMetal6 = new PreciousMetal();
        preciousMetal6.setId("002001");
        preciousMetal6.setName("守扩之羽比翼双飞4.8g");
        preciousMetal6.setPrice(1080.00f);
        preciousMetal6.setUnit("条");
        List<String> promotions6 = new ArrayList<String>(1);
        promotions6.add("第3件半价");
        promotions6.add("满3送1");
        preciousMetal6.setPromotions(promotions6);
        List<String> discoutCoupons6 = new ArrayList<String>(1);
        discoutCoupons6.add("95折券");
        preciousMetal6.setDiscountCoupons(discoutCoupons6);
        preciousMetalMap.put(preciousMetal6.getId(),preciousMetal6);

        PreciousMetal preciousMetal7 = new PreciousMetal();
        preciousMetal7.setId("002003");
        preciousMetal7.setName("中国银象棋12g");
        preciousMetal7.setPrice(698.00f);
        preciousMetal7.setUnit("套");
        List<String> promotions7 = new ArrayList<String>(1);
        promotions7.add("每满3000元减350");
        promotions7.add("每满2000减30");
        promotions7.add("每满1000减10");
        preciousMetal7.setPromotions(promotions7);
        List<String> discoutCoupons7 = new ArrayList<String>(1);
        discoutCoupons7.add("9折券");
        preciousMetal7.setDiscountCoupons(discoutCoupons7);
        preciousMetalMap.put(preciousMetal7.getId(),preciousMetal7);
    }

    public Map<String,PreciousMetal> getPreciousMetalMap(){
        return preciousMetalMap;
    }
}
