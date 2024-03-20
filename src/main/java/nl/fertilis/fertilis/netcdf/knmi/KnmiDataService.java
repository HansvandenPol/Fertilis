package nl.fertilis.fertilis.netcdf.knmi;

import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import nl.fertilis.fertilis.netcdf.io.DatasetFileHandler;
import nl.fertilis.fertilis.netcdf.io.DatasetFileHandler.SUPPORTED_DATASET_PROVIDER;
import nl.fertilis.fertilis.netcdf.knmi.KnmiFileRequestModel.FileRequestSortingOrder;
import nl.fertilis.fertilis.weather.Knmi10MinApiResponse;
import nl.fertilis.fertilis.weather.Knmi10MinStatistic;
import nl.fertilis.fertilis.weather.Knmi10MinStatisticData;
import org.springframework.stereotype.Service;
import ucar.array.Array;
import ucar.nc2.NetcdfFile;
import ucar.nc2.NetcdfFiles;
import ucar.nc2.Variable;

@Service
@Slf4j
public class KnmiDataService {

  private static final String DATASET_VERSION = "2";
  private static final int MAX_REQUEST_SIZE = 1;
  private final KnmiApi knmiApi;
  private final DatasetFileHandler datasetFileHandler;

  private static final List<KnmiVariable> DATASET_VARIABLE_VALUES = new ArrayList<>();

  static {
    DATASET_VARIABLE_VALUES.add(KnmiVariable.STATION_ID);
    DATASET_VARIABLE_VALUES.add(KnmiVariable.STATION_NAME);
    DATASET_VARIABLE_VALUES.add(KnmiVariable.STATION_LAT);
    DATASET_VARIABLE_VALUES.add(KnmiVariable.STATION_LON);
    DATASET_VARIABLE_VALUES.add(KnmiVariable.RAINFALL_DURATION_1H);
    DATASET_VARIABLE_VALUES.add(KnmiVariable.WIND_DIRECTION_AVG_10MIN);
    DATASET_VARIABLE_VALUES.add(KnmiVariable.WIND_SPEED_10METER_AVG_10MIN);
    DATASET_VARIABLE_VALUES.add(KnmiVariable.RAINFALL_1H);
    DATASET_VARIABLE_VALUES.add(KnmiVariable.RAINFALL_6H);
    DATASET_VARIABLE_VALUES.add(KnmiVariable.RAINFALL_12H);
    DATASET_VARIABLE_VALUES.add(KnmiVariable.RAINFALL_24H);
    DATASET_VARIABLE_VALUES.add(KnmiVariable.SOIL_TEMP_5CM_AVG);
    DATASET_VARIABLE_VALUES.add(KnmiVariable.SOIL_TEMP_10CM_AVG);
  }

  public KnmiDataService(KnmiApi knmiApi, DatasetFileHandler datasetFileHandler) {
    this.knmiApi = knmiApi;
    this.datasetFileHandler = datasetFileHandler;
  }

  public KnmiDatasetDownloadResult retrieveLatestDataset(String dataset) {
    final KnmiFileRequestModel requestModel = new KnmiFileRequestModel(dataset, DATASET_VERSION, FileRequestSortingOrder.DESC,
        MAX_REQUEST_SIZE);
    final KnmiFilesModel filesModel = knmiApi.getFiles(requestModel);
    final KnmiFileUrlModel urlModel = knmiApi.getTemporaryUrl(requestModel, filesModel.getFiles().getFirst().getFilename());

    return new KnmiDatasetDownloadResult(filesModel.getFiles().get(0).getFilename(),knmiApi.downloadDataset(urlModel.getTemporaryDownloadUrl()));
  }

  public Knmi10MinApiResponse retrieveAll10MinKnmiData() {
    // retrieve latest file
    final Path location = datasetFileHandler.getLatestDatasetPath(SUPPORTED_DATASET_PROVIDER.KNMI);

    final List<Knmi10MinStatistic> knmi10MinStatistics = new ArrayList<>();
    final Knmi10MinApiResponse response = new Knmi10MinApiResponse(Instant.now(), knmi10MinStatistics);


    // extract array data
    try(NetcdfFile file = NetcdfFiles.open(location.toString())) {
      log.info("Getting file name: {}", location.getFileName());
      DATASET_VARIABLE_VALUES.forEach(i -> {
        final List<Knmi10MinStatisticData<?>> statisticDataList = new ArrayList<>();
        final Knmi10MinStatistic statistic = new Knmi10MinStatistic(i.variableName,i.description, statisticDataList);

        try {
          Variable v = file.findVariable(i.variableName);
          Array a = v.readArray();
          Iterator it = a.iterator();

          int counter = 0;

          while (it.hasNext()) {
            final String item = String.valueOf(it.next());
            statisticDataList.add(new Knmi10MinStatisticData<>(counter++, item));
          }

          knmi10MinStatistics.add(statistic);
        } catch (Exception exception) {
          log.info(exception.getMessage(), exception);
        }
      });
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return response;
  }
}
