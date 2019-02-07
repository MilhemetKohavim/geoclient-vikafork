package gov.nyc.doitt.gis.geoclient.test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.event.Level;

public interface IntegrationTestSupport {

    default String getJavaLibraryPath() {
        return System.getProperty("java.library.path");
    }

    default String getJavaIoTmpdir() {
        return System.getProperty("java.io.tmpdir");
    }

    default boolean contains(File target) {
        List<String> paths = Arrays.asList(System.getProperty("PATH").split(File.separator));
        for (String string : paths) {
            Path path = Paths.get(string);
            if (path.toFile().equals(target)) {
                return true;
            }
        }
        return false;
    }

    default List<File> filesFromPathString(String key) {
        if (key == null) {
            throw new NullPointerException("Path string argument cannot be null");
        }
        List<File> result = new ArrayList<>();
        String[] paths = key.split(File.pathSeparator);
        for (int i = 0; i < paths.length; i++) {
            result.add(new File(paths[i]));
        }
        return result;
    }

    default File getEnvVarAsFile(String name) {
        return new File(System.getenv(name));
    }

    default void logEnvironment(final Level level, final Logger logger) {
        LogLevelAdapter.logAll(level, logger, Collections.synchronizedSortedMap(new TreeMap<>(System.getenv())));
    }

    default void logSystemProperties(final Level level, final Logger logger) {
        LogLevelAdapter.logAll(level, logger, Collections.synchronizedSortedMap(new TreeMap<>(System.getProperties())));
    }
}
