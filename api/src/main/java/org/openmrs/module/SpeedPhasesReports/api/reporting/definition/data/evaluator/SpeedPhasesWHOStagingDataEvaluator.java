package org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.evaluator;

import org.openmrs.annotation.Handler;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesWHOStagingDataDefinition;
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
@Handler(supports=SpeedPhasesWHOStagingDataDefinition.class, order=50)
public class SpeedPhasesWHOStagingDataEvaluator implements VisitDataEvaluator {

    @Autowired
    private EvaluationService evaluationService;

    public EvaluatedVisitData evaluate(VisitDataDefinition definition, EvaluationContext context) throws EvaluationException {
        EvaluatedVisitData c = new EvaluatedVisitData(definition, context);

        String qry = "select v.visit_id, cn.name"
                        + " from visit v "
                        + " inner join encounter e on e.visit_id = v.visit_id "
                        + " inner join obs o on o.encounter_id = e.encounter_id and o.voided=0 "
                        + "left outer join concept_name cn on cn.concept_id=o.value_coded  and cn.concept_name_type='FULLY_SPECIFIED'\n" +
                "    and cn.locale='en'\n"
                        + " where o.concept_id in(5356) ";
                        //+ " and v.date_started > :startDate  ";

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
