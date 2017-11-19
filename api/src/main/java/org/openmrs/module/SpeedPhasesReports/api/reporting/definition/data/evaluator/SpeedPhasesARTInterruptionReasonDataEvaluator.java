package org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.evaluator;

import org.openmrs.annotation.Handler;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.ARTRegimenDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesARTInterruptionReasonDataDefinition;
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
@Handler(supports=SpeedPhasesARTInterruptionReasonDataDefinition.class, order=50)
public class SpeedPhasesARTInterruptionReasonDataEvaluator implements VisitDataEvaluator {

    @Autowired
    private EvaluationService evaluationService;

    public EvaluatedVisitData evaluate(VisitDataDefinition definition, EvaluationContext context) throws EvaluationException {
        EvaluatedVisitData c = new EvaluatedVisitData(definition, context);
        VisitIdSet visitIds = new VisitIdSet(VisitDataUtil.getVisitIdsForContext(context, false));
        if (visitIds.getSize() == 0) {
            return c;
        }
        String qry = "select v.visit_id,\n" +
                "COALESCE(case reason_discontinued \n" +
                "when 102 then \"Toxicity/side effects\"\n" +
                "when 1434 then \"Pregnancy\"\n" +
                "when 160559 then \"Risk of pregnancy\"\n" +
                "when 160567 then \"New diagnosis of TB\"\n" +
                "when 160561 then \"New drug available\"\n" +
                "when 1754 then \"Drugs out of stock\"\n" +
                "when 843 then \"Clinical treatment failure\"\n" +
                "when 160566 then \"Immunological failure\"\n" +
                "when 160569 then \"Virological failure\"\n" +
                "when 159598 then \"Poor adherence\"\n" +
                "when 5485 then \"Inpatient care or hospitalization\"\n" +
                "when 819 then \"Patient lacks finance\"\n" +
                "when 127750 then \"Refusal/Patient decision\"\n" +
                "when 160016 then \"Planned treatment interruption\"\n" +
                "when 1253 then \"Completed total PMTCT\"\n" +
                "when 1270 then \"Tuberculosis treatment started\"\n" +
                "else \"\"\n" +
                "end, reason_discontinued_other) reason_discontinued\n" +
                "from visit v\n" +
                "inner join kenyaemr_etl.etl_drug_event d on date(d.date_discontinued) = date(v.date_started) and d.patient_id=v.patient_id\n" +
                "where discontinued is not null and v.visit_id in(:visitIds) ";

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
