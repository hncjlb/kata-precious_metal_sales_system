package com.coding.sales.manager;

import com.coding.sales.model.Member;

import java.util.HashMap;
import java.util.Map;

public class MemberMsg {

    private Map<String, Member> memberMsg = new HashMap<String, Member>();
    private static MemberMsg sInstance = new MemberMsg();

    public MemberMsg() {
        initMemberMsg();
    }

    public synchronized static MemberMsg getInstance() {
        System.out.println("getInstance");
        return sInstance;
    }

    private void initMemberMsg() {
        Member member1 = new Member("马丁", "普卡", "6236609999", "9860");
        Member member2 = new Member("王立", "金卡", "6630009999", "48860");
        Member member3 = new Member("李想", "白金卡", "8230009999", "98860");
        Member member4 = new Member("张三", "钻石卡", "9230009999", "198860");
        memberMsg.put(member1.getCardNo(), member1);
        memberMsg.put(member2.getCardNo(), member2);
        memberMsg.put(member3.getCardNo(), member3);
        memberMsg.put(member4.getCardNo(), member4);
    }

    public Map<String, Member> getMemberMsg() {
        return memberMsg;
    }


}
