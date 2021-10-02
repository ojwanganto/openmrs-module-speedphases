package org.openmrs.module.SpeedPhasesReports.api.reporting.definition.data;

import org.openmrs.module.reporting.data.BaseDataDefinition;
import org.openmrs.module.reporting.data.visit.definition.VisitDataDefinition;
import org.openmrs.module.reporting.definition.configuration.ConfigurationProperty;
import org.openmrs.module.reporting.definition.configuration.ConfigurationPropertyCachingStrategy;
import org.openmrs.module.reporting.evaluation.caching.Caching;

/**
 * Visit ID Column
 */
@Caching(strategy=ConfigurationPropertyCachingStrategy.class)
public class SpeedPhasesOTZServicesDataDefinition extends BaseDataDefinition implements VisitDataDefinition {

    public static final long serialVersionUID = 1L;

    @ConfigurationProperty
    private String otzServiceName;

    /**
     * Default Constructor
     */
    public SpeedPhasesOTZServicesDataDefinition() {
        super();
    }

    /**
     * Constructor to populate name only
     */
    public SpeedPhasesOTZServicesDataDefinition(String name) {
        super(name);
    }

    public SpeedPhasesOTZServicesDataDefinition(String name, String otzServiceName) {
        super(name);
        this.otzServiceName = otzServiceName;
    }


    //***** INSTANCE METHODS *****


    public String getOtzServiceName() {
        return otzServiceName;
    }

    public void setOtzServiceName(String otzServiceName) {
        this.otzServiceName = otzServiceName;
    }

    /**
     * @see org.openmrs.module.reporting.data.DataDefinition#getDataType()
     */
    public Class<?> getDataType() {
        return Double.class;
    }
}
