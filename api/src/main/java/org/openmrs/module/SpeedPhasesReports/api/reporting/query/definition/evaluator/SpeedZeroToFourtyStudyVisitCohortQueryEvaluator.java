package org.openmrs.module.SpeedPhasesReports.api.reporting.query.definition.evaluator;

import org.openmrs.annotation.Handler;
import org.openmrs.module.SpeedPhasesReports.api.reporting.query.definition.SpeedPhasesStudyVisitQuery;
import org.openmrs.module.SpeedPhasesReports.api.reporting.query.definition.SpeedTenToTwentyFourStudyVisitCohortDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.query.definition.SpeedZeroToFourtyStudyVisitCohortDefinition;
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

import java.util.List;

/**
 * The logic that evaluates an {@link SpeedPhasesStudyVisitQuery} and produces a {@link VisitQueryResult}
 */
@Handler(supports=SpeedZeroToFourtyStudyVisitCohortDefinition.class)
public class SpeedZeroToFourtyStudyVisitCohortQueryEvaluator implements VisitQueryEvaluator {

    @Autowired
    EvaluationService evaluationService;

    public VisitQueryResult evaluate(VisitQuery definition, EvaluationContext context) throws EvaluationException {
        context = ObjectUtil.nvl(context, new EvaluationContext());
        VisitQueryResult queryResult = new VisitQueryResult(definition, context);

        String qry = "select v.visit_id \n" +
                "from patient pt \n" +
                "inner join visit v on pt.patient_id = v.patient_id \n" +
                "inner join person p on v.patient_id = p.person_id\n" +
                "inner join  kenyaemr_etl.etl_hiv_enrollment e on e.patient_id = p.person_id\n" +
                "where  date(v.date_started) between date(:startDate) and date(:endDate) \n" +
                "and datediff(v.date_started, p.birthdate) div 365.25 <= 40\n" +
                "group by v.visit_id \n" +
                "having v.visit_id is not null; ";
        SqlQueryBuilder builder = new SqlQueryBuilder();
        builder.append(qry);
        builder.addParameter("startDate", ModuleUtils.startDate());
        builder.addParameter("endDate", ModuleUtils.getDefaultEndDate());

        List<Integer> results = evaluationService.evaluateToList(builder, Integer.class, context);
        queryResult.getMemberIds().addAll(results);
        return queryResult;
    }

}
