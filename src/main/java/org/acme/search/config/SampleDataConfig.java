package org.acme.search.config;

import io.smallrye.config.ConfigMapping;

/**
 * Configuration for sample data loading
 */
@ConfigMapping(prefix = "app.sample-data")
public interface SampleDataConfig {
    
    /**
     * Sample data loading mode
     */
    enum Mode {
        NONE,               // No sample data
        BASIC,              // Basic sample data (few records)
        PERFORMANCE_SMALL,  // Performance test data (10k records)
        PERFORMANCE_LARGE   // Performance test data (1M records)
    }
    
    /**
     * Sample data loading mode
     * @return the mode
     */
    Mode mode();
    
    /**
     * Number of records per entity type for performance modes
     * @return records per type
     */
    int recordsPerType();
}
