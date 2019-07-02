package com.coding.sales;

import com.coding.sales.model.Member;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MemberIntegralTest {

    private Member member;
    private MemberIntegral memberIntegral;

    @Before
    public void beFore() {
        member = new Member("马丁", "普卡", "6236609999", "9860");
        memberIntegral = new MemberIntegral();
    }

    @Test
    public void when_input_increase_integral_9860_output_19720() {
        int orderCumulativeIntegral = memberIntegral.getOrderCumulativeIntegral(member, 9860);
        assertEquals(19720, orderCumulativeIntegral);
    }

    @Test
    public void when_input_user_level_puka_output_integral_benchmark_1() {
        String memberIntegralBenchmark = memberIntegral.getMemberIntegralBenchmark(member);
        assertEquals("1", memberIntegralBenchmark);
    }

    @Test
    public void when_input_user_level_baijin_output_integral_benchmark_1point8() {
        Member member1 = new Member("马丁", "白金卡", "6236609999", "9860");
        String memberIntegralBenchmark = memberIntegral.getMemberIntegralBenchmark(member1);
        assertEquals("1.8", memberIntegralBenchmark);
    }
}