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
import org.openmrs.module.SpeedPhasesReports.api.reporting.converter.DCOMConverter;
import org.openmrs.module.SpeedPhasesReports.api.reporting.converter.DiscMaritalStatusConverter;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.ARTOriginalRegimenDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.ARTOriginalRegimenLineDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.ARTRegimenDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.ARTRegimenLineDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.DCOMDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.DiscLastArtDateDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.DiscLastArtDurationDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.MaritalStatusDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.PWPDisclosureDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.PatientDiscontinuationDateDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.PatientDiscontinuedDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.PatientStabilityDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.PregnancyStatusDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.ReasonPatientDiscontinuedDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesAgeAtARTStartCalculation;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesAgeAtProgramEnrollmentCalculation;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesBCGDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesBaselineVLDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesBaselineVLDateDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesBloodPressureDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesCacxScreeningDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesDateConfirmedHIVPositiveDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesEducationLevelDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesEnhancedAdherenceScoreDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesEnrollmentDateCalculation;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesEntryPointDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesFPMethodDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesFPUsageDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesFacilityTransferredFromDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesHasAllergiesDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesHasChronicIllnessDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesHasComplaintsDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesKeyPopulationTypeDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesLMPDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesMUACDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesMeasles1DataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesNextVisitDateDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesOPV1DataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesOPV2DataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesOTZActivityAtEnrollmentDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesOTZDiscontinuationDateDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesOTZDiscontinuationReasonDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesOTZServicesDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesOVCEnrollmentDetailsDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesPartnerTestedDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesPatientOrphanDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesPenta1DataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesPenta2DataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesPopulationTypeDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesQueryDateCalculation;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesSTIScreeningDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesStiPartnerNotificationDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesTransferInDateDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesViralLoadDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesViralLoadDateDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesVisitARTAdherenceDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesVisitCD4DataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesVisitCD4DateDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesVisitCTXAdherenceDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesVisitCondomUseDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesVisitDateDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesVisitEDDDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesVisitHeightDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesVisitTBStatusDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesVisitTypeDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesVisitWeightDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.SpeedPhasesWHOStagingDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.VisitOisDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.VisitOisDateDataDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.query.definition.DartStudyViralLoadCohortDefinition;
import org.openmrs.module.SpeedPhasesReports.api.reporting.query.definition.DartStudyVisitCohortDefinition;
import org.openmrs.module.kenyacore.report.ReportDescriptor;
import org.openmrs.module.kenyacore.report.ReportUtils;
import org.openmrs.module.kenyacore.report.builder.AbstractReportBuilder;
import org.openmrs.module.kenyacore.report.builder.Builds;
import org.openmrs.module.kenyacore.report.data.patient.definition.CalculationDataDefinition;
import org.openmrs.module.kenyaemr.calculation.library.hiv.art.InitialArtStartDateCalculation;
import org.openmrs.module.kenyaemr.metadata.HivMetadata;
import org.openmrs.module.kenyaemr.reporting.data.converter.CalculationResultConverter;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.reporting.data.DataDefinition;
import org.openmrs.module.reporting.data.converter.BirthdateConverter;
import org.openmrs.module.reporting.data.converter.DataConverter;
import org.openmrs.module.reporting.data.converter.DateConverter;
import org.openmrs.module.reporting.data.converter.ObjectFormatter;
import org.openmrs.module.reporting.data.encounter.definition.EncounterIdDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.ConvertedPatientDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.PatientIdDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.PatientIdentifierDataDefinition;
import org.openmrs.module.reporting.data.person.definition.BirthdateDataDefinition;
import org.openmrs.module.reporting.data.person.definition.ConvertedPersonDataDefinition;
import org.openmrs.module.reporting.data.person.definition.GenderDataDefinition;
import org.openmrs.module.reporting.data.person.definition.PreferredNameDataDefinition;
import org.openmrs.module.reporting.data.visit.definition.VisitIdDataDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.EncounterDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.VisitDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
@Builds({"speedPhasesReports.common.report.dartReport"})
public class DartReportBuilder extends AbstractReportBuilder {
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
                ReportUtils.map(datasetColumns(), "startDate=${startDate},endDate=${endDate}"),
                ReportUtils.map(vlDatasetColumns(), "startDate=${startDate},endDate=${endDate}")
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
        DataConverter identifierFormatter = new ObjectFormatter("{identifier}");
        DataDefinition identifierDef = new ConvertedPatientDataDefinition("identifier", new PatientIdentifierDataDefinition(upn.getName(), upn), identifierFormatter);

