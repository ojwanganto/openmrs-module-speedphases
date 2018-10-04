package org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.evaluator;

import org.openmrs.annotation.Handler;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.VisitOisDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.util.ModuleUtils;
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
@Handler(supports=VisitOisDataDefinition.class, order=50)
public class VisitOisDateDataEvaluator implements VisitDataEvaluator {

    @Autowired
    private EvaluationService evaluationService;

    public EvaluatedVisitData evaluate(VisitDataDefinition definition, EvaluationContext context) throws EvaluationException {
        EvaluatedVisitData c = new EvaluatedVisitData(definition, context);
        VisitIdSet visitIds = new VisitIdSet(VisitDataUtil.getVisitIdsForContext(context, false));
        if (visitIds.getSize() == 0) {
            return c;
        }
        String qry = "select v.visit_id, if(o.value_coded != 1107,DATE_FORMAT(o.obs_datetime, '%d/%m/%Y'), '') as visitOIsDate\n" +
                "from visit v \n" +
                "inner join encounter e on v.visit_id = e.visit_id \n" +
                "inner join (\n" +
                "select encounter_type_id from encounter_type where uuid='a0034eee-1940-4e35-847f-97537a35d05e'\n" +
                ") et on et.encounter_type_id = e.encounter_type \n" +
                "inner join obs o on o.encounter_id = e.encounter_id and o.concept_id = 6042\n" +
                "left outer join concept_name cn on cn.concept_id=o.value_coded  and cn.concept_name_type='FULLY_SPECIFIED'\n" +
                "and cn.locale='en' and v.visit_id in(:visitIds) group by v.visit_id ";


        SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
        queryBuilder.append(qry);
        queryBuilder.addParameter("visitIds", visitIds);
        queryBuilder.addParameter("startDate", ModuleUtils.startDate());
        queryBuilder.addParameter("endDate", ModuleUtils.getDefaultEndDate());
        Map<Integer, Object> data = evaluationService.evaluateToMap(queryBuilder, Integer.class, Object.class, context);
        c.setData(data);
        return c;
    }
}
