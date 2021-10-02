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
public class SpeedPhasesOTZActivityAtEnrollmentDataDefinition extends BaseDataDefinition implements VisitDataDefinition {

    public static final long serialVersionUID = 1L;

    @ConfigurationProperty
    private String otzColumnName;

    /**
     * Default Constructor
     */
    public SpeedPhasesOTZActivityAtEnrollmentDataDefinition() {
        super();
    }

    /**
     * Constructor to populate name only
     */
    public SpeedPhasesOTZActivityAtEnrollmentDataDefinition(String name) {
        super(name);
    }

    public SpeedPhasesOTZActivityAtEnrollmentDataDefinition(String name, String otzColumnName) {
        super(name);
        this.otzColumnName = otzColumnName;
    }


    //***** INSTANCE METHODS *****


    public String getOtzColumnName() {
        return otzColumnName;
    }

    public void setOtzColumnName(String otzColumnName) {
        this.otzColumnName = otzColumnName;
    }

    /**
     * @see org.openmrs.module.reporting.data.DataDefinition#getDataType()
     */
    public Class<?> getDataType() {
        return String.class;
    }
}
