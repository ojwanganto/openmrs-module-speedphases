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
import org.openmrs.module.SpeedPhasesReports.api.reporting.converter.GenericDateConverter;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.ARTRegimenDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.ARTRegimenLineDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.MaritalStatusDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.PWPDisclosureDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.PatientDiscontinuedDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.PregnancyStatusDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.ReasonPatientDiscontinuedDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesARTInterruptionReasonDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesBaselineVLDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesBloodPressureDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesDateConfirmedHIVPositiveDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesDateTransferredOutDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesDeathDateDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesEducationLevelDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesEnrollmentDateCalculation;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesEntryPointDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesFPUsageDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesHasTreatmentSupporterDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesNextVisitDateDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesQueryDateCalculation;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesSTIScreeningDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesTransferInDateDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesTreatmentSupporterRelationshipDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesViralLoadDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesVisitARTAdherenceDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesVisitARTInterruptionDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesVisitCD4DataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesVisitCTXAdherenceDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesVisitCTXDispensedDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesVisitCondomUseDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesVisitDateDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesVisitEDDDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesVisitHeightDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesVisitPartnerHIVDisclosureDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesVisitTBStatusDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesVisitTypeDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesVisitWeightDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesWHOStagingDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.VisitOisDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.query.definition.SpeedTenToTwentyFourStudyVisitCohortDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.query.definition.SpeedZeroToFourtyStudyVisitCohortDefinition;
import org.openmrs.module.kenyacore.report.ReportDescriptor;
import org.openmrs.module.kenyacore.report.ReportUtils;
import org.openmrs.module.kenyacore.report.builder.AbstractReportBuilder;
import org.openmrs.module.kenyacore.report.builder.Builds;
import org.openmrs.module.kenyacore.report.data.patient.definition.CalculationDataDefinition;
import org.openmrs.module.kenyaemr.calculation.library.hiv.art.InitialArtStartDateCalculation;
import org.openmrs.module.kenyaemr.metadata.HivMetadata;
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
import org.openmrs.module.reporting.data.visit.definition.VisitIdDataDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.VisitDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Builds({"speedPhasesReports.common.report.speedZeroToFourty"})
public class SpeedZeroToFourtyReportBuilder extends AbstractReportBuilder {
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    @Override
    protected List<Parameter> getParameters(ReportDescriptor descriptor) {
        return Arrays.asList();
    }

    @Override
    protected List<Mapped<DataSetDefinition>> buildDataSets(ReportDescriptor reportDescriptor, ReportDefinition reportDefinition) {
        return Arrays.asList(
                ReportUtils.map(datasetColumns(), "")
        );
    }

