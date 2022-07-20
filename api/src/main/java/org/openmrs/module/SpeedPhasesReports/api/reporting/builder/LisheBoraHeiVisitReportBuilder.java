/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.module.SpeedPhasesReports.api.reporting.builder;

import org.openmrs.PatientIdentifierType;
import org.openmrs.module.SpeedPhasesReports.api.reporting.calculation.SpeedPhasesBaselineVLAtEnrollmentCalculation;
import org.openmrs.module.SpeedPhasesReports.api.reporting.calculation.SpeedPhasesBaselineVLDateAtEnrollmentCalculation;
import org.openmrs.module.SpeedPhasesReports.api.reporting.converter.HEIOutcomeConverter;
import org.openmrs.module.SpeedPhasesReports.api.reporting.converter.HeiMedicationGivenConverter;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.*;
import org.openmrs.module.SpeedPhasesReports.api.reporting.query.definition.LisheBoraHeiVisitCohortDefinition;
import org.openmrs.module.SpeedPhasesReports.api.util.ModuleUtils;
import org.openmrs.module.kenyacore.report.ReportDescriptor;
import org.openmrs.module.kenyacore.report.ReportUtils;
import org.openmrs.module.kenyacore.report.builder.AbstractReportBuilder;
import org.openmrs.module.kenyacore.report.builder.Builds;
import org.openmrs.module.kenyacore.report.data.patient.definition.CalculationDataDefinition;
import org.openmrs.module.kenyaemr.metadata.HivMetadata;
import org.openmrs.module.kenyaemr.reporting.data.converter.CalculationResultConverter;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.hei.*;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.reporting.data.DataDefinition;
import org.openmrs.module.reporting.data.converter.BirthdateConverter;
import org.openmrs.module.reporting.data.converter.DataConverter;
import org.openmrs.module.reporting.data.converter.DateConverter;
import org.openmrs.module.reporting.data.converter.ObjectFormatter;
import org.openmrs.module.reporting.data.patient.definition.ConvertedPatientDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.PatientIdDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.PatientIdentifierDataDefinition;
import org.openmrs.module.reporting.data.person.definition.BirthdateDataDefinition;
import org.openmrs.module.reporting.data.person.definition.ConvertedPersonDataDefinition;
import org.openmrs.module.reporting.data.person.definition.GenderDataDefinition;
import org.openmrs.module.reporting.data.person.definition.PreferredNameDataDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.VisitDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
@Builds({"speedPhasesReports.common.report.lisheBoraHeiVisitReport"})
public class LisheBoraHeiVisitReportBuilder extends AbstractReportBuilder {
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    @Override
    protected List<Parameter> getParameters(ReportDescriptor descriptor) {
        return Arrays.asList(
                new Parameter("startDate", "Start Date", Date.class),
                new Parameter("endDate", "End Date", Date.class),
                new Parameter("dateBasedReporting", "", String.class)
        );
    }

    @Override
    protected List<Mapped<DataSetDefinition>> buildDataSets(ReportDescriptor reportDescriptor, ReportDefinition reportDefinition) {
        return Arrays.asList(
                ReportUtils.map(datasetColumns(), "startDate=${startDate},endDate=${endDate}")
        );
    }

