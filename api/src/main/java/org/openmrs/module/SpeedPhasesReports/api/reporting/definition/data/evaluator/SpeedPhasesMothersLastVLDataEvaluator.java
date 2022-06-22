package org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.evaluator;

import org.openmrs.annotation.Handler;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesMothersLastVLDataDefinition;
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
 * Evaluates a SpeedPhasesMothersLastVLDataDefinition to produce a VisitData
 */
@Handler(supports= SpeedPhasesMothersLastVLDataDefinition.class, order=50)
public class SpeedPhasesMothersLastVLDataEvaluator implements VisitDataEvaluator {

    @Autowired
    private EvaluationService evaluationService;

    public EvaluatedVisitData evaluate(VisitDataDefinition definition, EvaluationContext context) throws EvaluationException {
        EvaluatedVisitData c = new EvaluatedVisitData(definition, context);
        VisitIdSet visitIds = new VisitIdSet(VisitDataUtil.getVisitIdsForContext(context, false));
        if (visitIds.getSize() == 0) {
            return c;
        }

        String qry = "select v.visit_id, vl.lastVl from kenyaemr_etl.etl_hei_follow_up_visit v\n" +
                "     inner join visit vi on vi.patient_id = v.patient_id and vi.visit_id in(:visitIds) and vi.voided = 0\n" +
                "     inner join relationship r on v.patient_id = r.person_b\n" +
                "     inner join (select d.patient_id, d.gender from kenyaemr_etl.etl_patient_demographics d where d.gender = 'F')m on m.patient_id = r.person_a\n" +
                "     inner join kenyaemr_etl.etl_laboratory_extract \n" +
                " inner join relationship_type t on r.relationship = t.relationship_type_id and t.uuid='8d91a210-c2cc-11de-8d13-0010c6dffd0f'\n" +
                " inner join\n" +
                "(select\n" +
                "    patient_id, mid(max(concat(visit_date,test_result)),11) lastVl\n" +
                "    from\n" +
                "(select x.patient_id as patient_id,x.visit_date as visit_date, if(x.lab_test = 1305 and x.test_result = 1302, 'LDL', x.test_result) as test_result\n" +
                "from kenyaemr_etl.etl_laboratory_extract x where x.lab_test in (1305,856)\n" +
                "group by x.patient_id,x.visit_date) p_vl\n" +
                "group by patient_id) vl on vl.patient_id = r.person_a";

        SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
        queryBuilder.append(qry);
        queryBuilder.addParameter("visitIds", visitIds);
        Map<Integer, Object> data = evaluationService.evaluateToMap(queryBuilder, Integer.class, Object.class, context);
        c.setData(data);
        return c;
    }
}
