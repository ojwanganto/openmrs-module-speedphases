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

/**
 * Metadata constants
 */
@Component
public class ModuleReportMetadata extends AbstractMetadataBundle {

	public static final String MODULE_ID = "SpeedPhasesReports";
	public static final String DEFAULT_COHORT = MODULE_ID + ".default_cohort";
	private String defaultConfig = "";


	@Override
	public void install() throws Exception {
		//install(globalProperty(DEFAULT_COHORT, "HRS Study Default Cohort. Should be a list of patient IDs else all patients qualify", defaultConfig));

	}
}
