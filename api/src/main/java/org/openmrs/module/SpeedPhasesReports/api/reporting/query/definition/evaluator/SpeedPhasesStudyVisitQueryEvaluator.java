package org.openmrs.module.SpeedPhasesReports.api.reporting.query.definition.evaluator;

import org.openmrs.annotation.Handler;
import org.openmrs.module.SpeedPhasesReports.api.reporting.query.definition.SpeedPhasesStudyVisitQuery;
import org.openmrs.module.SpeedPhasesReports.api.util.ModuleFileProcessorUtil;
import org.openmrs.module.reporting.common.ObjectUtil;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.querybuilder.SqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.openmrs.module.reporting.query.visit.VisitQueryResult;
import org.openmrs.module.reporting.query.visit.definition.VisitQuery;
import org.openmrs.module.reporting.query.visit.evaluator.VisitQueryEvaluator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;

/**
 * The logic that evaluates an {@link SpeedPhasesStudyVisitQuery} and produces a {@link VisitQueryResult}
 */
@Handler(supports=SpeedPhasesStudyVisitQuery.class)
public class SpeedPhasesStudyVisitQueryEvaluator implements VisitQueryEvaluator {

    @Autowired
    EvaluationService evaluationService;

    public VisitQueryResult evaluate(VisitQuery definition, EvaluationContext context) throws EvaluationException {
        context = ObjectUtil.nvl(context, new EvaluationContext());
        VisitQueryResult queryResult = new VisitQueryResult(definition, context);

        String qry = ModuleFileProcessorUtil.getInitialCohortQuery();
        SqlQueryBuilder builder = new SqlQueryBuilder();
        builder.append(qry);
        builder.addParameter("effectiveDate", ModuleFileProcessorUtil.getDefaultDate());
        builder.addParameter("endDate", ModuleFileProcessorUtil.getDefaultEndDate());
        builder.addParameter("patientIds", ModuleFileProcessorUtil.defaultCohort());

        List<Integer> results = evaluationService.evaluateToList(builder, Integer.class, context);
        queryResult.getMemberIds().addAll(results);
        System.out.println("Effective date: ==============================" + ModuleFileProcessorUtil.getDefaultDate());
        System.out.println("End Date: ==============================" + ModuleFileProcessorUtil.getDefaultEndDate());
        System.out.println("Completed processing visit query: Total visits: " + results.size());
        return queryResult;
    }

}
