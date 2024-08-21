package com.hatchery.goals;

import java.sql.*;
import com.rova.Rova;
import com.rova.accounts.User;

import java.util.ArrayList;
import com.hatchery.Hatchery;

public class Goals {

    private Rova rova = null;
    private Hatchery hatchery = null;

    public Goals(Rova rova, Hatchery hatchery) {
        this.rova = rova;
    }

    /**
     * Does this account have any goals? If not, we want to force at least one.
     *
     * @return Boolean
     */
    public boolean hasGoals() {

        boolean has = false;

        if (rova.active()) {

            Connection c = rova.c();
            Statement s = null;
            ResultSet rs = null;

            try {

                s = c.createStatement();

                int aid = rova.user().getAccountId();

                String sql = "select count(*) from hatchery_goals where accountid=" + aid + "";

                rs = s.executeQuery(sql);

                while (rs.next()) {
                    if (rs.getInt(1) > 0) {
                        has = true;
                    }

                }

            } catch (Exception e) {

                rova.log("Errors determining whether an account established any goals. Err:" + e.toString(), "Goals",
                        "hasGoals", 1);

            } finally {

                rova.cleanup(c, s, rs);

            }

        }

        return has;

    }

    private String generateGoalKey() {
        return "g-" + rova.user().getAccountId() + "" + rova.user().getUserId() + ""
                + com.rova.utils.Math.getRandom5Numbers();
    }

    /**
     * Get a specific GOAL.
     *
     * @param goalid
     * @return
     */
    public Goal goal(String goalid) {
        Goal goal = null;

        Connection c = rova.c();
        Statement s = null;
        ResultSet rs = null;

        try {

            int gid = rova.d(goalid);

            if (gid > 0) {

                s = c.createStatement();

                String sql = "select title,parentid,area,starts,due,owner,teamid, status, istemplate, goal,progress_source,progress,privacy,aliaskey,author,budget,spend"
                        + " from hatchery_goals where goalid=" + gid + "";

                rs = s.executeQuery(sql);
                while (rs.next()) {

                    String t = rs.getString(1);
                    int p = rs.getInt(2);
                    int a = rs.getInt(3);
                    java.sql.Date start = rs.getDate(4);
                    java.sql.Date due = rs.getDate(5);
                    int owner = rs.getInt(6);
                    int team = rs.getInt(7);
                    int status = rs.getInt(8);
                    boolean isTemplate = rs.getInt(9) == 1 ? true : false;
                    String goaldesc = rs.getString(10);
                    int psource = rs.getInt(11);
                    int progress = rs.getInt(12);
                    int privacy = rs.getInt(13);
                    String key = rs.getString(14);
                    int author = rs.getInt(15);
                    double budget = rs.getDouble(16);
                    double spend = rs.getDouble(17);

                    /** get acls **/
                    ArrayList<com.rova.accounts.User> acls = getAcls(gid);

                    goal = new Goal(gid, t, p, a, start, due, owner, team, status, isTemplate, goaldesc, psource,
                            progress, privacy, key, author,budget, spend, acls);

                }

            }
        } catch (Exception e) {
            rova.log("Errors getting goal. Err:" + e.toString(), "Goals", "goal", 1);
        } finally {
            rova.cleanup(c, s, rs);
        }

        return goal;
    }

