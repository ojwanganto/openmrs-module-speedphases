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
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.ARTOriginalRegimenDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.ARTOriginalRegimenLineDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesDateConfirmedHIVPositiveDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesEnrollmentDateCalculation;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesFacilityTransferredFromDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesHasTreatmentSupporterDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesMUACDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesNextVisitDateDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesPatientOrphanDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesTransferInDateDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesTreatmentSupporterRelationshipDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesVisitCD4DataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesVisitCD4DateDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesVisitDateDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesVisitHeightDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesVisitWeightDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.query.definition.LisheBoraHivVisitCohortDefinition;
import org.openmrs.module.SpeedPhasesReports.api.util.ModuleUtils;
import org.openmrs.module.kenyacore.report.ReportDescriptor;
import org.openmrs.module.kenyacore.report.ReportUtils;
import org.openmrs.module.kenyacore.report.builder.AbstractReportBuilder;
import org.openmrs.module.kenyacore.report.builder.Builds;
import org.openmrs.module.kenyacore.report.data.patient.definition.CalculationDataDefinition;
import org.openmrs.module.kenyaemr.calculation.library.hiv.art.InitialArtStartDateCalculation;
import org.openmrs.module.kenyaemr.metadata.HivMetadata;
import org.openmrs.module.kenyaemr.reporting.data.converter.CalculationResultConverter;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.hei.HEIAgeAtTestConfirmatoryDataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.hei.HEIAgeAtTestInMonths12DataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.hei.HEIAgeAtTestInMonths18_24DataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.hei.HEIDNA2PCRTestTypeMonth6DataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.hei.HEIDNA3PCRTestTypeMonth12DataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.hei.HEIEnrollmentDateDataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.hei.HEIFeedingOptionsConfirmatoryDataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.hei.HEIFeedingOptionsMonth12DataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.hei.HEIFeedingOptionsMonth15DataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.hei.HEIFeedingOptionsMonth18_24DataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.hei.HEIFeedingOptionsMonth9DataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.hei.HEIFinalAntiBodyResultDataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.hei.HEIFinalAntiBodySampleDateMonth18_24DataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.hei.HEIGivenNVPCTXConfirmatoryDataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.hei.HEIGivenNVPCTXMonth12DataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.hei.HEIGivenNVPCTXMonth15DataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.hei.HEIGivenNVPCTXMonth18_24DataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.hei.HEIGivenNVPCTXMonth9DataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.hei.HEIHIVStatusMonth24DataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.hei.HEIMotherCCCNumberDataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.hei.HEIMothersNameAndTelephoneDataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.hei.HEIPCRConfirmatoryResultCollectionDateDataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.hei.HEIPCRConfirmatoryResultDateDataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.hei.HEIPCRConfirmatoryResultsStatusDataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.hei.HEIPCRConfirmatorySampleDateDataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.hei.HEIPCRConfirmatoryTestTypeDataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.hei.HEIPCRResultsCollectionDateMonth12DataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.hei.HEIPCRResultsCollectionDateMonth6DataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.hei.HEIPCRResultsStatusMonth12DataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.hei.HEIPCRResultsStatusMonth6DataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.hei.HEIPCRSampleDateMonth12DataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.hei.HEIPCRSampleDateMonth6DataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.hei.HEIRelationToInfantDataDefinition;
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
@Builds({"speedPhasesReports.common.report.lisheBoraHivVisitReport"})
public class LisheBoraHivVisitReportBuilder extends AbstractReportBuilder {
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

        DataConverter nameFormatter = new ObjectFormatter("{familyName}, {givenName}");
        DataDefinition nameDef = new ConvertedPersonDataDefinition("name", new PreferredNameDataDefinition(), nameFormatter);


        PatientIdentifierType upn = MetadataUtils.existing(PatientIdentifierType.class, HivMetadata._PatientIdentifierType.UNIQUE_PATIENT_NUMBER);
        PatientIdentifierType heiId = MetadataUtils.existing(PatientIdentifierType.class, ModuleUtils.HEI_UNIQUE_NUMBER);
        DataConverter identifierFormatter = new ObjectFormatter("{identifier}");
        DataDefinition identifierDef = new ConvertedPatientDataDefinition("identifier", new PatientIdentifierDataDefinition(upn.getName(), upn), identifierFormatter);
        DataDefinition heiIdDef = new ConvertedPatientDataDefinition("identifier", new PatientIdentifierDataDefinition(heiId.getName(), heiId), identifierFormatter);

