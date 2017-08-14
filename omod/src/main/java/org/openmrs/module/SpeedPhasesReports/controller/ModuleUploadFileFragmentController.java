package org.openmrs.module.SpeedPhasesReports.controller;

import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * controller for cohort import fragment
 */
public class ModuleUploadFileFragmentController {
    public void controller(FragmentModel model){
        /*AdministrationService as = Context.getAdministrationService();
        String folderName = as.getGlobalProperty("SpeedPhasesReports.cohort_file_dir");
        String csvFilename = "testCohort.csv";
        File loaddir = OpenmrsUtil.getDirectoryInApplicationDataDirectory(folderName);
        File csvFile = new File(loaddir, csvFilename);
        String fileExists = "No cohort file found. Please upload a cohort file in csv format. Refer to the side pane for more direction.";

        if (csvFile.exists())
            fileExists = "A cohort file already exists. Please upload a fresh one to override the existing if required";*/

        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("YYYY-MM-dd");

        ReportingConfiguration configuration= new ReportingConfiguration(new Date(), new Date());

        model.put("command", configuration);

    }
    public void uploadFile (@RequestParam("cohortFile") MultipartFile cohortFile, UiUtils ui) {
        // find the directory to put the file in
        AdministrationService as = Context.getAdministrationService();
        String folderName = as.getGlobalProperty("SpeedPhasesReports.cohort_file_dir");

        String csvFilename = "testCohort.csv";
        File loaddir = OpenmrsUtil.getDirectoryInApplicationDataDirectory(folderName);
        File csvFile = new File(loaddir, csvFilename);


        try {
            cohortFile.transferTo(csvFile);
            System.out.println("Cohort file was successfully uploaded. You can now run SpeedPhasesReports from Reports app on the main page");
        } catch (IOException e) {
            System.out.println("Sorry, the file could not be copied");
            e.printStackTrace();
        }
    }

    class ReportingConfiguration {
        private Date startDate;
        private Date endDate;

        public ReportingConfiguration(Date startDate, Date endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public Date getStartDate() {
            return startDate;
        }

        public void setStartDate(Date startDate) {
            this.startDate = startDate;
        }

        public Date getEndDate() {
            return endDate;
        }

        public void setEndDate(Date endDate) {
            this.endDate = endDate;
        }
    }
}
