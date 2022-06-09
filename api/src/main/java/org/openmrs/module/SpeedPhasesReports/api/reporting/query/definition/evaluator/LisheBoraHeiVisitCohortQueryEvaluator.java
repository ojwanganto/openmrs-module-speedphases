package org.openmrs.module.SpeedPhasesReports.api.reporting.query.definition.evaluator;

import org.openmrs.annotation.Handler;
import org.openmrs.module.SpeedPhasesReports.api.reporting.query.definition.LisheBoraHeiVisitCohortDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.query.definition.SpeedPhasesStudyVisitQuery;
import org.openmrs.module.SpeedPhasesReports.api.util.ModuleUtils;
import org.openmrs.module.reporting.common.ObjectUtil;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.querybuilder.SqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.openmrs.module.reporting.query.visit.VisitQueryResult;
import org.openmrs.module.reporting.query.visit.definition.VisitQuery;
import org.openmrs.module.reporting.query.visit.evaluator.VisitQueryEvaluator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * The logic that evaluates an {@link SpeedPhasesStudyVisitQuery} and produces a {@link VisitQueryResult}
 */
@Handler(supports= LisheBoraHeiVisitCohortDefinition.class)
public class LisheBoraHeiVisitCohortQueryEvaluator implements VisitQueryEvaluator {

    @Autowired
    EvaluationService evaluationService;

    public VisitQueryResult evaluate(VisitQuery definition, EvaluationContext context) throws EvaluationException {
        context = ObjectUtil.nvl(context, new EvaluationContext());
        VisitQueryResult queryResult = new VisitQueryResult(definition, context);

        List<Integer> cohort = ModuleUtils.getLisheBoraCohort();
        String qry = "select f.visit_id \n" +
                "from kenyaemr_etl.etl_hei_follow_up_visit f\n" +
                "inner join person p on p.person_id = f.patient_id \n" +
                "inner join  kenyaemr_etl.etl_hei_enrollment e on e.patient_id = f.patient_id\n" +
                "where  f.patient_id in (:patientList) and f.visit_id is not null and datediff(f.visit_date, p.birthdate) div 365.25 between 0 and 2";

        SqlQueryBuilder builder = new SqlQueryBuilder();

        Date startDate = (Date)context.getParameterValue("startDate");
        Date endDate = (Date)context.getParameterValue("endDate");

        builder.append(qry);
        builder.addParameter("startDate", startDate);
        builder.addParameter("endDate", endDate);
        builder.addParameter("patientList", cohort);

        List<Integer> results = evaluationService.evaluateToList(builder, Integer.class, context);
        queryResult.getMemberIds().addAll(results);
        return queryResult;
    }

}
