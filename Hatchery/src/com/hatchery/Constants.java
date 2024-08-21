package com.hatchery;

import java.util.ArrayList;

public class Constants {

    public static final String[] GOAL_AREAS = { "Undefined", "Financial", "Customers", "Organizational Growth",
            "Internal","Personal Growth","Other" };
    public static final String[] GOAL_AREACLASSES = { "UndefinedArea", "Financial", "Customers", "OrganizationalGrowth",
            "Internal","PersonalGrowth","Other" };


    public static final String[] GOAL_STATUSES = {"Initiated","Researching","On track","Stalled","Deferred","Archived"};
    public static final String[] GOAL_STATUS_COLORS = {"info","warning","success","danger","primary","secondary"};

    /**
     * Get a string representation of a goal area
     *
     * @param current
     * @return String
     */
    public static final String getAreaAsString(int current) {
        String v = null;

        try {

            v = (String) GOAL_AREAS[current];

        } catch (Exception e) {
            v = (String) GOAL_AREAS[0];
        }
        return v;
    }

    public static final String getAreaClassAsString(int current) {
        String v = null;

        try {

            v = (String) GOAL_AREACLASSES[current];

        } catch (Exception e) {
            v = (String) GOAL_AREACLASSES[0];
        }
        return v;
    }



    /**
     * Get areas a basic HTML select list.
     * @param current
     * @return
     */
    public static final String goalAreasAsSelection(int current)
    {
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < GOAL_AREAS.length; i++) {
            String area = (String)GOAL_AREAS[i];
            if(i==current) {

                sb.append("<option selected value="+i+">"+area+"</option>");

            } else {

                sb.append("<option value="+i+">"+area+"</option>");

            }
        }

        return sb.toString();
    }

    /**
     * Get a string representation of a goal status
     *
     * @param current
     * @return String
     */
    public static final String getGoalStatusAsString(int current) {
        String v = null;

        try {

            v = (String) GOAL_STATUSES[current];

        } catch (Exception e) {
            v = (String) GOAL_STATUSES[0];
        }
        return v;
    }

    public static final String getGoalStatusColorAsString(int current) {
        String v = null;

        try {

            v = (String) GOAL_STATUS_COLORS[current];

        } catch (Exception e) {
            v = (String) GOAL_STATUS_COLORS[0];
        }
        return v;
    }

    /**
     * Get goal statuses a basic HTML select list.
     * @param current
     * @return
     */
    public static final String goalStatusAsSelection(int current)
    {
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < GOAL_STATUSES.length; i++) {
            String status = (String)GOAL_STATUSES[i];
            if(i==current) {

                sb.append("<option selected value="+i+">"+status+"</option>");

            } else {

                sb.append("<option value="+i+">"+status+"</option>");

            }
        }

        return sb.toString();
    }



}
