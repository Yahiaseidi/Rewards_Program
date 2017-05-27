package com.example.yahia.rewards_program;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.*;
/**
 * Created by Leslie on 5/26/2017.
 */
@DynamoDBTable(tableName = "users")
public class users{
    private String card_id;
    private String phone_number;
    private String reward_points;

    @DynamoDBHashKey(attributeName = "card_id")
    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id){
        this.card_id = card_id;
    }

    @DynamoDBRangeKey(attributeName = "phone_number")
    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    @DynamoDBAttribute(attributeName = "reward_points")
    public String getReward_points() {
        return  reward_points;
    }

    public void setReward_points(String reward_points) {
        this.reward_points = reward_points;
    }


}


