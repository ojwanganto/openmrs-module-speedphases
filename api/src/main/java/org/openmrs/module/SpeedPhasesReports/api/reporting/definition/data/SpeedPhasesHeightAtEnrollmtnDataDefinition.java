package org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data;

import org.openmrs.module.reporting.data.BaseDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.PatientDataDefinition;
import org.openmrs.module.reporting.definition.configuration.ConfigurationPropertyCachingStrategy;
import org.openmrs.module.reporting.evaluation.caching.Caching;

/**
 * Visit ID Column
 */
@Caching(strategy=ConfigurationPropertyCachingStrategy.class)
public class SpeedPhasesHeightAtEnrollmtnDataDefinition extends BaseDataDefinition implements PatientDataDefinition {

    public static final long serialVersionUID = 1L;

    /**
     * Default Constructor
     */
    public SpeedPhasesHeightAtEnrollmtnDataDefinition() {
        super();
    }

    /**
     * Constructor to populate name only
     */
    public SpeedPhasesHeightAtEnrollmtnDataDefinition(String name) {
        super(name);
    }

    //***** INSTANCE METHODS *****

    /**
     * @see org.openmrs.module.reporting.data.DataDefinition#getDataType()
     */
    public Class<?> getDataType() {
        return Double.class;
    }
}
