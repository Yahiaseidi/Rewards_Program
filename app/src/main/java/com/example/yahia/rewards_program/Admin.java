package com.example.yahia.rewards_program;

/**
 * Created by Leslie on 8/5/2017.
 */

public class Admin {
    @com.google.gson.annotations.SerializedName("password")
    private String mPassword;

    /**
     * Item Id
     */
    @com.google.gson.annotations.SerializedName("id")
    private String mId;

    /**
     * Users constructor
     */
    public Admin() {

    }

    @Override
    public String toString() {
        return getPassword();
    }

    /**
     * Initializes a new Users
     *
     * @param password
     *            The item numbers
     * @param id
     *            The item id
     */
    public Admin(String password, String id) {
        this.setPassword(password);
        this.setId(id);
    }

    /**
     * Returns the item numbers
     */
    public String getPassword() {
        return mPassword;
    }

    /**
     * Sets the item numbers
     *
     * @param password
     *            numbers to set
     */
    public final void setPassword(String password) {
        mPassword = password;
    }


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

    @Override
    public boolean equals(Object o) {
        return o instanceof Admin && ((Admin) o).mId == mId;
    }

}
