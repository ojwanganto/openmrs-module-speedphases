package org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data;

import org.openmrs.module.reporting.data.BaseDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.PatientDataDefinition;
import org.openmrs.module.reporting.definition.configuration.ConfigurationPropertyCachingStrategy;
import org.openmrs.module.reporting.evaluation.caching.Caching;

import java.util.Date;

/**
 * Visit ID Column
 */
@Caching(strategy=ConfigurationPropertyCachingStrategy.class)
public class SpeedPhasesPatientOrphanDataDefinition extends BaseDataDefinition implements PatientDataDefinition {

    public static final long serialVersionUID = 1L;

    /**
     * Default Constructor
     */
    public SpeedPhasesPatientOrphanDataDefinition() {
        super();
    }

    /**
     * Constructor to populate name only
     */
    public SpeedPhasesPatientOrphanDataDefinition(String name) {
        super(name);
    }

    //***** INSTANCE METHODS *****

    /**
     * @see org.openmrs.module.reporting.data.DataDefinition#getDataType()
     */
    public Class<?> getDataType() {
        return Date.class;
    }
}
