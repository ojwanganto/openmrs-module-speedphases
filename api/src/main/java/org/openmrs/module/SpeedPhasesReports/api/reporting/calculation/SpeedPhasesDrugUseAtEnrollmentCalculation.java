/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.SpeedPhasesReports.api.reporting.calculation;

import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.api.*;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.SpeedPhasesReports.api.reporting.builder.LisheBoraHivVisitReportBuilder;
import org.openmrs.module.kenyacore.calculation.AbstractPatientCalculation;
import org.openmrs.module.kenyaemr.metadata.HivMetadata;
import org.openmrs.ui.framework.SimpleObject;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 */
public class SpeedPhasesDrugUseAtEnrollmentCalculation extends AbstractPatientCalculation {

    String DRUG_HISTORY_GROUPING_CONCEPT = "160741AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    String PMTCT_DRUG = "1148AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    String PEP_DRUG= "1691AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    String YES_CONCEPT= "1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    String PREP_REGIMEN_CONCEPT= "1087AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    String PEP_REGIMEN_CONCEPT= "1088AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    String PMTCT_REGIMEN_CONCEPT= "966AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    Integer PREP_DRUG= 165269;
    /**
     * @see org.openmrs.calculation.patient.PatientCalculation#evaluate(Collection, Map, PatientCalculationContext)
     */
    @Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> parameterValues, PatientCalculationContext context) {

        PatientService patientService = Context.getPatientService();
        PersonService personService = Context.getPersonService();
        EncounterService encounterService = Context.getEncounterService();
        ObsService obsService = Context.getObsService();
        ConceptService conceptService = Context.getConceptService();

        CalculationResultMap ret = new CalculationResultMap();
        for (Integer ptId : cohort) {

            SimpleResult result = null;
            List<SimpleObject> drugHistoryList = new ArrayList<SimpleObject>();

            // get last hiv enrollment encounter
            List<Encounter> hivEnrollmentEncounters = encounterService.getEncounters(
                    patientService.getPatient(ptId),
                    null,
                    null,
                    null,
                    Arrays.asList(Context.getFormService().getFormByUuid(HivMetadata._Form.HIV_ENROLLMENT)),
                    null,
                    null,
                    null,
                    null,
                    false
            );

            Encounter lastHivEnrollmentEncounter =  hivEnrollmentEncounters.size() > 0 ? hivEnrollmentEncounters.get(hivEnrollmentEncounters.size() - 1) : null;

            if (lastHivEnrollmentEncounter != null) {
                List<Obs> obs = obsService.getObservations(
                        Arrays.asList(personService.getPerson(ptId)),
                        Arrays.asList(lastHivEnrollmentEncounter),
                        Arrays.asList(conceptService.getConceptByUuid(DRUG_HISTORY_GROUPING_CONCEPT)),
                        null,
                        null,
                        null,
                        Arrays.asList("obsId"),
                        null,
                        null,
                        null,
                        null,
                        false
                );

                for(Obs o: obs) {
                    SimpleObject history = populateDrugUseHistory(o.getGroupMembers());
                    if (history.get("purpose") != null) {
                        drugHistoryList.add(history);
                    }
                }
            }

            result = new SimpleResult(drugHistoryList, this);
            ret.put(ptId, result);

        }

        return ret;
    }

    private SimpleObject populateDrugUseHistory(Set<Obs> groupMembers) {

        SimpleDateFormat df = new SimpleDateFormat(LisheBoraHivVisitReportBuilder.DATE_FORMAT);
        SimpleObject history = new SimpleObject();
        for (Obs obs : groupMembers) {
            if (obs.getConcept().getUuid().equals(PMTCT_DRUG) && obs.getValueCoded().getUuid().equals(YES_CONCEPT)) {
                history.put("purpose", "PMTCT");
            } else if (obs.getConcept().getUuid().equals(PEP_DRUG) && obs.getValueCoded().getUuid().equals(YES_CONCEPT)) {
                history.put("purpose", "PEP");
            } else if (obs.getConcept().getConceptId().equals(PREP_DRUG) && obs.getValueCoded().getUuid().equals(YES_CONCEPT)) {
                history.put("purpose", "PREP");
            } else if (obs.getConcept().getUuid().equals(PREP_REGIMEN_CONCEPT) || obs.getConcept().getUuid().equals(PEP_REGIMEN_CONCEPT) || obs.getConcept().getUuid().equals(PMTCT_REGIMEN_CONCEPT)) {
                history.put("regimen", getDrugRegimen(obs.getValueCoded()));
                history.put("dateLastUsed", df.format(obs.getObsDatetime()));
            }
        }

        return history;
    }

    private String getDrugRegimen(Concept ans) {

        if (ans == null)
            return "";

        String regimenName = "";
        if (ans.getUuid().equals("162559AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")) {
                regimenName = "ABC/DDI/LPV/r";
        }
        else if (ans.getUuid().equals("162562AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")) {
            regimenName = "ABC/LPV/R/TDF";
        }
        else if (ans.getUuid().equals("161361AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")) {
            regimenName = "EDF/3TC/EFV";
        }
        else if (ans.getUuid().equals("792AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")) {
            regimenName = "D4T/3TC/NVP";
        }
        else if (ans.getUuid().equals("9fb85385-b4fb-468c-b7c1-22f75834b4b0")) {
            regimenName = "TDF/3TC/DTG";
        }
        else if (ans.getUuid().equals("162200AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")) {
            regimenName = "3TC/ABC/LPV/r";
        }
        else if (ans.getUuid().equals("4dc0119b-b2a6-4565-8d90-174b97ba31db")) {
            regimenName = "ABC/3TC/DTG";
        }
        else if (ans.getUuid().equals("164511AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")) {
            regimenName = "AZT-3TC-ATV/r";
        }
        else if (ans.getUuid().equals("162563AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")) {
            regimenName = "3TC/ABC/EFV";
        }
        else if (ans.getUuid().equals("6dec7d7d-0fda-4e8d-8295-cb6ef426878d")) {
            regimenName = "AZT/3TC/DTG";
        }
        else if (ans.getUuid().equals("164505AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")) {
            regimenName = "TDF-3TC-EFV";
        }
        else if (ans.getUuid().equals("817AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")) {
            regimenName = "ABC/3TC/AZT";
        }
        else if (ans.getUuid().equals("1652AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")) {
            regimenName = "3TC/NVP/AZT";
        }
        else if (ans.getUuid().equals("162560AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")) {
            regimenName = "3TC/D4T/LPV/r";
        }
        else if (ans.getUuid().equals("162199AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")) {
            regimenName = "ABC/NVP/3TC";
        }
        else if (ans.getUuid().equals("164512AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")) {
        regimenName = "TDF-3TC-ATV/r";
        }
        else if (ans.getUuid().equals("104565AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")) {
            regimenName = "EFV/FTC/TDF";
        }
        else if (ans.getUuid().equals("162201AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")) {
            regimenName = "3TC/LPV/TDF/r";
        }
        else if (ans.getUuid().equals("162565AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")) {
            regimenName = "3TC/NVP/TDF";
        }
        else if (ans.getUuid().equals("162561AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")) {
            regimenName = "3TC/AZT/LPV/r";
        }
        else if (ans.getUuid().equals("160124AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")) {
            regimenName = "AZT/3TC/EFV";
        }
        else if (ans.getUuid().equals("160104AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")) {
            regimenName = "D4T/3TC/EFV";
        }
        return regimenName;
    }
}