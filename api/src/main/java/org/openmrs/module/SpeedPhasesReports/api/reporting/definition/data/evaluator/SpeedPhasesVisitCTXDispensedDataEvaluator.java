package org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.evaluator;

import org.openmrs.annotation.Handler;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.ARTRegimenDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesVisitCTXDispensedDataDefinition;
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
@Handler(supports=SpeedPhasesVisitCTXDispensedDataDefinition.class, order=50)
public class SpeedPhasesVisitCTXDispensedDataEvaluator implements VisitDataEvaluator {

    @Autowired
    private EvaluationService evaluationService;

    public EvaluatedVisitData evaluate(VisitDataDefinition definition, EvaluationContext context) throws EvaluationException {
        EvaluatedVisitData c = new EvaluatedVisitData(definition, context);
        VisitIdSet visitIds = new VisitIdSet(VisitDataUtil.getVisitIdsForContext(context, false));
        if (visitIds.getSize() == 0) {
            return c;
        }
        String qry = "select v.visit_id, (case fup.ctx_dispensed  when 1065 then \"Yes\" when 1066 then \"No\" when 1175 then \"N/A\" else \"\" end)\n" +
                "from visit v  \n" +
                "inner join kenyaemr_etl.etl_patient_hiv_followup fup on fup.visit_id=v.visit_id \n" +
                "where v.voided=0 and v.visit_id in(:visitIds) ";

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
