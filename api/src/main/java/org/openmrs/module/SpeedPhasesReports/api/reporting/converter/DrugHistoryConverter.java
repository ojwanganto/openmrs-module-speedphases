package org.openmrs.module.SpeedPhasesReports.api.reporting.converter;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.calculation.result.CalculationResult;
import org.openmrs.module.reporting.data.converter.DataConverter;
import org.openmrs.ui.framework.SimpleObject;

import java.util.List;

/**
 * Converts date element to custom regimen format
 */
public class DrugHistoryConverter implements DataConverter {

    private String reportVariable;
    private String purpose;

    public DrugHistoryConverter(String reportVariable, String purpose) {
        this.reportVariable = reportVariable;
        this.purpose = purpose;
    }

    @Override
    public Object convert(Object obj) {

        if (obj == null)
            return "";

        Object value = ((CalculationResult) obj).getValue();
        List<SimpleObject> drugList  =  (List<SimpleObject>) value;

        if (drugList == null || drugList.isEmpty() || StringUtils.isBlank(reportVariable)) {
                return "";
            }

            return checkPurpose(drugList, reportVariable, purpose);
    }

    private String checkPurpose(List<SimpleObject> drugList, String reportVariable, String purpose) {
        if (purpose.equals("PMTCT") && reportVariable.equals("purpose")) {
            for(int i = 0; i < drugList.size(); i++) {
                SimpleObject obj = drugList.get(i);
                if (obj != null && obj.get("purpose").equals("PMTCT")) {
                    return "PMTCT";
                }

            }
        }

        else if (purpose.equals("PMTCT") && reportVariable.equals("regimen")) {
            for(int i = 0; i < drugList.size(); i++) {
                SimpleObject obj = drugList.get(i);
                if (obj != null && obj.get("purpose").equals("PMTCT")) {
                    return obj.get("regimen").toString();
                }

            }
        }
        else if (purpose.equals("PMTCT") && reportVariable.equals("date")) {
            for(int i = 0; i < drugList.size(); i++) {
                SimpleObject obj = drugList.get(i);
                if (obj != null && obj.get("purpose").equals("PMTCT")) {
                    return obj.get("dateLastUsed").toString();
                }

            }
        }

        if (purpose.equals("PEP") && reportVariable.equals("purpose")) {
            for(int i = 0; i < drugList.size(); i++) {
                SimpleObject obj = drugList.get(i);
                if (obj != null && obj.get("purpose").equals("PEP")) {
                    return "PEP";
                }

            }
        }

        else if (purpose.equals("PEP") && reportVariable.equals("regimen")) {
            for(int i = 0; i < drugList.size(); i++) {
                SimpleObject obj = drugList.get(i);
                if (obj != null && obj.get("purpose").equals("PEP")) {
                    return obj.get("regimen").toString();
                }

            }
        }
        else if (purpose.equals("PEP") && reportVariable.equals("date")) {
            for(int i = 0; i < drugList.size(); i++) {
                SimpleObject obj = drugList.get(i);
                if (obj != null && obj.get("purpose").equals("PEP")) {
                    return obj.get("dateLastUsed").toString();
                }

            }
        }

        if (purpose.equals("PREP") && reportVariable.equals("purpose")) {
            for(int i = 0; i < drugList.size(); i++) {
                SimpleObject obj = drugList.get(i);
                if (obj != null && obj.get("purpose").equals("PREP")) {
                    return "PREP";
                }

            }
        }

        else if (purpose.equals("PREP") && reportVariable.equals("regimen")) {
            for(int i = 0; i < drugList.size(); i++) {
                SimpleObject obj = drugList.get(i);
                if (obj != null && obj.get("purpose").equals("PREP")) {
                    return obj.get("regimen").toString();
                }

            }
        }
        else if (purpose.equals("PREP") && reportVariable.equals("date")) {
            for(int i = 0; i < drugList.size(); i++) {
                SimpleObject obj = drugList.get(i);
                if (obj != null && obj.get("purpose").equals("PREP")) {
                    return obj.get("dateLastUsed").toString();
                }

            }
        }
        return "";
    }
    @Override
    public Class<?> getInputDataType() {
        return Integer.class;
    }

    @Override
    public Class<?> getDataType() {
        return String.class;
    }

    public String getReportVariable() {
        return reportVariable;
    }

    public void setReportVariable(String reportVariable) {
        this.reportVariable = reportVariable;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
}