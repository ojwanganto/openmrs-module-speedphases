package org.openmrs.module.SpeedPhasesReports.api.reporting.converter;

import org.openmrs.module.reporting.data.converter.DataConverter;

/**
 * Converts HEI Outcome
 */
public class HEIOutcomeConverter implements DataConverter {

    @Override
    public Object convert(Object original) {

        Integer d = (Integer) original;

        if (d == null)
            return "";

        return formatCareModel(d);
    }

    @Override
    public Class<?> getInputDataType() {
        return Integer.class;
    }

    @Override
    public Class<?> getDataType() {
        return String.class;
    }

    private String formatCareModel(Integer r) {
        if (r.equals(138571)) {
            return "Confirmed HIV Positive";
        } else if (r.equals(5240)) {
            return "Lost";
        } else if (r.equals(160432)) {
            return "Dead";
        } else if (r.equals(159492)) {
            return "Transfer Out";
        } else if (r.equals(1403)) {
            return "HIV Neg at age > 18 months";
        }
        return "";
    }
}