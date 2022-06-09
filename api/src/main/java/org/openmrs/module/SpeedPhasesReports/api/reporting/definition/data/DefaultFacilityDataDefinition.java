package org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data;

import org.openmrs.module.reporting.data.BaseDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.PatientDataDefinition;
import org.openmrs.module.reporting.definition.configuration.ConfigurationProperty;
import org.openmrs.module.reporting.definition.configuration.ConfigurationPropertyCachingStrategy;
import org.openmrs.module.reporting.evaluation.caching.Caching;

/**
 * Default facility Column
 */
@Caching(strategy=ConfigurationPropertyCachingStrategy.class)
public class DefaultFacilityDataDefinition extends BaseDataDefinition implements PatientDataDefinition {

    public static final long serialVersionUID = 1L;
    @ConfigurationProperty
    private String propertyName;

    /**
     * Default Constructor
     */
    public DefaultFacilityDataDefinition() {
        super();
    }

    /**
     * Constructor to populate name only
     */
    public DefaultFacilityDataDefinition(String name) {
        super(name);
    }

    public DefaultFacilityDataDefinition(String name, String propertyName) {
        super(name);
        this.propertyName = propertyName;
    }

    //***** INSTANCE METHODS *****

    /**
     * @see org.openmrs.module.reporting.data.DataDefinition#getDataType()
     */
    public Class<?> getDataType() {
        return String.class;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }
}
