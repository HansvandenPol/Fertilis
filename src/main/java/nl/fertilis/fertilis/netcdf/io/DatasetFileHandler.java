package nl.fertilis.fertilis.netcdf.io;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DatasetFileHandler {

  private static final String USER_HOME_DIR = System.getProperty("user.home");
  private static final String FILE_SEPARATOR = System.getProperty("file.separator");

  @Value("${max.dataset.amount}")
  private int maxAmount;

  @Value("${file.directory}")
  private String baseDirName;

  public void createNewDatasetFile(String filename, byte[] bytes, SUPPORTED_DATASET_PROVIDER provider) throws IOException {
    final String datasetDir = getDatasetDirectory(provider);
    final Path dir = Paths.get(USER_HOME_DIR + FILE_SEPARATOR + baseDirName + FILE_SEPARATOR + datasetDir);
    Files.createDirectories(dir);

    removeOldestDataset(dir);

    final Path completePath = Paths.get(dir.toString(), filename);
    if (!Files.exists(completePath)) {
      try {
        Files.write(completePath, bytes);
      } catch (Exception exception) {
        log.error(exception.getMessage(), exception);
      }
    }
  }

  public Path getLatestDatasetPath(SUPPORTED_DATASET_PROVIDER provider) {
    final String datasetDir = getDatasetDirectory(provider);
    final Path dir = Paths.get(USER_HOME_DIR + FILE_SEPARATOR + baseDirName + FILE_SEPARATOR + datasetDir);
    List<Path> list = new ArrayList<>();

    try (final DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
      stream.iterator().forEachRemaining(list::add);
      return orderPathsByDate(list, false).getFirst();
    } catch (Exception exception) {
      log.error(exception.getMessage(), exception);
    }
    return null;
  }

  private void removeOldestDataset(Path dir) {
    List<Path> list = new ArrayList<>();
    try (final DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
      stream.iterator().forEachRemaining(list::add);

      if(list.size() >= maxAmount ) {
        list = orderPathsByDate(list, true);

        log.info("to delete: {}", list.get(0).toString());
        Files.delete(list.getFirst());
      }
    } catch (Exception exception) {
      log.error(exception.getMessage(), exception);
    }
  }

  private String getDatasetDirectory(SUPPORTED_DATASET_PROVIDER provider) {
    switch(provider) {
      case KNMI -> {
        return "knmi-data";
      }default -> {
        return "";
      }
    }
  }

  private List<Path> orderPathsByDate(List<Path> paths, boolean isAscending) {
    List<Path> ordered =  paths.stream().sorted((i, j) -> {

      try {
        Instant a = Files.getLastModifiedTime(i)
                         .toInstant();
        Instant b = Files.getLastModifiedTime(j)
                         .toInstant();
        return a.compareTo(b);
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
      return 0;
    }).toList();
    return isAscending ? ordered : ordered.reversed();
  }

  public enum SUPPORTED_DATASET_PROVIDER {
    KNMI;
  }
}
