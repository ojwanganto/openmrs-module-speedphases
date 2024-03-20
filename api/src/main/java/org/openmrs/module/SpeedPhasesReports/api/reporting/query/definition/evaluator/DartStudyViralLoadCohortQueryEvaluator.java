package org.openmrs.module.SpeedPhasesReports.api.reporting.query.definition.evaluator;

import org.openmrs.annotation.Handler;
import org.openmrs.module.SpeedPhasesReports.api.reporting.query.definition.DartStudyViralLoadCohortDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.query.definition.SpeedPhasesStudyVisitQuery;
import org.openmrs.module.reporting.common.ObjectUtil;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.querybuilder.SqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.openmrs.module.reporting.query.encounter.EncounterQueryResult;
import org.openmrs.module.reporting.query.encounter.definition.EncounterQuery;
import org.openmrs.module.reporting.query.encounter.evaluator.EncounterQueryEvaluator;
import org.openmrs.module.reporting.query.visit.VisitQueryResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * The logic that evaluates an {@link SpeedPhasesStudyVisitQuery} and produces a {@link VisitQueryResult}
 */
@Handler(supports= DartStudyViralLoadCohortDefinition.class)
public class DartStudyViralLoadCohortQueryEvaluator implements EncounterQueryEvaluator {

    @Autowired
    EvaluationService evaluationService;

    public EncounterQueryResult evaluate(EncounterQuery definition, EvaluationContext context) throws EvaluationException {
        context = ObjectUtil.nvl(context, new EvaluationContext());
        EncounterQueryResult queryResult = new EncounterQueryResult(definition, context);

        String qry = "select t.encounter_id\n" +
                "from kenyaemr_etl.etl_laboratory_extract t\n" +
                "         inner join (\n" +
                "    select v.visit_id, v.patient_id\n" +
                "    from patient pt\n" +
                "             inner join visit v on pt.patient_id = v.patient_id\n" +
                "             inner join person p on v.patient_id = p.person_id\n" +
                "             inner join  kenyaemr_etl.etl_hiv_enrollment e on e.patient_id = p.person_id\n" +
                "    where  date(v.date_started) between date(:startDate) and date(:endDate)\n" +
                "      and datediff(v.date_started, p.birthdate) div 365.25 between 0 and 19\n" +
                "    group by v.visit_id\n" +
                "    having v.visit_id is not null\n" +
                ") c on c.patient_id = t.patient_id\n" +
                "where t.lab_test in (1305, 856);";
        SqlQueryBuilder builder = new SqlQueryBuilder();

        Date startDate = (Date)context.getParameterValue("startDate");
        Date endDate = (Date)context.getParameterValue("endDate");

        builder.append(qry);
        builder.addParameter("startDate", startDate);
        builder.addParameter("endDate", endDate);

        List<Integer> results = evaluationService.evaluateToList(builder, Integer.class, context);
        queryResult.getMemberIds().addAll(results);
        return queryResult;
    }

}