    public ArrayList<Goal> subGoals(String parentid) {
        ArrayList<Goal> goals = new ArrayList<Goal>();

        Connection c = rova.c();
        Statement s = null;
        ResultSet rs = null;

        try {

            int pid = rova.d(parentid);

            if (pid > 0) {

                s = c.createStatement();

                String sql = "select goalid, title,area,starts,due,owner,teamid, status, istemplate, goal,progress_source,progress,privacy,aliaskey,author, budget,spend"
                        + " from hatchery_goals where parentid=" + pid + "";

                rova.log(sql);

                rs = s.executeQuery(sql);
                while (rs.next()) {

                    int gid = rs.getInt(1);
                    String t = rs.getString(2);
                    int a = rs.getInt(3);
                    java.sql.Date start = rs.getDate(4);
                    java.sql.Date due = rs.getDate(5);
                    int owner = rs.getInt(6);
                    int team = rs.getInt(7);
                    int status = rs.getInt(8);
                    boolean isTemplate = rs.getInt(9) == 1 ? true : false;
                    String goaldesc = rs.getString(10);
                    int psource = rs.getInt(11);
                    int progress = rs.getInt(12);
                    int privacy = rs.getInt(13);
                    String key = rs.getString(14);
                    int author = rs.getInt(15);
                    double budget=rs.getDouble(16);
                    double spend = rs.getDouble(17);

                    /** get acls **/
                    ArrayList<com.rova.accounts.User> acls = getAcls(gid);

                    Goal goal = new Goal(gid, t, pid, a, start, due, owner, team, status, isTemplate, goaldesc, psource,
                            progress, privacy, key, author, budget,spend,acls);
                    goals.add(goal);

                }

            }
        } catch (Exception e) {
            rova.log("Errors getting subgoals. Err:" + e.toString(), "Goals", "subGoals", 1);
        } finally {
            rova.cleanup(c, s, rs);
        }

        return goals;
    }



    /**
     * Can this user see this goal
     *
     * @param goal
     * @return
     */
    private boolean hasAccess(Goal goal) {
        return true;
    }

    private boolean hasAccess(Goal goal, int userid) {
        return true;
    }

    public boolean hasAccess(Goal goal, String userid) {
        return true;
    }

    /**
     * JSON representation of goals
     * @return
     */
    public String goalsAsString() {
        ArrayList<Goal> lst = goals();
        com.google.gson.Gson g = new com.google.gson.Gson();
        return g.toJson(lst);
    }

    /**
     * Get all goals I have access to
     *
     * @return
     */
    public ArrayList<Goal> goals() {
        ArrayList<Goal> goals = new ArrayList<Goal>();

        Connection c = rova.c();
        Statement s = null;
        ResultSet rs = null;

        try {

            s = c.createStatement();

            String sql = "select goalid, title, parentid, area,starts,due,owner,teamid, status, istemplate, goal,"
                    + "progress_source,progress,privacy,aliaskey,author, budget, spend"
                    + " from hatchery_goals order by due asc";

            rs = s.executeQuery(sql);
            while (rs.next()) {

                int gid = rs.getInt(1);
                String t = rs.getString(2);
                int pid = rs.getInt(3);
                int a = rs.getInt(4);
                java.sql.Date start = rs.getDate(5);
                java.sql.Date due = rs.getDate(6);
                int owner = rs.getInt(7);
                int team = rs.getInt(8);
                int status = rs.getInt(9);
                boolean isTemplate = rs.getInt(10) == 1 ? true : false;
                String goaldesc = rs.getString(11);
                int psource = rs.getInt(12);
                int progress = rs.getInt(13);
                int privacy = rs.getInt(14);
                String key = rs.getString(15);
                int author = rs.getInt(16);
                double budget = rs.getDouble(17);
                double spend = rs.getDouble(18);

                /** get acls **/
                ArrayList<com.rova.accounts.User> acls = getAcls(gid);

                Goal goal = new Goal(gid, t, pid, a, start, due, owner, team, status, isTemplate, goaldesc, psource,
                        progress, privacy, key, author, budget, spend, acls);

                if (hasAccess(goal)) {
                    goals.add(goal);
                }

            }

        } catch (Exception e) {
            rova.log("Errors getting GOALS. Err:" + e.toString(), "Goals", "goals", 1);
        } finally {
            rova.cleanup(c, s, rs);
        }

        return goals;
    }




