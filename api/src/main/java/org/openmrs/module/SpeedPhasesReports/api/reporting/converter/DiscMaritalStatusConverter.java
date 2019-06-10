package org.openmrs.module.SpeedPhasesReports.api.reporting.converter;

import org.openmrs.module.reporting.data.converter.DataConverter;

/**
 * Converts date element to custom regimen format
 */
public class DiscMaritalStatusConverter implements DataConverter {

    @Override
    public Object convert(Object original) {

        String d = (String) original;

        if (d == null)
            return " ";

        return formatRegimen(d);
    }

    @Override
    public Class<?> getInputDataType() {
        return String.class;
    }

    @Override
    public Class<?> getDataType() {
        return String.class;
    }

    private String formatRegimen(String r) {
        if (r.equals("Separated")) {
            return "Divorced";
        } else if (r.equals("Polygamous")) {
            return "Married polygamous";
        } else if (r.equals("Never married")) {
            return "Single";
        } else if (r.equals("Married")) {
            return "Married monogamous";
        } else if (r.equals("Living with partner")) {
            return "Cohabiting";
        }
        return r;
    }

    /*from db
    * null
Separated
Polygamous
Never married
Widowed
Married
Divorced
Living with partner



Single, Married polygamous, Married monogamous, Divorced, Widowed, Cohabitating

    * */



}