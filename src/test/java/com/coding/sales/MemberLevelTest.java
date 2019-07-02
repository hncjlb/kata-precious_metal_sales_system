package com.coding.sales;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MemberLevelTest {

    private MemberLevel memberLevel;

    @Before
    public void beFore() {
        memberLevel = new MemberLevel();
    }

    @Test
    public void when_input_integral_9860_output_level_puka() {
        String memberLevelFromIntegral = memberLevel.getMemberLevelFromIntegral(9860);
        assertEquals("普卡", memberLevelFromIntegral);
    }

    @Test
    public void when_input_integral_98860_output_level_baijinka() {
        String memberLevelFromIntegral = memberLevel.getMemberLevelFromIntegral(98860);
        assertEquals("白金卡", memberLevelFromIntegral);
    }

}