    /**
     * Get all goals aligned to a specific area
     * @param areaid
     * @return
     */
    public ArrayList<Goal> goalsByArea(String areaid) {
        ArrayList<Goal> goals = new ArrayList<Goal>();

        int iArea = rova.d(areaid);
        Connection c = rova.c();
        Statement s = null;
        ResultSet rs = null;

        try {

            s = c.createStatement();

            String sql = "select goalid, title, parentid, area,starts,due,owner,teamid, status, istemplate, goal,"
                    + "progress_source,progress,privacy,aliaskey,author, budget, spend"
                    + " from hatchery_goals where area=" + iArea + " order by due asc";

            rs = s.executeQuery(sql);
            while (rs.next()) {

                int gid = rs.getInt(1);
                String t = rs.getString(2);
                int pid = rs.getInt(3);
                int a = rs.getInt(4);
                java.sql.Date start = rs.getDate(5);
                java.sql.Date due = rs.getDate(6);
                int owner = rs.getInt(7);
                int team = rs.getInt(8);
                int status = rs.getInt(9);
                boolean isTemplate = rs.getInt(10) == 1 ? true : false;
                String goaldesc = rs.getString(11);
                int psource = rs.getInt(12);
                int progress = rs.getInt(13);
                int privacy = rs.getInt(14);
                String key = rs.getString(15);
                int author = rs.getInt(16);
                double budget = rs.getDouble(17);
                double spend = rs.getDouble(18);

                /** get acls **/
                ArrayList<com.rova.accounts.User> acls = getAcls(gid);

                Goal goal = new Goal(gid, t, pid, a, start, due, owner, team, status, isTemplate, goaldesc, psource,
                        progress, privacy, key, author, budget, spend, acls);

                if (hasAccess(goal)) {
                    goals.add(goal);
                }

            }

        } catch (Exception e) {
            rova.log("Errors getting GOALS by Area. Err:" + e.toString(), "Goals", "goalsByArea", 1);
        } finally {
            rova.cleanup(c, s, rs);
        }

        return goals;
    }


    /**
     * Goals where there is a specific owner
     * @return
     */
    public ArrayList<Goal> goalsOwner(String userid) {
        ArrayList<Goal> goals = new ArrayList<Goal>();

        int iOwner = rova.d(userid);

        Connection c = rova.c();
        Statement s = null;
        ResultSet rs = null;

        try {

            s = c.createStatement();

            String sql = "select goalid, title, parentid, area,starts,due,owner,teamid, status, istemplate, goal,"
                    + "progress_source,progress,privacy,aliaskey,author, budget, spend"
                    + " from hatchery_goals where owner=" + iOwner + " order by due asc";

            rs = s.executeQuery(sql);
            while (rs.next()) {

                int gid = rs.getInt(1);
                String t = rs.getString(2);
                int pid = rs.getInt(3);
                int a = rs.getInt(4);
                java.sql.Date start = rs.getDate(5);
                java.sql.Date due = rs.getDate(6);
                int owner = rs.getInt(7);
                int team = rs.getInt(8);
                int status = rs.getInt(9);
                boolean isTemplate = rs.getInt(10) == 1 ? true : false;
                String goaldesc = rs.getString(11);
                int psource = rs.getInt(12);
                int progress = rs.getInt(13);
                int privacy = rs.getInt(14);
                String key = rs.getString(15);
                int author = rs.getInt(16);
                double budget = rs.getDouble(17);
                double spend = rs.getDouble(18);

                /** get acls **/
                ArrayList<com.rova.accounts.User> acls = getAcls(gid);

                Goal goal = new Goal(gid, t, pid, a, start, due, owner, team, status, isTemplate, goaldesc, psource,
                        progress, privacy, key, author, budget, spend, acls);

                if (hasAccess(goal)) {
                    goals.add(goal);
                }

            }

        } catch (Exception e) {
            rova.log("Errors getting GOALS where there is a specific owner. Err:" + e.toString(), "Goals", "goalsOwner", 1);
        } finally {
            rova.cleanup(c, s, rs);
        }

        return goals;
    }

