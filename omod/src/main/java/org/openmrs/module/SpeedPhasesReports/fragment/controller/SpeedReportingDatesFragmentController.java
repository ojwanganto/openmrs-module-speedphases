package org.openmrs.module.SpeedPhasesReports.fragment.controller;

import org.openmrs.GlobalProperty;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * controller for reporting dates fragment
 */
public class SpeedReportingDatesFragmentController {
    public void controller(FragmentModel model){
        AdministrationService as = Context.getAdministrationService();
        String startDate = as.getGlobalProperty("SpeedPhasesReports.startDate");
        String endDate = as.getGlobalProperty("SpeedPhasesReports.endDate");

        model.put("startDate", startDate);
        model.put("endDate", endDate);

    }
    public void saveDates (@RequestParam("startDate") Date startDate, @RequestParam("endDate") Date endDate, UiUtils ui) {

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        AdministrationService as = Context.getAdministrationService();
        as.saveGlobalProperty(new GlobalProperty("SpeedPhasesReports.startDate", sf.format(startDate)));
        as.saveGlobalProperty(new GlobalProperty("SpeedPhasesReports.endDate", sf.format(endDate)));

    }
}
