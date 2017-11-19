package org.openmrs.module.SpeedPhasesReports.api.reporting.model;

import java.util.Date;
import java.util.Set;

/**
 * A class that holds data in cohort csv file
 */
public class ModuleCohortFile {
    private Date effectiveDate;
    private Date endDate;
    private Set<Long> patientIds;

    public ModuleCohortFile() {
    }

    public ModuleCohortFile(Date effectiveDate, Date endDate, Set<Long> patientIds) {
        this.effectiveDate = effectiveDate;
        this.endDate = endDate;
        this.patientIds = patientIds;
    }

    public ModuleCohortFile(Date effectiveDate, Set<Long> patientIds) {
        this.effectiveDate = effectiveDate;
        this.patientIds = patientIds;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Set<Long> getPatientIds() {
        return patientIds;
    }

    public void setPatientIds(Set<Long> patientIds) {
        this.patientIds = patientIds;
    }
}
