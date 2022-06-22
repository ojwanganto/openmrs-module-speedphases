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
import org.openmrs.EncounterType;
import org.openmrs.Obs;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.ListResult;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.kenyacore.calculation.AbstractPatientCalculation;
import org.openmrs.module.kenyacore.calculation.CalculationUtils;
import org.openmrs.module.kenyacore.calculation.Calculations;
import org.openmrs.module.kenyaemr.Dictionary;
import org.openmrs.module.kenyaemr.calculation.EmrCalculationUtils;
import org.openmrs.module.kenyaemr.metadata.HivMetadata;
import org.openmrs.module.metadatadeploy.MetadataUtils;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class SpeedPhasesWeightAtEnrollmentCalculation extends AbstractPatientCalculation {

    /**
     * @see org.openmrs.calculation.patient.PatientCalculation#evaluate(Collection, Map, PatientCalculationContext)
     */
    @Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> parameterValues, PatientCalculationContext context) {

        Concept currentHeight = Dictionary.getConcept(Dictionary.WEIGHT_KG);
        CalculationResultMap hivEnrollmentMap = Calculations.firstEncounter(MetadataUtils.existing(EncounterType.class, HivMetadata._EncounterType.HIV_ENROLLMENT), cohort, context);

        CalculationResultMap heightObss = Calculations.allObs(currentHeight, cohort, context);

        CalculationResultMap ret = new CalculationResultMap();
        for (Integer ptId : cohort) {
            SimpleResult result = null;
            Date enrollmentDate = ((Encounter) hivEnrollmentMap.get(ptId).getValue()).getEncounterDatetime();

            ListResult heightObsResult = (ListResult) heightObss.get(ptId);

            if (enrollmentDate != null && heightObsResult != null && !heightObsResult.isEmpty()) {
                List<Obs> weight = CalculationUtils.extractResultValues(heightObsResult);
                Obs lastBeforeEnrollment = EmrCalculationUtils.findLastOnOrBefore(weight, enrollmentDate);

                if (lastBeforeEnrollment != null) {
                    Double heightValue = lastBeforeEnrollment.getValueNumeric();
                    if (heightValue != null) {
                        result = new SimpleResult(heightValue, this);
                    }
                }
            }

            ret.put(ptId, result);
        }
        return ret;
    }
}