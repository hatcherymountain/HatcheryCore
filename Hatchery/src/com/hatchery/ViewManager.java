package com.hatchery;
import java.sql.*;
import com.rova.Rova;
public class ViewManager {

    private Rova rova = null;


    /**
     * This contemplates everything related to user preferences
     * @param rova
     */
    public ViewManager(Rova rova)
    {
        this.rova=rova;
    }
}
