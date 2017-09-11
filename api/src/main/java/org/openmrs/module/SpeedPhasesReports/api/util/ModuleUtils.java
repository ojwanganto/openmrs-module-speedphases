package org.openmrs.module.SpeedPhasesReports.api.util;

import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Util class for HRSReports
 */
public class ModuleUtils {

    public static Date startDate() {
        AdministrationService as = Context.getAdministrationService();
        String startDate = as.getGlobalProperty("SpeedPhasesReports.startDate");
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date sdate = (Date)formatter.parse(startDate);
            return sdate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null ;
    }


    public static Date getDefaultEndDate() {
        AdministrationService as = Context.getAdministrationService();
        String endDate = as.getGlobalProperty("SpeedPhasesReports.endDate");
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date edate = (Date)formatter.parse(endDate);
            return edate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null ;
    }

    public static String getInitialCohortQuery () {

        String qry = "select v.visit_id \n" +
                "from patient pt inner join visit v on pt.patient_id = v.patient_id inner join person p on v.patient_id = p.person_id\n" +
                "where v.visit_id is not null or v.visit_id != '' and date(v.date_started) between date(:startDate) and date(:endDate) \n" +
                "and datediff(v.date_started, p.birthdate) div 365.25 between 10 and 24\n" +
                " order by v.patient_id, v.visit_id ";

        return qry;

    }


}
