package org.acme.search.service;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.acme.search.config.SampleDataConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test to verify that performance tests are disabled by default
 */
@QuarkusTest
class PerformanceTestDisabledTest {

    @Inject
    SampleDataConfig sampleDataConfig;

    @Test
    void testPerformanceTestsDisabledByDefault() {
        // Verify that the default mode is BASIC, not a performance mode
        assertEquals(SampleDataConfig.Mode.BASIC, sampleDataConfig.mode(), 
            "Performance tests should be disabled by default - mode should be BASIC");
        
        // Verify it's not a performance mode
        assertNotEquals(SampleDataConfig.Mode.PERFORMANCE_SMALL, sampleDataConfig.mode(),
            "Default mode should not be PERFORMANCE_SMALL");
        assertNotEquals(SampleDataConfig.Mode.PERFORMANCE_LARGE, sampleDataConfig.mode(),
            "Default mode should not be PERFORMANCE_LARGE");
    }

    @Test
    void testRecordsPerTypeIsReasonableForTests() {
        // In test environment, records per type should be small for fast tests
        int recordsPerType = sampleDataConfig.recordsPerType();
        assertTrue(recordsPerType > 0, "Records per type should be positive");
        assertTrue(recordsPerType <= 100, "Records per type should be small in test environment for fast execution");
    }

    @Test
    void testAllModesAvailable() {
        // Verify all modes are available for users who want to enable performance tests
        SampleDataConfig.Mode[] modes = SampleDataConfig.Mode.values();
        assertEquals(4, modes.length, "Should have 4 sample data modes available");
        
        // Verify performance modes exist but are not default
        boolean hasPerformanceSmall = false;
        boolean hasPerformanceLarge = false;
        
        for (SampleDataConfig.Mode mode : modes) {
            if (mode == SampleDataConfig.Mode.PERFORMANCE_SMALL) {
                hasPerformanceSmall = true;
            }
            if (mode == SampleDataConfig.Mode.PERFORMANCE_LARGE) {
                hasPerformanceLarge = true;
            }
        }
        
        assertTrue(hasPerformanceSmall, "PERFORMANCE_SMALL mode should be available");
        assertTrue(hasPerformanceLarge, "PERFORMANCE_LARGE mode should be available");
    }
}