        dsd.addColumn("VISIT ID", new VisitIdDataDefinition(), null);
        dsd.addColumn("id", new PatientIdDataDefinition(), "");
        dsd.addColumn("Name", nameDef, "");
        dsd.addColumn("DOB", new BirthdateDataDefinition(), "", new BirthdateConverter(DATE_FORMAT));
        dsd.addColumn("Sex", new GenderDataDefinition(), "");
        dsd.addColumn("Unique Patient Number", identifierDef, null);
        dsd.addColumn("Marital Status", new MaritalStatusDataDefinition(), null, new DiscMaritalStatusConverter());
        dsd.addColumn("Education Level", new SpeedPhasesEducationLevelDataDefinition(), null);
        dsd.addColumn("Date Confirmed Positive", new SpeedPhasesDateConfirmedHIVPositiveDataDefinition(), "", new DateConverter(DATE_FORMAT));
        dsd.addColumn("Date Enrolled in Care", new CalculationDataDefinition("DOE", new SpeedPhasesEnrollmentDateCalculation()), "", new CalculationResultConverter());
        dsd.addColumn("Age at Enrollment", new CalculationDataDefinition("AgeatEnrollment", new SpeedPhasesAgeAtProgramEnrollmentCalculation()), "", new CalculationResultConverter());
        dsd.addColumn("gc_orphan", new SpeedPhasesPatientOrphanDataDefinition(), null);
        //dsd.addColumn("Age at Enrollment", new SpeedPhasesAgeAtEnrollmentDataDefinition(), null);
        //dsd.addColumn("Age at ART Start", new SpeedPhasesAgeAtARTStartDataDefinition(), null);
        dsd.addColumn("Visit Date", new SpeedPhasesVisitDateDataDefinition(),"", new DateConverter(DATE_FORMAT));

        // wave two additional columns
        dsd.addColumn("Baseline VL", new SpeedPhasesBaselineVLDataDefinition(), null);
        dsd.addColumn("Baseline VL Date", new SpeedPhasesBaselineVLDateDataDefinition(), null);
        dsd.addColumn("ART Adherence", new SpeedPhasesVisitARTAdherenceDataDefinition(), null);
        dsd.addColumn("CTX Adherence", new SpeedPhasesVisitCTXAdherenceDataDefinition(), null);
        dsd.addColumn("Entry Point", new SpeedPhasesEntryPointDataDefinition(), null);
        dsd.addColumn("STI Screening", new SpeedPhasesSTIScreeningDataDefinition(), null);
        dsd.addColumn("PWP Disclosure", new PWPDisclosureDataDefinition(), null);
        dsd.addColumn("Condom Provided", new SpeedPhasesVisitCondomUseDataDefinition(), null);
        dsd.addColumn("Visit Type", new SpeedPhasesVisitTypeDataDefinition(), null);
        dsd.addColumn("Population Type", new SpeedPhasesPopulationTypeDataDefinition(), null);
        dsd.addColumn("Key Population Type", new SpeedPhasesKeyPopulationTypeDataDefinition(), null);
        dsd.addColumn("Partner Tested", new SpeedPhasesPartnerTestedDataDefinition(), null);
        dsd.addColumn("Transfer-in Date", new SpeedPhasesTransferInDateDataDefinition(), null, new DateConverter(DATE_FORMAT));
        dsd.addColumn("Facility Transferred From", new SpeedPhasesFacilityTransferredFromDataDefinition(), null);

