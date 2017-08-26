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

package org.openmrs.module.SpeedPhasesReports.api.metadata;

import org.openmrs.module.metadatadeploy.bundle.AbstractMetadataBundle;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.globalProperty;

/**
 * Metadata constants
 */
@Component
public class ModuleReportMetadata extends AbstractMetadataBundle {

	public static final String MODULE_ID = "SpeedPhasesReports";
	public static final String START_DATE = MODULE_ID + ".startDate";
	public static final String END_DATE = MODULE_ID + ".endDate";

	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	String defaultEndDate = df.format(new Date());



	@Override
	public void install() throws Exception {
		install(globalProperty(START_DATE, "Reporting start date for Speed study report", "2015-11-01"));
		install(globalProperty(END_DATE, "Reporting end date for Speed study report", defaultEndDate));

	}
}
