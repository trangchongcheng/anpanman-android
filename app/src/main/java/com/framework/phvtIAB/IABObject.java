package com.framework.phvtIAB;
/**
 * Framework PHATVT
 *
 * @author PhatVan ヴァン  タン　ファット
 * @since:  04 - 2016
 * In App Billing Objects
 *
 */

public class IABObject {
    //----------------------------------------------------------------------------------------------
    String id;
    String name;
    boolean isPurchased;

    //----------------------------------------------------------------------------------------------
    public String getId() {
        return id;
    }

    //----------------------------------------------------------------------------------------------
    public void setId(String id) {
        this.id = id;
    }

    //----------------------------------------------------------------------------------------------
    public String getName() {
        return name;
    }

    //----------------------------------------------------------------------------------------------
    public void setName(String name) {
        this.name = name;
    }

    //----------------------------------------------------------------------------------------------
    public boolean isPurchased() {
        return isPurchased;
    }

    //----------------------------------------------------------------------------------------------
    public void setIsPurchased(boolean isPurchased) {
        this.isPurchased = isPurchased;
    }

    //----------------------------------------------------------------------------------------------
    /**
     * Object to String Log
     *
     * @return
     */
    @Override
    public String toString() {
        return "IABObject{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", isPurchased=" + isPurchased +
                '}';
    }

    //----------------------------------------------------------------------------------------------
    /**
     * COnstructor with id , name and purchase
     *
     * @param isPurchased
     * @param id
     */

    public IABObject(String id, String name, boolean isPurchased) {
        this.id = id;
        this.name = name;
        this.isPurchased = isPurchased;
    }


    //----------------------------------------------------------------------------------------------
    /**
     * Constructor initilize Class
     */
    public IABObject() {
        this.id = "";
        this.name = "";
        this.isPurchased = false;
    }

}
