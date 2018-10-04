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
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.*;
import org.openmrs.module.SpeedPhasesReports.api.reporting.query.definition.SpeedPhasesStudyVisitQuery;
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
@Builds({"speedPhasesReports.common.report.speedReport"})
public class SpeedReportBuilder extends AbstractReportBuilder {
    public static final String DATE_FORMAT = "dd/MM/yyyy";

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
        dsd.addColumn("DOB", new BirthdateDataDefinition(), "", new BirthdateConverter(DATE_FORMAT));
        dsd.addColumn("Sex", new GenderDataDefinition(), "");
        dsd.addColumn("Unique Patient Number", identifierDef, null);
        dsd.addColumn("Marital Status", new MaritalStatusDataDefinition(), null);
        dsd.addColumn("Education Level", new SpeedPhasesEducationLevelDataDefinition(), null);
        dsd.addColumn("Date Confirmed Positive", new SpeedPhasesDateConfirmedHIVPositiveDataDefinition(), "", new DateConverter(DATE_FORMAT));
        dsd.addColumn("Date Enrolled in Care", new CalculationDataDefinition("DOE", new SpeedPhasesEnrollmentDateCalculation()), "", new CalculationResultConverter());
        dsd.addColumn("Age at Enrollment", new CalculationDataDefinition("AgeatEnrollment", new SpeedPhasesAgeAtProgramEnrollmentCalculation()), "", new CalculationResultConverter());
        //dsd.addColumn("Age at Enrollment", new SpeedPhasesAgeAtEnrollmentDataDefinition(), null);
        //dsd.addColumn("Age at ART Start", new SpeedPhasesAgeAtARTStartDataDefinition(), null);
        dsd.addColumn("Visit Date", new SpeedPhasesVisitDateDataDefinition(),"", new DateConverter(DATE_FORMAT));

        // wave two additional columns
        dsd.addColumn("Baseline VL", new SpeedPhasesBaselineVLDataDefinition(), null);
        dsd.addColumn("ART Adherence", new SpeedPhasesVisitARTAdherenceDataDefinition(), null);
        dsd.addColumn("Entry Point", new SpeedPhasesEntryPointDataDefinition(), null);
        dsd.addColumn("STI Screening", new SpeedPhasesSTIScreeningDataDefinition(), null);
        dsd.addColumn("PWP Disclosure", new PWPDisclosureDataDefinition(), null);
        dsd.addColumn("Condom Provided", new SpeedPhasesVisitCondomUseDataDefinition(), null);
        dsd.addColumn("Visit Type", new SpeedPhasesVisitTypeDataDefinition(), null);
        dsd.addColumn("Population Type", new SpeedPhasesPopulationTypeDataDefinition(), null);
        dsd.addColumn("Partner Tested", new SpeedPhasesPartnerTestedDataDefinition(), null);
        dsd.addColumn("Transfer-in Date", new SpeedPhasesTransferInDateDataDefinition(), null, new DateConverter(DATE_FORMAT));
        dsd.addColumn("Facility Transferred From", new SpeedPhasesFacilityTransferredFromDataDefinition(), null);

        // ----------------------------
        // new columns
        dsd.addColumn("Pregnancy Status", new PregnancyStatusDataDefinition(), null);
        dsd.addColumn("FP", new SpeedPhasesFPUsageDataDefinition(), null);
        dsd.addColumn("WHO Stage", new SpeedPhasesWHOStagingDataDefinition(), null);
        dsd.addColumn("STI Screening", new SpeedPhasesSTIScreeningDataDefinition(), null);
        dsd.addColumn("PWP Disclosure", new PWPDisclosureDataDefinition(), null);
        dsd.addColumn("CD4 Result", new SpeedPhasesVisitCD4DataDefinition(), null);
        dsd.addColumn("CD4 Date", new SpeedPhasesVisitCD4DateDataDefinition(), "");
        dsd.addColumn("VL Result", new SpeedPhasesViralLoadDataDefinition(), null);
        dsd.addColumn("VL Date", new SpeedPhasesViralLoadDateDataDefinition(), "");
        dsd.addColumn("Next Visit Date", new SpeedPhasesNextVisitDateDataDefinition(), "", new DateConverter(DATE_FORMAT));
        dsd.addColumn("Art Start Date", new CalculationDataDefinition("Art Start Date", new InitialArtStartDateCalculation()), "", new CalculationResultConverter());
        dsd.addColumn("Age at ART Start", new CalculationDataDefinition("Age at Art Start", new SpeedPhasesAgeAtARTStartCalculation()), "", new CalculationResultConverter());
        dsd.addColumn("Last ART Regimen", new ARTRegimenDataDefinition(), null);
        dsd.addColumn("Last ART Regimen Line", new ARTRegimenLineDataDefinition(), null);

        dsd.addColumn("Patient Discontinued", new PatientDiscontinuedDataDefinition(), null);
        dsd.addColumn("Discontinuation Reason", new ReasonPatientDiscontinuedDataDefinition(), null);
        dsd.addColumn("Visit Ois", new VisitOisDataDefinition(), null);
        dsd.addColumn("Visit Ois Date", new VisitOisDateDataDefinition(), null);

        dsd.addColumn("Exit date", new PatientDiscontinuationDateDataDefinition(), "");
        dsd.addColumn("Exit Reason", new ReasonPatientDiscontinuedDataDefinition(), null);


        dsd.addColumn("evaluationDate", new CalculationDataDefinition("Query Date", new SpeedPhasesQueryDateCalculation()),"", new CalculationResultConverter());


        dsd.addRowFilter(new SpeedPhasesStudyVisitQuery(), "");
        return dsd;

    }
}