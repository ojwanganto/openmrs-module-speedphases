package org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.evaluator;

import org.openmrs.annotation.Handler;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesAgeAtARTStartDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesHasTreatmentSupporterDataDefinition;
import org.openmrs.module.reporting.data.patient.EvaluatedPatientData;
import org.openmrs.module.reporting.data.patient.definition.PatientDataDefinition;
import org.openmrs.module.reporting.data.patient.evaluator.PatientDataEvaluator;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.querybuilder.SqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Evaluates a VisitIdDataDefinition to produce a VisitData
 */
@Handler(supports=SpeedPhasesAgeAtARTStartDataDefinition.class, order=50)
public class SpeedPhasesAgeAtARTStartDataEvaluator implements PatientDataEvaluator {

    @Autowired
    private EvaluationService evaluationService;

    public EvaluatedPatientData evaluate(PatientDataDefinition definition, EvaluationContext context) throws EvaluationException {
        EvaluatedPatientData c = new EvaluatedPatientData(definition, context);

        String qry = "select patient_id, TIMESTAMPDIFF(YEAR, DOB, start_art_date) AS \"AgeAtArtStart\"\n" +
                "from(\n" +
                "select \n" +
                "  d.patient_id, min(de.date_started) start_art_date, d.DOB\n" +
                "  from kenyaemr_etl.etl_patient_demographics d  \n" +
                "  inner join kenyaemr_etl.etl_hiv_enrollment e on e.patient_id=d.patient_id and e.voided=0 \n" +
                "  left outer join kenyaemr_etl.etl_drug_event de on e.patient_id = de.patient_id\n" +
                "  group by d.patient_id\n" +
                ") t;\n";

        SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
        queryBuilder.append(qry);
        Map<Integer, Object> data = evaluationService.evaluateToMap(queryBuilder, Integer.class, Object.class, context);
        c.setData(data);
        return c;
    }
}