import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import demo.CustomNumberEntity;
import demo.FastestComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FastestNumberFinder implements NumberFinder {
    private static final Logger logger = LoggerFactory.getLogger(FastestNumberFinder.class);
    private final Gson reader = new Gson();
    private final Type jsonJavaType = new TypeToken<List<CustomNumberEntity>>() {}.getType();

    @Override
    public boolean contains(int valueToFind, List<CustomNumberEntity> list) {
        FastestComparator fastestComparator = new FastestComparator();
        if(!list.isEmpty()){
            return list
                    .parallelStream()
                    .filter(entity->fastestComparator.compare(valueToFind,entity)==0)
                    .findAny()
                    .isPresent();
        }
        return false;
    }

    @Override
    public List<CustomNumberEntity> readFromFile(String filePath) {
        try {
            String fileContent = Files.lines(Paths.get(filePath)).collect(Collectors.joining(System.lineSeparator()));
            return reader.fromJson(fileContent, jsonJavaType);
        } catch (IOException exception) {
            logger.error("unable to read file due to {}", exception.getMessage(), exception);
        } catch (Exception exception) {
            logger.error("generic read file due to {}", exception.getMessage(), exception);
        }
        return Collections.emptyList();
    }
}