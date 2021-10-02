package org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.evaluator;

import org.openmrs.annotation.Handler;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesFPMethodDataDefinition;
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
@Handler(supports=SpeedPhasesFPMethodDataDefinition.class, order=50)
public class SpeedPhasesFPMethodDataEvaluator implements VisitDataEvaluator {

    @Autowired
    private EvaluationService evaluationService;

    public EvaluatedVisitData evaluate(VisitDataDefinition definition, EvaluationContext context) throws EvaluationException {
        EvaluatedVisitData c = new EvaluatedVisitData(definition, context);
        VisitIdSet visitIds = new VisitIdSet(VisitDataUtil.getVisitIdsForContext(context, false));
        if (visitIds.getSize() == 0) {
            return c;
        }
        String qry = "select v.visit_id,  (case fup.family_planning_method\n" +
                "        when 160570 then 'Emergency contraceptive pills'\n" +
                "        when 780 then 'Oral Contraceptives Pills'\n" +
                "        when 5279 then 'Injectible'\n" +
                "        when 1359 then 'Implant'\n" +
                "        when 5275 then 'Intrauterine Device'\n" +
                "        when 136163 then 'Lactational Amenorhea Method'\n" +
                "        when 5278 then 'Diaphram/Cervical Cap'\n" +
                "        when 5277 then 'Fertility awareness'\n" +
                "        when 1472 then 'Tubal Ligation'\n" +
                "        when 190 then 'Condoms'\n" +
                "        when 1489 then 'Vasectomy'\n" +
                "        else ''\n" +
                "        end\n" +
                "          ) fp_method\n" +
                "from visit v  \n" +
                "inner join kenyaemr_etl.etl_patient_hiv_followup fup on fup.visit_id=v.visit_id \n" +
                "where v.voided=0 and v.visit_id in(:visitIds) and fup.family_planning_method is not null ";

        SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
        queryBuilder.append(qry);
        queryBuilder.addParameter("visitIds", visitIds);
        Map<Integer, Object> data = evaluationService.evaluateToMap(queryBuilder, Integer.class, Object.class, context);
        c.setData(data);
        return c;
    }
}
