package org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.evaluator;

import org.openmrs.annotation.Handler;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesFPUsageDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.util.ModuleFileProcessorUtil;
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
@Handler(supports=SpeedPhasesFPUsageDataDefinition.class, order=50)
public class SpeedPhasesFPUsageDataEvaluator implements VisitDataEvaluator {

    @Autowired
    private EvaluationService evaluationService;

    public EvaluatedVisitData evaluate(VisitDataDefinition definition, EvaluationContext context) throws EvaluationException {
        EvaluatedVisitData c = new EvaluatedVisitData(definition, context);

        String qry = "select v.visit_id,  \n" +
                "(case o.value_coded\n" +
                "when 965 then 'Currently using FP'\n" +
                "when 160652 then 'Not using FP'\n" +
                "when 1360 then 'Wants FP' \n" +
                "else ''\n" +
                "end\n" +
                "\n" +
                ") as fp_status\n" +
                "from visit v \n" +
                "inner join encounter e on e.visit_id = v.visit_id \n" +
                "inner join obs o on o.encounter_id = e.encounter_id and o.voided=0 \n" +
                "where o.concept_id =160653 ";

        //we want to restrict visits to those for patients in question
        qry = qry + " and v.visit_id in (";
        qry = qry + ModuleFileProcessorUtil.getInitialCohortQuery();
        qry = qry + ") ";

        SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
        queryBuilder.append(qry);
        queryBuilder.addParameter("effectiveDate", ModuleFileProcessorUtil.getDefaultDate());
        queryBuilder.addParameter("endDate", ModuleFileProcessorUtil.getDefaultEndDate());
        queryBuilder.addParameter("patientIds", ModuleFileProcessorUtil.defaultCohort());
        Map<Integer, Object> data = evaluationService.evaluateToMap(queryBuilder, Integer.class, Object.class, context);
        c.setData(data);
        System.out.println("Completed processing Date ART started");
        return c;
    }
}
