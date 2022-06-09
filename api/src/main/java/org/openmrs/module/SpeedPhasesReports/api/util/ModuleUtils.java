package org.openmrs.module.SpeedPhasesReports.api.util;

import au.com.bytecode.opencsv.CSVReader;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifierType;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.kenyaemr.metadata.CommonMetadata;
import org.openmrs.module.kenyaemr.metadata.HivMetadata;
import org.openmrs.util.OpenmrsUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Util class for HRSReports
 */
public class ModuleUtils {
    public static final String HEI_UNIQUE_NUMBER = "0691f522-dd67-4eeb-92c8-af5083baf338";

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

    public static List<Integer> getLisheBoraCohort() {
        AdministrationService as = Context.getAdministrationService();

        String folderName = "lisheBoraCohort.csv";//as.getGlobalProperty(GP_LOCAL_DIRECTORY, DEFAULT_FRONTEND_DIRECTORY);
        PatientService patientService = Context.getPatientService();
        PatientIdentifierType heiIdentifier = patientService.getPatientIdentifierTypeByUuid(HEI_UNIQUE_NUMBER);

        List<Integer> cohort = new ArrayList<Integer>();

        try  {
            CSVReader csvReader = new CSVReader(new FileReader("/var/lib/OpenMRS/lisheBoraList.csv"));
            String[] values = null;
            while ((values = csvReader.readNext()) != null) {
                List<Patient> patientsForIdentifier = patientService.getPatients(null, values[0], Arrays.asList(heiIdentifier), true);
                if (patientsForIdentifier.size() > 0) {
                    cohort.add(patientsForIdentifier.get(0).getPatientId());
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(cohort);
        return cohort;
    }


}
