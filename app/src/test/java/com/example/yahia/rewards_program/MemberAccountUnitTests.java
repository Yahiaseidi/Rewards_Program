package com.example.yahia.rewards_program;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by JosePC on 7/18/2017.
 */

public class MemberAccountUnitTests extends MemberAccount {

    @Test
    public void pointsNeededForRewardTest ()
    {
        int pointIncrement = 100;
        int customerPoints = 1086;
        int expectedResult = 14;

        assertTrue("The points needed amount does not match the expected value!!", expectedResult == pointsNeededForReward(pointIncrement, customerPoints));
    }
}