    public ArrayList<Goal> myPrivateGoals(String userid) {
        ArrayList<Goal> goals = new ArrayList<Goal>();

        int iOwner = rova.d(userid);

        Connection c = rova.c();
        Statement s = null;
        ResultSet rs = null;

        try {

            s = c.createStatement();

            String sql = "select goalid, title, parentid, area,starts,due,owner,teamid, status, istemplate, goal,"
                    + "progress_source,progress,privacy,aliaskey,author, budget, spend"
                    + " from hatchery_goals where owner=" + iOwner + " and privacy=0 order by due asc";

            rs = s.executeQuery(sql);
            while (rs.next()) {

                int gid = rs.getInt(1);
                String t = rs.getString(2);
                int pid = rs.getInt(3);
                int a = rs.getInt(4);
                java.sql.Date start = rs.getDate(5);
                java.sql.Date due = rs.getDate(6);
                int owner = rs.getInt(7);
                int team = rs.getInt(8);
                int status = rs.getInt(9);
                boolean isTemplate = rs.getInt(10) == 1 ? true : false;
                String goaldesc = rs.getString(11);
                int psource = rs.getInt(12);
                int progress = rs.getInt(13);
                int privacy = rs.getInt(14);
                String key = rs.getString(15);
                int author = rs.getInt(16);
                double budget = rs.getDouble(17);
                double spend = rs.getDouble(18);

                /** get acls **/
                ArrayList<com.rova.accounts.User> acls = getAcls(gid);

                Goal goal = new Goal(gid, t, pid, a, start, due, owner, team, status, isTemplate, goaldesc, psource,
                        progress, privacy, key, author, budget, spend, acls);

                if (hasAccess(goal)) {
                    goals.add(goal);
                }

            }

        } catch (Exception e) {
            rova.log("Errors getting PRIVATE GOALS. Err:" + e.toString(), "Goals", "goamyPrivateGoalssOwner", 1);
        } finally {
            rova.cleanup(c, s, rs);
        }

        return goals;
    }



    /*
     * Get access control lists for a specific goal.
     */
    private ArrayList<com.rova.accounts.User> getAcls(int goalid) {
        ArrayList<com.rova.accounts.User> lst = new ArrayList<com.rova.accounts.User>();

        return lst;
    }

    /** Method for adding a quick goal **/
    public String addQuickGoal(jakarta.servlet.http.HttpServletRequest r) {
        String goalid = null;
        if (rova.active()) {

            String title = r.getParameter("title");
            title = com.rova.Rova.clean(title);
            if (title.length() > 0) {

                String area = r.getParameter("area");
                int iArea = com.rova.utils.Strings.getIntFromString(area);
                String privacy = r.getParameter("privacy");
                int iPrivacy = com.rova.utils.Strings.getIntFromString(privacy);
                String userid = r.getParameter("userid");
                int iUserid = rova.d(userid);
                String starts = r.getParameter("starts");
                starts = com.rova.utils.Calendar.clean(starts);
                String ends = r.getParameter("ends");
                ends = com.rova.utils.Calendar.clean(ends);
                String description = r.getParameter("description");
                description = com.rova.Rova.clean(description);
                int author = rova.user().getUserId();

                String budget = r.getParameter("budget"); if(budget==null) { budget="0"; }
                double dblBudget = com.rova.utils.Math.getDouble(budget);

                String spend = r.getParameter("spend"); if(spend==null) { spend="0"; }
                double dblSpend = com.rova.utils.Math.getDouble(spend);


                String parentid = r.getParameter("parentid");
                int iParent = rova.d(parentid);

                Connection c = rova.c();
                ResultSet rs = null;
                PreparedStatement ps = null;

                try {

                    int aid = rova.user().getAccountId();

                    String key = generateGoalKey();
                    String sql = "insert into hatchery_goals values(null," + aid + ",'" + title + "'," + iParent + ","
                            + "" + iArea + ",'" + starts + "','" + ends + "'," + iUserid + ",0,0,0,'" + description
                            + "',0,0," + iPrivacy + ",'" + key + "'," + author + "," + dblBudget +","+dblSpend+")";

                    ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    ps.executeUpdate();

                    rs = ps.getGeneratedKeys();

                    if (rs.next()) {
                        goalid = rova.e(rs.getInt(1));
                    }

                } catch (Exception e) {
                    rova.log("Errors adding quick goal. Err:" + e.toString(), "Goals", "addQuickGoal", 1);
                } finally {
                    rova.cleanup(c, ps);
                }

            }

        }

        return goalid;
    }


}