        // ----------------------------
        dsd.addColumn("Weight", new SpeedPhasesVisitWeightDataDefinition(), null);
        dsd.addColumn("Height", new SpeedPhasesVisitHeightDataDefinition(), null);
        dsd.addColumn("MUAC", new SpeedPhasesMUACDataDefinition(), null);
        dsd.addColumn("Blood Pressure", new SpeedPhasesBloodPressureDataDefinition(), null);

        dsd.addColumn("TB Status", new SpeedPhasesVisitTBStatusDataDefinition(), null);
        // new columns
        dsd.addColumn("Pregnancy Status", new PregnancyStatusDataDefinition(), null);
        dsd.addColumn("LMP", new SpeedPhasesLMPDataDefinition(), null);
        dsd.addColumn("EDD", new SpeedPhasesVisitEDDDataDefinition(), null);

        dsd.addColumn("FP", new SpeedPhasesFPUsageDataDefinition(), null);
        dsd.addColumn("WHO Stage", new SpeedPhasesWHOStagingDataDefinition(), null);
        dsd.addColumn("PWP Disclosure", new PWPDisclosureDataDefinition(), null);
        dsd.addColumn("CD4 Result", new SpeedPhasesVisitCD4DataDefinition(), null);
        dsd.addColumn("CD4 Date", new SpeedPhasesVisitCD4DateDataDefinition(), "");
        dsd.addColumn("VL Result", new SpeedPhasesViralLoadDataDefinition(), null);
        dsd.addColumn("VL Date", new SpeedPhasesViralLoadDateDataDefinition(), "");
        dsd.addColumn("Next Visit Date", new SpeedPhasesNextVisitDateDataDefinition(), "", new DateConverter(DATE_FORMAT));
        dsd.addColumn("Art Start Date", new CalculationDataDefinition("Art Start Date", new InitialArtStartDateCalculation()), "", new CalculationResultConverter());
        dsd.addColumn("Age at ART Start", new CalculationDataDefinition("Age at Art Start", new SpeedPhasesAgeAtARTStartCalculation()), "", new CalculationResultConverter());

        dsd.addColumn("StartRegimen", new ARTOriginalRegimenDataDefinition(), null);
        dsd.addColumn("StartRegimen Line", new ARTOriginalRegimenLineDataDefinition(), null);

        dsd.addColumn("Last ART Regimen", new ARTRegimenDataDefinition(), null);
        dsd.addColumn("Last ART Regimen Line", new ARTRegimenLineDataDefinition(), null);
        dsd.addColumn("Last ART Date", new DiscLastArtDateDataDefinition(), null, new DateConverter(DATE_FORMAT));
        dsd.addColumn("Duration", new DiscLastArtDurationDataDefinition(), null);

        /*dsd.addColumn("Patient Discontinued", new PatientDiscontinuedDataDefinition(), null);
        dsd.addColumn("Discontinuation Reason", new ReasonPatientDiscontinuedDataDefinition(), null);
        */
        dsd.addColumn("Visit Ois", new VisitOisDataDefinition(), null);
        dsd.addColumn("Visit Ois Date", new VisitOisDateDataDefinition(), null);

        dsd.addColumn("Stable/Unstable", new PatientStabilityDataDefinition(), null);
        dsd.addColumn("CurrentCareModel", new DCOMDataDefinition(), null,new DCOMConverter());

        dsd.addColumn("gc_allergies1", new SpeedPhasesHasAllergiesDataDefinition(), null,null);
        dsd.addColumn("gc_complains", new SpeedPhasesHasComplaintsDataDefinition(), null,null);
        dsd.addColumn("gc_anyillness", new SpeedPhasesHasChronicIllnessDataDefinition(), null,null);

