package org.openmrs.module.SpeedPhasesReports.api.metadata;

import org.openmrs.module.kenyaemr.metadata.SecurityMetadata;
import org.openmrs.module.metadatadeploy.bundle.AbstractMetadataBundle;
import org.openmrs.module.metadatadeploy.bundle.Requires;
import org.springframework.stereotype.Component;

import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.idSet;
import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.privilege;
import static org.openmrs.module.metadatadeploy.bundle.CoreConstructors.role;

/**
 * Implementation of access control to the app.
 */
@Component
@Requires(SecurityMetadata.class)
public class ModuleSecurityMetadata extends AbstractMetadataBundle{

    public static class _Privilege {
        public static final String APP_SPEED_PHASES_MODULE = "App: SpeedPhasesReports.reports";
    }

    public static final class _Role {
        public static final String APPLICATION_STUDY_MODULE_ROLE = "Application: Configure SpeedPhases Reports Module";
    }

    /**
     * @see AbstractMetadataBundle#install()
     */
    @Override
    public void install() {

        install(privilege(_Privilege.APP_SPEED_PHASES_MODULE, "Able to access App for  Speed and Phases Report"));
        install(role(_Role.APPLICATION_STUDY_MODULE_ROLE, "A role for Configuring Speed and Phases Report", idSet(
                SecurityMetadata._Role.API_PRIVILEGES_VIEW_AND_EDIT
        ), idSet(
                _Privilege.APP_SPEED_PHASES_MODULE
        )));
    }
}
