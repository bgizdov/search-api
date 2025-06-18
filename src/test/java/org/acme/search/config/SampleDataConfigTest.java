package org.acme.search.config;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class SampleDataConfigTest {

    @Inject
    SampleDataConfig sampleDataConfig;

    @Test
    void testDefaultConfiguration() {
        // Test that the configuration is loaded correctly
        assertNotNull(sampleDataConfig);
        assertEquals(SampleDataConfig.Mode.BASIC, sampleDataConfig.mode());
        // In test environment, records-per-type is set to 10 for faster tests
        assertTrue(sampleDataConfig.recordsPerType() > 0, "Records per type should be positive");
    }

    @Test
    void testModeEnum() {
        // Test that all enum values are available
        SampleDataConfig.Mode[] modes = SampleDataConfig.Mode.values();
        assertEquals(4, modes.length);
        
        assertTrue(java.util.Arrays.asList(modes).contains(SampleDataConfig.Mode.NONE));
        assertTrue(java.util.Arrays.asList(modes).contains(SampleDataConfig.Mode.BASIC));
        assertTrue(java.util.Arrays.asList(modes).contains(SampleDataConfig.Mode.PERFORMANCE_SMALL));
        assertTrue(java.util.Arrays.asList(modes).contains(SampleDataConfig.Mode.PERFORMANCE_LARGE));
    }
}
