package org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.evaluator;

import org.openmrs.annotation.Handler;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.ARTRegimenDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesVisitPartnerHIVDisclosureDataDefinition;
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
@Handler(supports=SpeedPhasesVisitPartnerHIVDisclosureDataDefinition.class, order=50)
public class SpeedPhasesVisitPartnerHIVDisclosureDataEvaluator implements VisitDataEvaluator {

    @Autowired
    private EvaluationService evaluationService;

    public EvaluatedVisitData evaluate(VisitDataDefinition definition, EvaluationContext context) throws EvaluationException {
        EvaluatedVisitData c = new EvaluatedVisitData(definition, context);

        String qry = "select v.visit_id, (case fup.pwp_disclosure  when 1065 then \"Yes\" when 1066 then \"No\" when 1067 then \"Unknown\" else \"\" end)\n" +
                "from visit v  \n" +
                "inner join kenyaemr_etl.etl_patient_hiv_followup fup on fup.visit_id=v.visit_id \n" +
                "where v.voided=0 ";

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