        // immunizations
        dsd.addColumn("gc_bcg", new SpeedPhasesBCGDataDefinition(), null,null);
        dsd.addColumn("gc_pv_1", new SpeedPhasesOPV1DataDefinition(), null,null);
        dsd.addColumn("gc_pv_2", new SpeedPhasesOPV2DataDefinition(), null,null);
        dsd.addColumn("gc_penta_1", new SpeedPhasesPenta1DataDefinition(), null,null);
        dsd.addColumn("gc_penta_2", new SpeedPhasesPenta2DataDefinition(), null,null);
        dsd.addColumn("gc_measles_1", new SpeedPhasesMeasles1DataDefinition(), null,null);


        //
        dsd.addColumn("gc_fpmethod", new SpeedPhasesFPMethodDataDefinition(), "");
        dsd.addColumn("gc_cacxscreen", new SpeedPhasesCacxScreeningDataDefinition(), "");
        dsd.addColumn("STI Partner Notification", new SpeedPhasesStiPartnerNotificationDataDefinition(), null);
        dsd.addColumn("ad_adherence_score", new SpeedPhasesEnhancedAdherenceScoreDataDefinition(), "");

        // OTZ columns

        dsd.addColumn("otze_enroldate", new SpeedPhasesOTZActivityAtEnrollmentDataDefinition("otzEnrollment", "visit_date"), "");
        dsd.addColumn("otze_orientation", new SpeedPhasesOTZActivityAtEnrollmentDataDefinition("otzEnrollment", "orientation"), "");
        dsd.addColumn("otze_participation", new SpeedPhasesOTZActivityAtEnrollmentDataDefinition("otzEnrollment", "participation"), "");
        dsd.addColumn("otze_leadership", new SpeedPhasesOTZActivityAtEnrollmentDataDefinition("otzEnrollment", "leadership"), "");
        dsd.addColumn("otze_descision_making", new SpeedPhasesOTZActivityAtEnrollmentDataDefinition("otzEnrollment", "making_decision_future"), "");
        dsd.addColumn("otze_trans_adultcare", new SpeedPhasesOTZActivityAtEnrollmentDataDefinition("otzEnrollment", "transition_to_adult_care"), "");
        dsd.addColumn("otze_treat_litracy", new SpeedPhasesOTZActivityAtEnrollmentDataDefinition("otzEnrollment", "treatment_literacy"), "");
        dsd.addColumn("otze_SRH", new SpeedPhasesOTZActivityAtEnrollmentDataDefinition("otzEnrollment", "srh"), "");
        dsd.addColumn("otze_bey3rd_90", new SpeedPhasesOTZActivityAtEnrollmentDataDefinition("otzEnrollment", "beyond_third_ninety"), "");

        dsd.addColumn("otzv_orientation", new SpeedPhasesOTZServicesDataDefinition("otzVisit", "orientation"), "");
        dsd.addColumn("otzv_participation", new SpeedPhasesOTZServicesDataDefinition("otzVisit", "participation"), "");
        dsd.addColumn("otzv_leadership", new SpeedPhasesOTZServicesDataDefinition("otzVisit", "leadership"), "");
        dsd.addColumn("otzv_descision_making", new SpeedPhasesOTZServicesDataDefinition("otzVisit", "making_decision_future"), "");
        dsd.addColumn("otzv_trans_adultcare", new SpeedPhasesOTZServicesDataDefinition("otzVisit", "transition_to_adult_care"), "");
        dsd.addColumn("otzv_treat_litracy", new SpeedPhasesOTZServicesDataDefinition("otzVisit", "treatment_literacy"), "");
        dsd.addColumn("otzv_SRH", new SpeedPhasesOTZServicesDataDefinition("otzVisit", "srh"), "");
        dsd.addColumn("otzv_bey3rd_90", new SpeedPhasesOTZServicesDataDefinition("otzVisit", "beyond_third_ninety"), "");
        dsd.addColumn("otzv_disc_reason", new SpeedPhasesOTZDiscontinuationReasonDataDefinition(), "");
        dsd.addColumn("otzv_disc_date", new SpeedPhasesOTZDiscontinuationDateDataDefinition(), "");

