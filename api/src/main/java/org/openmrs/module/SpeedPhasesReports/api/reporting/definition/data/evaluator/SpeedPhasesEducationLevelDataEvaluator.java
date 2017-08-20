package org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.evaluator;

import org.openmrs.annotation.Handler;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesEducationLevelDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.util.ModuleFileProcessorUtil;
import org.openmrs.module.reporting.data.patient.EvaluatedPatientData;
import org.openmrs.module.reporting.data.patient.definition.PatientDataDefinition;
import org.openmrs.module.reporting.data.patient.evaluator.PatientDataEvaluator;
import org.openmrs.module.reporting.data.visit.EvaluatedVisitData;
import org.openmrs.module.reporting.data.visit.definition.VisitDataDefinition;
import org.openmrs.module.reporting.data.visit.evaluator.VisitDataEvaluator;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.querybuilder.SqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Evaluates a VisitIdDataDefinition to produce a VisitData
 */
@Handler(supports=SpeedPhasesEducationLevelDataDefinition.class, order=50)
public class SpeedPhasesEducationLevelDataEvaluator implements PatientDataEvaluator {

    @Autowired
    private EvaluationService evaluationService;

    public EvaluatedPatientData evaluate(PatientDataDefinition definition, EvaluationContext context) throws EvaluationException {
        EvaluatedPatientData c = new EvaluatedPatientData(definition, context);

        String qry = "select e.patient_id ,\n" +
                "mid(max(concat(date(o.obs_datetime), cn.name)), 11) as education_level\n" +
                "from patient p  inner join encounter e on e.patient_id = p.patient_id  \n" +
                "inner join obs o on o.encounter_id = e.encounter_id and o.voided=0  \n" +
                "left outer join concept_name cn on cn.concept_id=o.value_coded  and cn.concept_name_type='FULLY_SPECIFIED'\n" +
                "    and cn.locale='en'\n" +
                "where o.concept_id =1712 \n" +
                "group by e.patient_id\n";


        SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
        queryBuilder.append(qry);
        Map<Integer, Object> data = evaluationService.evaluateToMap(queryBuilder, Integer.class, Object.class, context);
        c.setData(data);
        System.out.println("Completed processing Education Level");
        return c;
    }
}
