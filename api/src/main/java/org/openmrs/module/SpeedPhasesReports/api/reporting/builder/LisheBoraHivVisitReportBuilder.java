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
import org.openmrs.module.SpeedPhasesReports.api.reporting.calculation.*;
import org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data.*;
import org.openmrs.module.SpeedPhasesReports.api.reporting.query.definition.LisheBoraHivVisitCohortDefinition;
import org.openmrs.module.SpeedPhasesReports.api.util.ModuleUtils;
import org.openmrs.module.kenyacore.report.ReportDescriptor;
import org.openmrs.module.kenyacore.report.ReportUtils;
import org.openmrs.module.kenyacore.report.builder.AbstractReportBuilder;
import org.openmrs.module.kenyacore.report.builder.Builds;
import org.openmrs.module.kenyacore.report.data.patient.definition.CalculationDataDefinition;
import org.openmrs.module.kenyaemr.Dictionary;
import org.openmrs.module.kenyaemr.metadata.HivMetadata;
import org.openmrs.module.kenyaemr.reporting.calculation.converter.ObsValueNumericConverter;
import org.openmrs.module.kenyaemr.reporting.calculation.converter.WHOStageDataConverter;
import org.openmrs.module.kenyaemr.reporting.data.converter.CalculationResultConverter;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.art.ETLArtStartDateDataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.art.ETLFirstRegimenDataDefinition;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.reporting.common.TimeQualifier;
import org.openmrs.module.reporting.data.DataDefinition;
import org.openmrs.module.reporting.data.converter.BirthdateConverter;
import org.openmrs.module.reporting.data.converter.DataConverter;
import org.openmrs.module.reporting.data.converter.DateConverter;
import org.openmrs.module.reporting.data.converter.ObjectFormatter;
import org.openmrs.module.reporting.data.patient.definition.ConvertedPatientDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.PatientIdDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.PatientIdentifierDataDefinition;
import org.openmrs.module.reporting.data.person.definition.*;
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

        dsd.addColumn("Date Confirmed Positive", new SpeedPhasesDateConfirmedHIVPositiveDataDefinition(), "", new DateConverter(DATE_FORMAT));
        dsd.addColumn("Date Enrolled in Care", new CalculationDataDefinition("DOE", new SpeedPhasesEnrollmentDateCalculation()), "", new CalculationResultConverter());
        dsd.addColumn("Art Start Date", new ETLArtStartDateDataDefinition(), "", new DateConverter(DATE_FORMAT));
        dsd.addColumn("First Regimen", new ETLFirstRegimenDataDefinition(), "");

        // -------------
        dsd.addColumn("Weight at enrollment", new CalculationDataDefinition("Weight at enrollment", new SpeedPhasesWeightAtEnrollmentCalculation()), "", new CalculationResultConverter());
        dsd.addColumn("Height at enrollment", new CalculationDataDefinition("Height at enrollment", new SpeedPhasesHeightAtEnrollmentCalculation()), "", new CalculationResultConverter());
        dsd.addColumn("MUAC at enrollment", new CalculationDataDefinition("MUAC at enrollment", new SpeedPhasesMuacAtEnrollmentCalculation()), "", new CalculationResultConverter());
        dsd.addColumn("WHO Stage at enrollment", new CalculationDataDefinition("WHO Stage at enrollment", new SpeedPhasesWhoStageAtEnrollmentCalculation()), "", new CalculationResultConverter());
        dsd.addColumn("Baseline VL", new CalculationDataDefinition("Baseline VL", new SpeedPhasesBaselineVLAtEnrollmentCalculation()), "", new CalculationResultConverter());
        dsd.addColumn("Baseline VL Date1", new CalculationDataDefinition("Baseline VL Date", new SpeedPhasesBaselineVLDateAtEnrollmentCalculation()), "", new CalculationResultConverter());
        dsd.addColumn("Baseline CD4", new CalculationDataDefinition("Baseline CD4", new SpeedPhasesCD4AtEnrollmentStartCalculation()), "", new CalculationResultConverter());
        dsd.addColumn("Breastfeeding at enrollment", new CalculationDataDefinition("Breastfeeding at enrollment", new SpeedPhasesBreastfeedingAtEnrollmentCalculation()), "", new CalculationResultConverter());
        dsd.addColumn("Nutritional status", new SpeedPhasesNutritionalStatusDataDefinition(), "");



        //------------
        dsd.addColumn("Visit Date", new SpeedPhasesVisitDateDataDefinition(),"", new DateConverter(DATE_FORMAT));

        dsd.addColumn("Weight", new SpeedPhasesVisitWeightDataDefinition(), null);
        dsd.addColumn("Height", new SpeedPhasesVisitHeightDataDefinition(), null);

        dsd.addColumn("MUAC", new SpeedPhasesMUACDataDefinition(), null);

        dsd.addColumn("Transfer-in Date", new SpeedPhasesTransferInDateDataDefinition(), null, new DateConverter(DATE_FORMAT));
        dsd.addColumn("Facility Transferred From", new SpeedPhasesFacilityTransferredFromDataDefinition(), null);

        // ----------------------------
        dsd.addColumn("First WHO Stage", new ObsForPersonDataDefinition("First WHO Stage", TimeQualifier.FIRST, Dictionary.getConcept(Dictionary.CURRENT_WHO_STAGE), null, null), "", new WHOStageDataConverter());
        dsd.addColumn("First CD4 Count", new ObsForPersonDataDefinition("First CD4 Count", TimeQualifier.FIRST, Dictionary.getConcept(Dictionary.CD4_COUNT), null, null), "", new ObsValueNumericConverter(1));

        dsd.addColumn("CD4 Result", new SpeedPhasesVisitCD4DataDefinition(), null);
        dsd.addColumn("CD4 Date", new SpeedPhasesVisitCD4DateDataDefinition(), "");

       /* dsd.addColumn("Baseline VL", new ETLFirstVLResultDataDefinition(), null);
        dsd.addColumn("Baseline VL Date", new ETLFirstVLDateDataDefinition(), null, new DateConverter(DATE_FORMAT));
*/
        dsd.addColumn("StartRegimen", new ARTOriginalRegimenDataDefinition(), null);
        dsd.addColumn("StartRegimen Line", new ARTOriginalRegimenLineDataDefinition(), null);

        // -----------------


        dsd.addColumn("Next Visit Date", new SpeedPhasesNextVisitDateDataDefinition(), "", new DateConverter(DATE_FORMAT));



        LisheBoraHivVisitCohortDefinition cd = new LisheBoraHivVisitCohortDefinition();
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        dsd.addRowFilter(cd, "startDate=${startDate},endDate=${endDate}");
        return dsd;

    }
}