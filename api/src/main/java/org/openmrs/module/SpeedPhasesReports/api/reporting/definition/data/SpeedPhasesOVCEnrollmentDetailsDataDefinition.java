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
public class SpeedPhasesOVCEnrollmentDetailsDataDefinition extends BaseDataDefinition implements VisitDataDefinition {

    public static final long serialVersionUID = 1L;

    @ConfigurationProperty
    private String ovcColumnName;

    /**
     * Default Constructor
     */
    public SpeedPhasesOVCEnrollmentDetailsDataDefinition() {
        super();
    }

    /**
     * Constructor to populate name only
     */
    public SpeedPhasesOVCEnrollmentDetailsDataDefinition(String name) {
        super(name);
    }

    public SpeedPhasesOVCEnrollmentDetailsDataDefinition(String name, String ovcColumnName) {
        super(name);
        this.ovcColumnName = ovcColumnName;
    }


    //***** INSTANCE METHODS *****


    public String getOvcColumnName() {
        return ovcColumnName;
    }

    public void setOvcColumnName(String ovcColumnName) {
        this.ovcColumnName = ovcColumnName;
    }

    /**
     * @see org.openmrs.module.reporting.data.DataDefinition#getDataType()
     */
    public Class<?> getDataType() {
        return String.class;
    }
}
