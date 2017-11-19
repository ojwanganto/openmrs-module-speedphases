package org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.evaluator;

import org.openmrs.annotation.Handler;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesARTDateMedicallyEligibleDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesBaselineWHOStageDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.util.ModuleUtils;
import org.openmrs.module.reporting.data.patient.EvaluatedPatientData;
import org.openmrs.module.reporting.data.patient.definition.PatientDataDefinition;
import org.openmrs.module.reporting.data.patient.evaluator.PatientDataEvaluator;
import org.openmrs.module.reporting.data.visit.EvaluatedVisitData;
import org.openmrs.module.reporting.data.visit.VisitDataUtil;
import org.openmrs.module.reporting.data.visit.definition.VisitDataDefinition;
import org.openmrs.module.reporting.data.visit.evaluator.VisitDataEvaluator;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.querybuilder.SqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.openmrs.module.reporting.query.visit.VisitIdSet;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Evaluates a VisitIdDataDefinition to produce a VisitData
 */
@Handler(supports=SpeedPhasesBaselineWHOStageDataDefinition.class, order=50)
public class SpeedPhasesBaselineWHOStageDataEvaluator implements PatientDataEvaluator {

    @Autowired
    private EvaluationService evaluationService;

    public EvaluatedPatientData evaluate(PatientDataDefinition definition, EvaluationContext context) throws EvaluationException {
        EvaluatedPatientData c = new EvaluatedPatientData(definition, context);

        String qry = "select\n" +
                "d.patient_id, IF(d.who_stage in (1204,1204),\"WHO Stage1\", IF(d.who_stage in (1205,1221),\"WHO Stage2\", IF(d.who_stage in (1206,1222),\"WHO Stage3\", IF(d.who_stage in (1207,1223),\"WHO Stage4\", \"\")))) as who_stage\n" +
                "FROM (\n" +
                "select\n" +
                "d.patient_id, mid(min(concat(f.visit_date, f.who_stage)), 11) as who_stage\n" +
                "from kenyaemr_etl.etl_patient_demographics d \n" +
                "inner join kenyaemr_etl.etl_patient_hiv_followup f on f.patient_id=d.patient_id and f.who_stage is not null and f.voided=0\n" +
                "group by d.patient_id\n" +
                ") d\n";

        SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
        queryBuilder.append(qry);
        Map<Integer, Object> data = evaluationService.evaluateToMap(queryBuilder, Integer.class, Object.class, context);
        c.setData(data);
        return c;
    }
}
