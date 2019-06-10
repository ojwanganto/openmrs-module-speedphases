package org.openmrs.module.SpeedPhasesReports.api.reporting.converter;

import org.openmrs.module.reporting.data.converter.DataConverter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Converts date element to custom regimen format
 */
public class DiscRegimenConverter implements DataConverter {

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
        if (r.equals("TDF+3TC+EFV")) {
            return "3TC/EFV/TDF";
        } else if (r.equals("3TC+NVP+TDF")) {
            return "3TC/NVP/TDF";
        } else if (r.equals("TDF+3TC+DTG")) {
            return "3TC/DTG/TDF";
        } else if (r.equals("TDF+3TC+LPV/r")) {
            return "3TC/TDF/LPV/r";
        } else if (r.equals("TDF+3TC+ATV/r")) {
            return "3TC/TDF/ATV/r";
        } else if (r.equals("AZT+3TC+NVP")) {
            return "3TC/AZT/NVP";
        } else if (r.equals("D4T+3TC+NVP")) {
            return "3TC/D4T/NVP";
        } else if (r.equals("ABC+3TC+EFV")) {
            return "3TC/ABC/EFV";
        } else if (r.equals("D4T+3TC+EFV")) {
            return "3TC/D4T/EFV";
        } else if (r.equals("AZT+3TC+DTG")) {
            return "3TC/AZT/DTG";
        } else if (r.equals("AZT+3TC+EFV")) {
            return "3TC/AZT/EFV";
        } else if (r.equals("ABC+3TC+NVP")) {
            return "3TC/ABC/NVP";
        } else if (r.equals("ABC+3TC+LPV/r")) {
            return "3TC/ABC/LPV/r";
        } else if (r.equals("AZT+3TC+LPV/r")) {
            return "3TC/AZT/LPV/r";
        } else if (r.equals("AZT+3TC+ATV/r")) {
            return "3TC/AZT/ATV/r";
        }
        return r;
    }

}