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
        int customerPoints = 186;
        int expectedResult = 200 - 186;

        assertTrue("The points needed amount is not correct!", expectedResult == pointsNeededForReward(pointIncrement, customerPoints));
    }
}
