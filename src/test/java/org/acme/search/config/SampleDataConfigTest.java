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
        // Test that the default configuration is loaded correctly
        assertNotNull(sampleDataConfig);
        assertEquals(SampleDataConfig.Mode.BASIC, sampleDataConfig.mode());
        assertEquals(2500, sampleDataConfig.recordsPerType());
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
