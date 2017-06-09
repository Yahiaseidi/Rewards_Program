package com.example.yahia.rewards_program;


public class Users {

    //Yahia you wild!

    /**
     * Item numbers
     */
    @com.google.gson.annotations.SerializedName("numbers")
    private String mNumbers;

    /**
     * Item Id
     */
    @com.google.gson.annotations.SerializedName("id")
    private String mId;

    /**
     * Indicates if the item is pointsd
     */
    @com.google.gson.annotations.SerializedName("points")
    private String mPoints;

    @com.google.gson.annotations.SerializedName("card")
    private String mCard;

    /**
     * Users constructor
     */
    public Users() {

    }

    @Override
    public String toString() {
        return getNumber();
    }

    /**
     * Initializes a new Users
     *
     * @param numbers
     *            The item numbers
     * @param id
     *            The item id
     */
    public Users(String numbers, String id) {
        this.setNumber(numbers);
        this.setId(id);
    }

    /**
     * Returns the item numbers
     */
    public String getNumber() {
        return mNumbers;
    }

    /**
     * Sets the item numbers
     *
     * @param numbers
     *            numbers to set
     */
    public final void setNumber(String numbers) {
        mNumbers = numbers;
    }

    public String getCard() {
        return mCard;
    }


    public final void setCard(String card) {
        mCard = card;
    }

    /**
     * Returns the item id
     */
    public String getId() {
        return mId;
    }

    /**
     * Sets the item id
     *
     * @param id
     *            id to set
     */
    public final void setId(String id) {
        mId = id;
    }

    /**
     * Indicates if the item is marked as pointsd
     */
    public String getPoints() {
        return mPoints;
    }

    /**
     * Marks the item as pointsd or inpointsd
     */
    public void setPoints(String points) {
        mPoints = points;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Users && ((Users) o).mId == mId;
    }
}