    protected DataSetDefinition datasetColumns() {
        VisitDataSetDefinition dsd = new VisitDataSetDefinition();
        dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
        dsd.setName("VisitInformation");
        dsd.setDescription("Visit information");

        DataConverter nameFormatter = new ObjectFormatter("{givenName} {middleName} {familyName}");
        DataDefinition nameDef = new ConvertedPersonDataDefinition("name", new PreferredNameDataDefinition(), nameFormatter);


        PatientIdentifierType upn = MetadataUtils.existing(PatientIdentifierType.class, HivMetadata._PatientIdentifierType.UNIQUE_PATIENT_NUMBER);
        PatientIdentifierType heiId = MetadataUtils.existing(PatientIdentifierType.class, ModuleUtils.HEI_UNIQUE_NUMBER);

        DataConverter identifierFormatter = new ObjectFormatter("{identifier}");
        DataDefinition identifierDef = new ConvertedPatientDataDefinition("identifier", new PatientIdentifierDataDefinition(upn.getName(), upn), identifierFormatter);
        DataDefinition heiIdDef = new ConvertedPatientDataDefinition("identifier", new PatientIdentifierDataDefinition(heiId.getName(), heiId), identifierFormatter);


        dsd.addColumn("id", new PatientIdDataDefinition(), "");
        dsd.addColumn("Name", nameDef, "");
        dsd.addColumn("DOB", new BirthdateDataDefinition(), "", new BirthdateConverter(DATE_FORMAT));
        dsd.addColumn("Sex", new GenderDataDefinition(), "");
        dsd.addColumn("HEI Unique Number", heiIdDef, null);
        dsd.addColumn("CCC Number", identifierDef, null);

        dsd.addColumn("HEI Enrollment Date", new HEIEnrollmentDateDataDefinition(), null, new DateConverter(DATE_FORMAT));
        dsd.addColumn("Mothers Phone", new SpeedPhasesMothersPhoneDataDefinition(),"");

        dsd.addColumn("Mother CCC Number", new HEIMotherCCCNumberDataDefinition(), null);
        dsd.addColumn("Birth Weight", new SpeedPhasesBirthWeightDataDefinition(), null);
        dsd.addColumn("Mother received Drugs for PMTCT", new HEIMotherOnPMTCTDrugsDataDefinition(), null);
        dsd.addColumn("PMTCT Drug combination", new HEIMotherOnARVDataDefinition(), null);
        dsd.addColumn("Mother on ART at enrolment of infant", new HeiMotherOnHaartAtEnrolmentDataDefinition(), null);
        dsd.addColumn("Mother ART Regimen", new HEIMotherARVRegimenDataDefinition(), null);
        dsd.addColumn("Infant prophylaxis", new HEIInfantProphylaxisDataDefinition(), null); //To be reviewed to capture more options as in the EMR

        dsd.addColumn("Visit Date", new SpeedPhasesVisitDateDataDefinition(),"", new DateConverter(DATE_FORMAT));

        dsd.addColumn("Weight", new SpeedPhasesVisitWeightDataDefinition(), null);
        dsd.addColumn("Height", new SpeedPhasesVisitHeightDataDefinition(), null);
        dsd.addColumn("Infant feeding", new SpeedPhasesHeiVisitInfantFeedingDataDefinition(), null);

        dsd.addColumn("AZT Given", new SpeedPhasesHeiVisitMedicationDataDefinition("Medication given", "azt_given"),"", new HeiMedicationGivenConverter());
        dsd.addColumn("NVP Given", new SpeedPhasesHeiVisitMedicationDataDefinition("Medication given", "nvp_given"),"", new HeiMedicationGivenConverter());
        dsd.addColumn("CTX Given", new SpeedPhasesHeiVisitMedicationDataDefinition("Medication given", "ctx_given"),"", new HeiMedicationGivenConverter());
        dsd.addColumn("HEI Outcome", new HEIOutcomeDataDefinition(),"", new HEIOutcomeConverter());
        dsd.addColumn("Baseline VL", new CalculationDataDefinition("Baseline VL", new SpeedPhasesBaselineVLAtEnrollmentCalculation()), "", new CalculationResultConverter());
        dsd.addColumn("Baseline VL Date", new CalculationDataDefinition("Baseline VL Date", new SpeedPhasesBaselineVLDateAtEnrollmentCalculation()), "", new CalculationResultConverter());
        dsd.addColumn("Facility Name", new DefaultFacilityDataDefinition("Facility Name", "facilityName"),"");
        dsd.addColumn("evaluationDate", new CalculationDataDefinition("Query Date", new SpeedPhasesQueryDateCalculation()),"", new CalculationResultConverter());



        LisheBoraHeiVisitCohortDefinition cd = new LisheBoraHeiVisitCohortDefinition();
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        dsd.addRowFilter(cd, "startDate=${startDate},endDate=${endDate}");
        return dsd;

    }
}