        //dsd.addColumn("VISIT ID", new VisitIdDataDefinition(), null);

        dsd.addColumn("id", new PatientIdDataDefinition(), "");
        dsd.addColumn("Name", nameDef, "");
        dsd.addColumn("DOB", new BirthdateDataDefinition(), "", new BirthdateConverter(DATE_FORMAT));
        dsd.addColumn("Sex", new GenderDataDefinition(), "");
        dsd.addColumn("HEI ID", heiIdDef, null);
        dsd.addColumn("Unique Patient Number", identifierDef, null);
        dsd.addColumn("gc_orphan", new SpeedPhasesPatientOrphanDataDefinition(), null);
        dsd.addColumn("Treatment Supporter", new SpeedPhasesHasTreatmentSupporterDataDefinition(), null);
        dsd.addColumn("Treatment Supporter Relationship", new SpeedPhasesTreatmentSupporterRelationshipDataDefinition(), null);
        dsd.addColumn("HEI Enrollment Date", new HEIEnrollmentDateDataDefinition(), null);
        dsd.addColumn("Mothers Name and Phone", new HEIMothersNameAndTelephoneDataDefinition(),"");

        dsd.addColumn("Maternal CCC Number", new HEIMotherCCCNumberDataDefinition(), null);
        dsd.addColumn("Relation to infant", new HEIRelationToInfantDataDefinition(), null);

        dsd.addColumn("Date Confirmed Positive", new SpeedPhasesDateConfirmedHIVPositiveDataDefinition(), "", new DateConverter(DATE_FORMAT));
        dsd.addColumn("Date Enrolled in Care", new CalculationDataDefinition("DOE", new SpeedPhasesEnrollmentDateCalculation()), "", new CalculationResultConverter());

        dsd.addColumn("Art Start Date", new CalculationDataDefinition("Art Start Date", new InitialArtStartDateCalculation()), "", new GenericDateConverter());

        dsd.addColumn("Visit Date", new SpeedPhasesVisitDateDataDefinition(),"", new DateConverter(DATE_FORMAT));

        dsd.addColumn("Weight", new SpeedPhasesVisitWeightDataDefinition(), null);
        dsd.addColumn("Height", new SpeedPhasesVisitHeightDataDefinition(), null);
        dsd.addColumn("MUAC", new SpeedPhasesMUACDataDefinition(), null);

        dsd.addColumn("Transfer-in Date", new SpeedPhasesTransferInDateDataDefinition(), null, new DateConverter(DATE_FORMAT));
        dsd.addColumn("Facility Transferred From", new SpeedPhasesFacilityTransferredFromDataDefinition(), null);

        // ----------------------------

        dsd.addColumn("CD4 Result", new SpeedPhasesVisitCD4DataDefinition(), null);
        dsd.addColumn("CD4 Date", new SpeedPhasesVisitCD4DateDataDefinition(), "");

        dsd.addColumn("StartRegimen", new ARTOriginalRegimenDataDefinition(), null);
        dsd.addColumn("StartRegimen Line", new ARTOriginalRegimenLineDataDefinition(), null);

