package com.hatchery.goals;
import java.sql.Date;
import java.util.ArrayList;
import com.rova.accounts.User;

public record Goal(int goalid,
                   String title,
                   int parentid,
                   int area,
                   Date starts,
                   Date due, int owner,
                   int teamid, int status,
                   boolean isTemplate,
                   String goal,
                   int progressSource, int progress, int privacy,
                   String goalkey, int author, double budget, double spend,
                   ArrayList<User> acl)
{
}
