package org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.evaluator;

import org.openmrs.annotation.Handler;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.ReasonPatientDiscontinuedDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.util.ModuleUtils;
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
@Handler(supports=ReasonPatientDiscontinuedDataDefinition.class, order=50)
public class ReasonPatientDiscontinuedDataEvaluator implements VisitDataEvaluator {

    @Autowired
    private EvaluationService evaluationService;

    public EvaluatedVisitData evaluate(VisitDataDefinition definition, EvaluationContext context) throws EvaluationException {
        EvaluatedVisitData c = new EvaluatedVisitData(definition, context);

        String qry = "select v.visit_id, cn.name\n" +
                "from visit v \n" +
                "inner join encounter e on v.visit_id = e.visit_id \n" +
                "inner join (\n" +
                "select encounter_type_id from encounter_type where uuid=\"2bdada65-4c72-4a48-8730-859890e25cee\"\n" +
                ") et on et.encounter_type_id = e.encounter_type \n" +
                "left outer join obs o on o.encounter_id = e.encounter_id and o.concept_id = 161555\n" +
                "left outer join concept_name cn on cn.concept_id=o.value_coded  and cn.concept_name_type='FULLY_SPECIFIED'\n" +
                "and cn.locale='en' \n" +
                " ";

        //we want to restrict visits to those for patients in question
        qry = qry + " and v.visit_id in (";
        qry = qry + ModuleUtils.getInitialCohortQuery();
        qry = qry + ") ";

        SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
        queryBuilder.append(qry);
        queryBuilder.addParameter("startDate", ModuleUtils.startDate());
        queryBuilder.addParameter("endDate", ModuleUtils.getDefaultEndDate());
        Map<Integer, Object> data = evaluationService.evaluateToMap(queryBuilder, Integer.class, Object.class, context);
        c.setData(data);
        return c;
    }
}