        dsd.addColumn("ovc_enroldate", new SpeedPhasesOVCEnrollmentDetailsDataDefinition("ovcEnrollment", "visit_date"), "");
        dsd.addColumn("ovc_caregivername", new SpeedPhasesOVCEnrollmentDetailsDataDefinition("ovcEnrollment", "caregiver_name"), "");
        dsd.addColumn("ovc_caregivergender", new SpeedPhasesOVCEnrollmentDetailsDataDefinition("ovcEnrollment", "caregiver_gender"), "");
        dsd.addColumn("ovc_caregiverphone", new SpeedPhasesOVCEnrollmentDetailsDataDefinition("ovcEnrollment", "caregiver_phone_number"), "");
        dsd.addColumn("ovc_cpims_enrollment", new SpeedPhasesOVCEnrollmentDetailsDataDefinition("ovcEnrollment", "client_enrolled_cpims"), "");
        dsd.addColumn("ovc_offer_partner", new SpeedPhasesOVCEnrollmentDetailsDataDefinition("ovcEnrollment", "partner_offering_ovc"), "");
        dsd.addColumn("ovc_compmodel", new SpeedPhasesOVCEnrollmentDetailsDataDefinition("ovcEnrollment", "ovc_comprehensive_program"), "");
        dsd.addColumn("ovc_dreamsmodel", new SpeedPhasesOVCEnrollmentDetailsDataDefinition("ovcEnrollment", "dreams_program"), "");
        dsd.addColumn("ovc_preventiomodel", new SpeedPhasesOVCEnrollmentDetailsDataDefinition("ovcEnrollment", "ovc_preventive_program"), "");


        dsd.addColumn("Exit date", new PatientDiscontinuationDateDataDefinition(), "");
        dsd.addColumn("Exit Reason", new ReasonPatientDiscontinuedDataDefinition(), null);


        dsd.addColumn("evaluationDate", new CalculationDataDefinition("Query Date", new SpeedPhasesQueryDateCalculation()),"", new CalculationResultConverter());

        DartStudyVisitCohortDefinition cd = new DartStudyVisitCohortDefinition();
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        dsd.addRowFilter(cd, "startDate=${startDate},endDate=${endDate}");
        return dsd;

    }

    /**
     * Add viral load dataset
     * @return
     */
    protected DataSetDefinition vlDatasetColumns() {
        EncounterDataSetDefinition dsd = new EncounterDataSetDefinition();
        dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
        dsd.setName("ViralLoadInformation");
        dsd.setDescription("Viral load information");

        DataConverter nameFormatter = new ObjectFormatter("{familyName}, {givenName}");
        DataDefinition nameDef = new ConvertedPersonDataDefinition("name", new PreferredNameDataDefinition(), nameFormatter);


        PatientIdentifierType upn = MetadataUtils.existing(PatientIdentifierType.class, HivMetadata._PatientIdentifierType.UNIQUE_PATIENT_NUMBER);
        DataConverter identifierFormatter = new ObjectFormatter("{identifier}");
        DataDefinition identifierDef = new ConvertedPatientDataDefinition("identifier", new PatientIdentifierDataDefinition(upn.getName(), upn), identifierFormatter);

        dsd.addColumn("Encounter ID", new EncounterIdDataDefinition(), null);
        dsd.addColumn("id", new PatientIdDataDefinition(), "");
        dsd.addColumn("Name", nameDef, "");
        dsd.addColumn("Unique Patient Number", identifierDef, null);

        DartStudyViralLoadCohortDefinition cd = new DartStudyViralLoadCohortDefinition();
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        dsd.addRowFilter(cd, "startDate=${startDate},endDate=${endDate}");
        return dsd;

    }
}