    protected DataSetDefinition datasetColumns() {
        VisitDataSetDefinition dsd = new VisitDataSetDefinition();
        dsd.setName("VisitInformation");
        dsd.setDescription("Visit information");

        DataConverter nameFormatter = new ObjectFormatter("{familyName}, {givenName}");
        DataDefinition nameDef = new ConvertedPersonDataDefinition("name", new PreferredNameDataDefinition(), nameFormatter);


        PatientIdentifierType upn = MetadataUtils.existing(PatientIdentifierType.class, HivMetadata._PatientIdentifierType.UNIQUE_PATIENT_NUMBER);
        DataConverter identifierFormatter = new ObjectFormatter("{identifier}");
        DataDefinition identifierDef = new ConvertedPatientDataDefinition("identifier", new PatientIdentifierDataDefinition(upn.getName(), upn), identifierFormatter);

        dsd.addColumn("VISIT ID", new VisitIdDataDefinition(), null);
        dsd.addColumn("id", new PatientIdDataDefinition(), "");
        dsd.addColumn("Name", nameDef, "");
        dsd.addColumn("DOB", new BirthdateDataDefinition(), "", new BirthdateConverter("yyyy-MM-dd"));
        dsd.addColumn("Sex", new GenderDataDefinition(), "");
        dsd.addColumn("Unique Patient Number", identifierDef, null);
        dsd.addColumn("Marital Status", new MaritalStatusDataDefinition(), null);
        dsd.addColumn("Education Level", new SpeedPhasesEducationLevelDataDefinition(), null);
        dsd.addColumn("Date Confirmed Positive", new SpeedPhasesDateConfirmedHIVPositiveDataDefinition(), "", new DateConverter("yyyy-MM-dd"));
        dsd.addColumn("Date Enrolled in Care", new CalculationDataDefinition("DOE", new SpeedPhasesEnrollmentDateCalculation()), "", new GenericDateConverter());
        dsd.addColumn("Transfer-in Date", new SpeedPhasesTransferInDateDataDefinition(), null, new DateConverter(DATE_FORMAT));
        dsd.addColumn("Visit Date", new SpeedPhasesVisitDateDataDefinition(),"", new DateConverter(DATE_FORMAT));
        // new columns
        dsd.addColumn("Pregnancy Status", new PregnancyStatusDataDefinition(), null);
        dsd.addColumn("FP", new SpeedPhasesFPUsageDataDefinition(), null);
        dsd.addColumn("WHO Stage", new SpeedPhasesWHOStagingDataDefinition(), null);
        dsd.addColumn("STI Screening", new SpeedPhasesSTIScreeningDataDefinition(), null);
        dsd.addColumn("PWP Disclosure", new PWPDisclosureDataDefinition(), null);
        dsd.addColumn("CD4", new SpeedPhasesVisitCD4DataDefinition(), null);
        dsd.addColumn("Viral Load", new SpeedPhasesViralLoadDataDefinition(), null);
        dsd.addColumn("Next Visit Date", new SpeedPhasesNextVisitDateDataDefinition(), "", new DateConverter(DATE_FORMAT));
        dsd.addColumn("Art Start Date", new CalculationDataDefinition("Art Start Date", new InitialArtStartDateCalculation()), "", new GenericDateConverter());
        dsd.addColumn("Regimen", new ARTRegimenDataDefinition(), null);
        dsd.addColumn("Regimen Line", new ARTRegimenLineDataDefinition(), null);
        dsd.addColumn("Patient Discontinued", new PatientDiscontinuedDataDefinition(), null);
        dsd.addColumn("Discontinuation Reason", new ReasonPatientDiscontinuedDataDefinition(), null);
        dsd.addColumn("Visit Ois", new VisitOisDataDefinition(), null);
        dsd.addColumn("TB Status", new SpeedPhasesVisitTBStatusDataDefinition(), null);
        dsd.addColumn("Partner HIV Disclosure", new SpeedPhasesVisitPartnerHIVDisclosureDataDefinition(), null);
        dsd.addColumn("EDD", new SpeedPhasesVisitEDDDataDefinition(), null);
        dsd.addColumn("Condom provided", new SpeedPhasesVisitCondomUseDataDefinition(), null);
        dsd.addColumn("Visit Type", new SpeedPhasesVisitTypeDataDefinition(), null);
        dsd.addColumn("CTX adherence", new SpeedPhasesVisitCTXAdherenceDataDefinition(), null);
        dsd.addColumn("CTX dispensed", new SpeedPhasesVisitCTXDispensedDataDefinition(), null);
        dsd.addColumn("ART adherence", new SpeedPhasesVisitARTAdherenceDataDefinition(), null);
        dsd.addColumn("ART interruption", new SpeedPhasesVisitARTInterruptionDataDefinition(), null);
        dsd.addColumn("ART interruption reason", new SpeedPhasesARTInterruptionReasonDataDefinition(), null);
        dsd.addColumn("Date died", new SpeedPhasesDeathDateDataDefinition(), null);

        dsd.addColumn("Weight", new SpeedPhasesVisitWeightDataDefinition(), null);
        dsd.addColumn("Height", new SpeedPhasesVisitHeightDataDefinition(), null);
        dsd.addColumn("Treatment Supporter", new SpeedPhasesHasTreatmentSupporterDataDefinition(), null);
        dsd.addColumn("Treatment Supporter Relationship", new SpeedPhasesTreatmentSupporterRelationshipDataDefinition(), null);
        dsd.addColumn("Baseline VL", new SpeedPhasesBaselineVLDataDefinition(), null);
        dsd.addColumn("Baseline WHO Stage", new SpeedPhasesWHOStagingDataDefinition(), null);
        dsd.addColumn("Blood Pressure", new SpeedPhasesBloodPressureDataDefinition(), null);
        dsd.addColumn("Date Transferred Out", new SpeedPhasesDateTransferredOutDataDefinition(), null);
        dsd.addColumn("Entry Point", new SpeedPhasesEntryPointDataDefinition(), null);
        //dsd.addColumn("Reason for Medical Switch", new SpeedPhasesReasonForMedicalSwitchDataDefinition(), null);

        dsd.addColumn("evaluationDate", new CalculationDataDefinition("Query Date", new SpeedPhasesQueryDateCalculation()),"", new GenericDateConverter());
        dsd.addRowFilter(new SpeedZeroToFourtyStudyVisitCohortDefinition(), "");
        return dsd;

    }
}