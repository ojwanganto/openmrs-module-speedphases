package org.openmrs.module.SpeedPhasesReports.api.reporting.converter;

import org.openmrs.module.reporting.data.converter.DataConverter;

/**
 * Converter for HEI medication given
 */
public class HeiMedicationGivenConverter implements DataConverter {

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
        if (r.equals(80586) || r.equals(86663) || r.equals(105281)) {
            return "Yes";
        }
        return "";
    }
}