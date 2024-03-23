package org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.evaluator;

import org.openmrs.annotation.Handler;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.EncounterBasedViralLoadOrderReasonDefinition;
import org.openmrs.module.reporting.data.encounter.EvaluatedEncounterData;
import org.openmrs.module.reporting.data.encounter.definition.EncounterDataDefinition;
import org.openmrs.module.reporting.data.encounter.evaluator.EncounterDataEvaluator;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.querybuilder.SqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Evaluates a VisitIdDataDefinition to produce a VisitData
 */
@Handler(supports= EncounterBasedViralLoadOrderReasonDefinition.class, order=50)
public class EncounterBasedViralLoadOrderReasonEvaluator implements EncounterDataEvaluator {

    @Autowired
    private EvaluationService evaluationService;

    public EvaluatedEncounterData evaluate(EncounterDataDefinition definition, EvaluationContext context) throws EvaluationException {
        EvaluatedEncounterData c = new EvaluatedEncounterData(definition, context);

        String qry = "select encounter_id, (case order_reason when 843 then 'Confirmation of treatment failure (repeat VL)' when 1259 then 'Single Drug Substitution' when 1434 then 'Pregnancy'\n" +
                "                                        when 159882 then 'Breastfeeding' when 160566 then 'Immunologic failure' when 160569 then 'Virologic failure'\n" +
                "                                        when 161236 then 'Routine' when 162080 then 'Baseline VL (for infants diagnosed through EID)' when 162081 then 'Repeat' when 163523 then 'Clinical failure'\n" +
                "                                        when 160032 then 'Confirmation of persistent low level Viremia (PLLV)' when 1040 then 'Initial PCR (6week or first contact)' when 1326 then '2nd PCR (6 months)' when 164860 then '3rd PCR (12months)'\n" +
                "                                        when 162082 then 'Confirmatory PCR and Baseline VL' when 164460 then 'Ab test 6 weeks after cessation of breastfeeding'\n" +
                "                                        when 164860 then 'Ab test at 18 months (1.5 years)'\n" +
                "                                        else '' end) as order_reason\n" +
                "    from kenyaemr_etl.etl_laboratory_extract where lab_test in (1305, 856) and order_reason is not null; ";

        SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
        queryBuilder.append(qry);
        Map<Integer, Object> data = evaluationService.evaluateToMap(queryBuilder, Integer.class, Object.class, context);
        c.setData(data);
        return c;
    }
}
