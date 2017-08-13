package com.example.yahia.rewards_program;

/**
* Created by Leslie on 8/12/2017.
*/

public class Rewards {


    @com.google.gson.annotations.SerializedName("totalWinnings")
    private String mTotalWinnings;

    /**
     * Item Id
     */
    @com.google.gson.annotations.SerializedName("id")
    private String mId;

    /**
     * Indicates if the item is pointsd
     */
    @com.google.gson.annotations.SerializedName("lowAmount")
    private String mLowAmount;

    @com.google.gson.annotations.SerializedName("mediumAmount")
    private String mMediumAmount;

    @com.google.gson.annotations.SerializedName("highAmount")
    private String mHighAmount;

    /**
     * Users constructor
     */
    public Rewards() {

    }

    public Rewards(String totalWinnings, String lowAmount, String mediumAmount, String highAmount) {
        this.setTotalWinnings(totalWinnings);
        this.setLowAmount(lowAmount);
        this.setMediumAmount(mediumAmount);
        this.setHighAmount(highAmount);
    }

    /**
     * Returns the item numbers
     */
    public String getTotalWinnings() {
        return mTotalWinnings;
    }


    public final void setTotalWinnings(String totalWinnings) {
        mTotalWinnings = totalWinnings;
    }

    public String getLowAmount() {
        return mLowAmount;
    }


    public final void setLowAmount(String lowAmount) {
        mLowAmount = lowAmount;
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
    public String getMediumAmount() {
        return mMediumAmount;
    }

    /**
     * Marks the item as pointsd or inpointsd
     */
    public void setMediumAmount(String mediumAmount) {
        mMediumAmount = mediumAmount;
    }

    public String getHighAmount() {
        return mHighAmount;
    }

    /**
     * Marks the item as pointsd or inpointsd
     */
    public void setHighAmount(String highAmount) {
        mHighAmount = highAmount;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof com.example.yahia.rewards_program.Rewards && ((com.example.yahia.rewards_program.Rewards) o).mId == mId;
    }
}