        // -----------------
        dsd.addColumn("Test Type 6 months", new HEIDNA2PCRTestTypeMonth6DataDefinition(),"");
        dsd.addColumn("Sample Date 6 months", new HEIPCRSampleDateMonth6DataDefinition(),"");
        dsd.addColumn("Results Date 6 months", new HEIPCRResultsCollectionDateMonth6DataDefinition(),"");
        dsd.addColumn("Results Status 6 months", new HEIPCRResultsStatusMonth6DataDefinition(),"");
        dsd.addColumn("Feeding option 9 months", new HEIFeedingOptionsMonth9DataDefinition(),"");
        dsd.addColumn("Given NVPCTX 9 months", new HEIGivenNVPCTXMonth9DataDefinition(),"");
//		dsd.addColumn("Given NVP 9 months", new HEIGivenNVPMonth9DataDefinition(),"");
//		dsd.addColumn("Given CTX 9 months", new HEIGivenCTXMonth9DataDefinition(),"");
        dsd.addColumn("Feeding option 12 months", new HEIFeedingOptionsMonth12DataDefinition(),"");
        dsd.addColumn("Given NVPCTX 12 months", new HEIGivenNVPCTXMonth12DataDefinition(),"");
//		dsd.addColumn("Given NVP 12 months", new HEIGivenNVPMonth12DataDefinition(),"");
//		dsd.addColumn("Given CTX 12 months", new HEIGivenCTXMonth12DataDefinition(),"");
        dsd.addColumn("Age at Test 12 months", new HEIAgeAtTestInMonths12DataDefinition(),"");
        dsd.addColumn("Test Type 12 months", new HEIDNA3PCRTestTypeMonth12DataDefinition(),"");
        dsd.addColumn("Sample Date 12 months", new HEIPCRSampleDateMonth12DataDefinition(),"");
        dsd.addColumn("Results Date 12 months", new HEIPCRResultsCollectionDateMonth12DataDefinition(),"");
        dsd.addColumn("Results Status 12 months", new HEIPCRResultsStatusMonth12DataDefinition(),"");
        dsd.addColumn("Feeding option 15 months", new HEIFeedingOptionsMonth15DataDefinition(),"");
        dsd.addColumn("Given NVPCTX 15 months", new HEIGivenNVPCTXMonth15DataDefinition(),"");

        dsd.addColumn("Feeding option Confirmatory", new HEIFeedingOptionsConfirmatoryDataDefinition(),"");
        dsd.addColumn("Given NVPCTX Confirmatory", new HEIGivenNVPCTXConfirmatoryDataDefinition(),"");

        dsd.addColumn("Age at Test Confirmatory", new HEIAgeAtTestConfirmatoryDataDefinition(),"");
        dsd.addColumn("Test Type Confirmatory", new HEIPCRConfirmatoryTestTypeDataDefinition(),"");
        dsd.addColumn("Sample Date Confirmatory", new HEIPCRConfirmatorySampleDateDataDefinition(),"");
        dsd.addColumn("Results Date Confirmatory", new HEIPCRConfirmatoryResultDateDataDefinition(),"");
        dsd.addColumn("Results Collection Date Confirmatory", new HEIPCRConfirmatoryResultCollectionDateDataDefinition(),"");
        dsd.addColumn("Results Status confirmatory", new HEIPCRConfirmatoryResultsStatusDataDefinition(),"");
        dsd.addColumn("Feeding options 18-24 months", new HEIFeedingOptionsMonth18_24DataDefinition(),"");
        dsd.addColumn("Given NVPCTX 18-24 months", new HEIGivenNVPCTXMonth18_24DataDefinition(),"");
//		dsd.addColumn("Given NVP 18-24 months", new HEIGivenNVPMonth18_24DataDefinition(),"");
//		dsd.addColumn("Given CTX 18-24 months", new HEIGivenCTXMonth18_24DataDefinition(),"");
        dsd.addColumn("Antibody Test Date at 18-24 months", new HEIFinalAntiBodySampleDateMonth18_24DataDefinition(),"");
        dsd.addColumn("Age in Months ", new HEIAgeAtTestInMonths18_24DataDefinition(),"");
        dsd.addColumn("Antibody Test results at 18-24 months", new HEIFinalAntiBodyResultDataDefinition(),"");
        dsd.addColumn("HIV Status at 24 months", new HEIHIVStatusMonth24DataDefinition(),"");

        // -----------------

        dsd.addColumn("Next Visit Date", new SpeedPhasesNextVisitDateDataDefinition(), "", new DateConverter(DATE_FORMAT));



        LisheBoraHivVisitCohortDefinition cd = new LisheBoraHivVisitCohortDefinition();
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        dsd.addRowFilter(cd, "startDate=${startDate},endDate=${endDate}");
        return dsd;

    }
}