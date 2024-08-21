package com.hatchery.teams;

import com.rova.Rova;

import java.util.*;
import java.sql.*;

import com.hatchery.Hatchery;

public class Teams {

    private Rova rova = null;
    private Hatchery hatchery = null;

    public Teams(Rova rova, Hatchery hatchery) {
        this.rova = rova;
        this.hatchery = hatchery;
    }


    /**
     * Adds a new team with active status
     *
     * @param Title of the team
     */
    public String addTeam(String title) {
        String teamid = null;
        if (rova.isAdmin() || rova.isContributor()) {

            Connection c = rova.c();
            PreparedStatement ps = null;
            ResultSet rs = null;

            try {

                if (!title.isEmpty()) {
                    title = com.rova.Rova.clean(title);

                    String sql = "insert into hatchery_teams values(null,?,?,1)";

                    ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                    ps.setInt(1, rova.user().getAccountId());
                    ps.setString(2, title);
                    ps.executeUpdate();

                    rs = ps.getGeneratedKeys();

                    if (rs.next()) {
                        teamid = rova.e(rs.getInt(1));
                        rova.audit().audit("Added new team", "addTeam(title)", "Teams", "addTeam");
                    }


                }

            } catch (Exception e) {
                rova.log("Errors adding new team Err:" + e.toString(), "Teams", "addTeam", 1);
            } finally {
                rova.cleanup(c, ps, rs);
            }

        }

        return teamid;
    }

   


    public boolean isMember(String teamid, String userid) {
        return isMember(rova.d(teamid), rova.d(userid));
    }

    /**
     * Is a given user a member of a specific team?
     *
     * @param teamid
     * @param userid
     * @return
     */
    private boolean isMember(int teamid, int userid) {
        boolean is = false;

        if (rova.active()) {
            Connection c = rova.c();
            Statement s = null;
            ResultSet rs = null;
            try {

                int aid = rova.user().getUserId();
                s = c.createStatement();
                rs = s.executeQuery("select count(*) from hatchery_team_members where accountid=" + aid + " and teamid=" + teamid + " and userid=" + userid + "");
                while (rs.next()) {
                    if (rs.getInt(1) > 0) {
                        is = true;
                    }
                }


            } catch (Exception e) {
                rova.log("Errors determining whether a  user is a member of a team. Err:" + e.toString(), "Teams", "isMember", 1);
            } finally {
                rova.cleanup(c, s, rs);
            }
        }
        return is;
    }
}
