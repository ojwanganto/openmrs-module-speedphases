package org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.evaluator;

import org.openmrs.annotation.Handler;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesHeiVisitMedicationDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesPopulationTypeDataDefinition;
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
 * Evaluates a SpeedPhasesPopulationTypeDataDefinition to produce a VisitData
 */
@Handler(supports=SpeedPhasesHeiVisitMedicationDataDefinition.class, order=50)
public class SpeedPhasesHeiVisitMedicationDataEvaluator implements VisitDataEvaluator {

    @Autowired
    private EvaluationService evaluationService;

    public EvaluatedVisitData evaluate(VisitDataDefinition definition, EvaluationContext context) throws EvaluationException {
        EvaluatedVisitData c = new EvaluatedVisitData(definition, context);
        VisitIdSet visitIds = new VisitIdSet(VisitDataUtil.getVisitIdsForContext(context, false));
        if (visitIds.getSize() == 0) {
            return c;
        }
        SpeedPhasesHeiVisitMedicationDataDefinition dataDefinition = (SpeedPhasesHeiVisitMedicationDataDefinition) definition;
        String medicationName = dataDefinition.getMedicationName();
        String qry = "select v.visit_id, fup.:medicationName \n" +
                "from kenyaemr_etl.etl_hei_follow_up_visit fup  \n" +
                "inner join visit v on v.visit_id=fup.visit_id \n" +
                "where v.voided=0 and v.visit_id in(:visitIds) ";

        qry = qry.replace(":medicationName", medicationName);
        SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
        queryBuilder.append(qry);
        queryBuilder.addParameter("visitIds", visitIds);
        Map<Integer, Object> data = evaluationService.evaluateToMap(queryBuilder, Integer.class, Object.class, context);
        c.setData(data);
        return c;
    }
}
