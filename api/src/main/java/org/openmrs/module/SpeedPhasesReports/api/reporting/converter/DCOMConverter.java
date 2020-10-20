package org.openmrs.module.SpeedPhasesReports.api.reporting.converter;

import org.openmrs.module.reporting.data.converter.DataConverter;

/**
 * Converts date element to custom regimen format
 */
public class DCOMConverter implements DataConverter {

    @Override
    public Object convert(Object original) {

        Integer d = (Integer) original;

        if (d == null)
            return " ";

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
        if (r.equals(164942)) {
            return "Standard Care";
        } else if (r.equals(164943)) {
            return "Fast Track";
        } else if (r.equals(164944)) {
            return "Community ART Distribution - HCW Led";
        } else if (r.equals(164945)) {
            return "Community ART Distribution - Peer Led";
        } else if (r.equals(164946)) {
            return "Facility ART Distribution Group";
        }
        return "";
    }
}