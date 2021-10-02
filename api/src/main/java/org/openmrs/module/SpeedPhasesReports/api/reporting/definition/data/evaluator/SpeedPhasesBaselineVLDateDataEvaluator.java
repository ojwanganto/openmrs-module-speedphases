package org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.evaluator;

import org.openmrs.annotation.Handler;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesBaselineVLDateDataDefinition;
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
@Handler(supports=SpeedPhasesBaselineVLDateDataDefinition.class, order=50)
public class SpeedPhasesBaselineVLDateDataEvaluator implements PatientDataEvaluator {

    @Autowired
    private EvaluationService evaluationService;

    public EvaluatedPatientData evaluate(PatientDataDefinition definition, EvaluationContext context) throws EvaluationException {
        EvaluatedPatientData c = new EvaluatedPatientData(definition, context);

        String qry = "select\n" +
                "d.patient_id, d.vl_result_date as vl_result_date\n" +
                "FROM (\n" +
                "select\n" +
                "d.patient_id, left(min(concat(l.visit_date, l.test_result)), 10) as vl_result_date\n" +
                "from kenyaemr_etl.etl_patient_demographics d \n" +
                "inner join kenyaemr_etl.etl_laboratory_extract l on l.patient_id=d.patient_id and l.lab_test=856\n" +
                "group by d.patient_id\n" +
                ") d\n";

        SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
        queryBuilder.append(qry);
        Map<Integer, Object> data = evaluationService.evaluateToMap(queryBuilder, Integer.class, Object.class, context);
        c.setData(data);
        return c;
    }
}
