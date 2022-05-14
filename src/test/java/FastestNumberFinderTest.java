import demo.CustomNumberEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FastestNumberFinderTest {
    private Logger logger = LoggerFactory.getLogger(FastestNumberFinderTest.class);
    private FastestNumberFinder fastestNumberFinder;

    @BeforeEach
    public void initialize() {
        fastestNumberFinder = new FastestNumberFinder();
    }

    @Test
    @DisplayName("json will be parsed to a list from relative resource path")
    public void readJsonFileFromResourceDirShouldBeSuccessful() {
        final List<CustomNumberEntity> entities = fastestNumberFinder.readFromFile("src/test/resources/test-scenario_1.json");
        Assertions.assertFalse(entities.isEmpty());
    }

    @Test
    @DisplayName("json will be parsed to a list from absolute resource path")
    public void readJsonFileFromAbsolutePathShouldBeSuccessful() {
        Path absPath = Paths.get("src/test/resources/test-scenario_1.json").toAbsolutePath();
        logger.debug("Found File {} in {}", "src/test/resources/test-scenario_1.json", absPath.toString());
        final List<CustomNumberEntity> entities = fastestNumberFinder.readFromFile(absPath.toString());
        Assertions.assertFalse(entities.isEmpty());
    }

    @ParameterizedTest(name = "{index}:->{0} found in json.")
    @ValueSource(ints = {67, 45, 45, -3, 12, 100, 3})
    @DisplayName("provided value must be found in json file")
    public void validateValueFoundInProvidedJsonFile(Integer valueToBeFound) {
        List<CustomNumberEntity> entities = fastestNumberFinder
                .readFromFile("src/test/resources/test-scenario_1.json");
        boolean isFound = fastestNumberFinder.contains(valueToBeFound, entities);
        Assertions.assertTrue(isFound);
    }

    @ParameterizedTest(name = "{index}:->{0} not found in json.")
    @ValueSource(ints = {9, 99, 999, 9999, 99999})
    @DisplayName("provided value must not be found in json file")
    public void validateValueNotFoundInProvidedJsonFile(Integer valueNotToBeFound) {
        List<CustomNumberEntity> entities = fastestNumberFinder
                .readFromFile("src/test/resources/test-scenario_1.json");
        boolean isFound = fastestNumberFinder.contains(valueNotToBeFound, entities);
        Assertions.assertFalse(isFound);
    }
}