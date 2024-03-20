package nl.fertilis.fertilis.netcdf.knmi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import nl.fertilis.fertilis.netcdf.io.DatasetFileHandler;
import nl.fertilis.fertilis.netcdf.io.DatasetFileHandler.SUPPORTED_DATASET_PROVIDER;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KnmiDownloadCron {
  private static final String KNMI_10_MIN_DATASET_NAME = "Actuele10mindataKNMIstations";
  private final KnmiDataService knmiDataService;
  private final DatasetFileHandler datasetFileHandler;

  public KnmiDownloadCron(KnmiDataService knmiDataService, DatasetFileHandler datasetFileHandler) {
    this.knmiDataService = knmiDataService;
    this.datasetFileHandler = datasetFileHandler;
  }

  @Scheduled(cron = "0 */1 * * * *")
  public void downloadKnmi10MinData() throws IOException {
    final KnmiDatasetDownloadResult downloadResult = knmiDataService.retrieveLatestDataset(KNMI_10_MIN_DATASET_NAME);

    datasetFileHandler.createNewDatasetFile(downloadResult.getFileName(), downloadResult.getBytes(), SUPPORTED_DATASET_PROVIDER.KNMI);
  }
}
