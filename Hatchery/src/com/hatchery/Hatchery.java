package com.hatchery;
import com.rova.*;
import com.hatchery.goals.Goals;
import com.hatchery.teams.Teams;

public class Hatchery {

    private boolean onboarded = false;
    private Rova rova   = null;
    private Goals goals = null;
    private Teams teams = null;

    public Hatchery(Rova rova) {
        this.rova=rova;

    }

    public Goals goals() {
        if(goals==null) { goals = new Goals(rova,this);}
        return goals;
    }

    public Teams teams() {
        if(teams==null) { teams = new Teams(rova,this); }
        return teams;
    }



    /**
     * When logging in we first check if the account has been onboarded or not.
     * @return
     */
    public boolean onboarded() {

        if(rova.isActive())
        {
            String typeCompany = rova.accounts().accountProperties().getPropertyValueAsString(rova.user(),"typecompany");
            boolean hasGoals = goals().hasGoals();

            if(hasGoals && typeCompany.length() > 0 )
            {
                onboarded = true;
            }
        }

        return this.onboarded;
    }
}
