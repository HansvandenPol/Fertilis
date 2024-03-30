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
import org.springframework.util.StringUtils;
import ucar.array.Array;
import ucar.array.Section;
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
  private final KnmiStationService knmiStationService;

  public KnmiDataService(KnmiApi knmiApi, DatasetFileHandler datasetFileHandler, KnmiStationService knmiStationService) {
    this.knmiApi = knmiApi;
    this.datasetFileHandler = datasetFileHandler;
    this.knmiStationService = knmiStationService;
  }

  public KnmiDatasetDownloadResult retrieveLatestDataset(String dataset) {
    final KnmiFileRequestModel requestModel = new KnmiFileRequestModel(dataset, DATASET_VERSION, FileRequestSortingOrder.DESC,
        MAX_REQUEST_SIZE);
    final KnmiFilesModel filesModel = knmiApi.getFiles(requestModel);
    final KnmiFileUrlModel urlModel = knmiApi.getTemporaryUrl(requestModel, filesModel.getFiles().getFirst().getFilename());

    return new KnmiDatasetDownloadResult(filesModel.getFiles().get(0).getFilename(),knmiApi.downloadDataset(urlModel.getTemporaryDownloadUrl()));
  }

  public Knmi10MinApiResponse retrieveAll10MinKnmiData() {
    final List<KnmiVariable> datasetList = new ArrayList<>();

    datasetList.add(KnmiVariable.STATION_ID);
    datasetList.add(KnmiVariable.STATION_NAME);
    datasetList.add(KnmiVariable.STATION_LAT);
    datasetList.add(KnmiVariable.STATION_LON);
    datasetList.add(KnmiVariable.RAINFALL_DURATION_1H);
    datasetList.add(KnmiVariable.WIND_DIRECTION_AVG_10MIN);
    datasetList.add(KnmiVariable.WIND_SPEED_10METER_AVG_10MIN);
    datasetList.add(KnmiVariable.RAINFALL_1H);
    datasetList.add(KnmiVariable.RAINFALL_6H);
    datasetList.add(KnmiVariable.RAINFALL_12H);
    datasetList.add(KnmiVariable.RAINFALL_24H);
    datasetList.add(KnmiVariable.SOIL_TEMP_5CM_AVG);
    datasetList.add(KnmiVariable.SOIL_TEMP_10CM_AVG);

    return createKnmiResponseDataKnmi10Min(datasetList);
  }

  public Knmi10MinApiResponse getQuickWeatherInformation(String latitude, String longitude) {
    if(!StringUtils.hasText(latitude) || !StringUtils.hasText(longitude)) {
      log.info("No geo information found");
    }

    final List<KnmiVariable> datasetList = new ArrayList<>();
    datasetList.add(KnmiVariable.STATION_NAME);
    datasetList.add(KnmiVariable.WIND_DIRECTION_AVG_10MIN);
    datasetList.add(KnmiVariable.RAINFALL_1H);
    datasetList.add(KnmiVariable.AMBIENT_TEMP_6H_MAX);
    datasetList.add(KnmiVariable.WIND_SPEED_1H_AVG);
    datasetList.add(KnmiVariable.TOTAL_CLOUD_COVER);

    final var stationIndex = knmiStationService.getStationIndexByCoordinates(latitude, longitude);
    return createKnmiResponseDataByStationId(datasetList, stationIndex);
  }

  private Knmi10MinApiResponse createKnmiResponseDataByStationId(List<KnmiVariable> datasetList, int stationId) {
    // retrieve latest file
    final Path location = datasetFileHandler.getLatestDatasetPath(SUPPORTED_DATASET_PROVIDER.KNMI);

    final List<Knmi10MinStatistic> knmi10MinStatistics = new ArrayList<>();
    final Knmi10MinApiResponse response = new Knmi10MinApiResponse(Instant.now(), knmi10MinStatistics);

    // extract array data
    try(NetcdfFile file = NetcdfFiles.open(location.toString())) {
      log.info("Getting file name: {}", location.getFileName());
      datasetList.forEach(i -> {
        final List<Knmi10MinStatisticData<?>> statisticDataList = new ArrayList<>();
        final Knmi10MinStatistic statistic = new Knmi10MinStatistic(i.variableName,i.description, statisticDataList);

        int[] origin;
        int[] size;

        try {
          if(i.variableName.equals("stationname")) {
            origin = new int[]{stationId};
            size = new int[]{1};
          } else {
            origin = new int[]{stationId, 0};
            size = new int[]{1, 1};
          }

          Variable v = file.findVariable(i.variableName);
          Array a = v.readArray(new Section(origin, size));
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

  private Knmi10MinApiResponse createKnmiResponseDataKnmi10Min(List<KnmiVariable> datasetList) {
    // retrieve latest file
    final Path location = datasetFileHandler.getLatestDatasetPath(SUPPORTED_DATASET_PROVIDER.KNMI);

    final List<Knmi10MinStatistic> knmi10MinStatistics = new ArrayList<>();
    final Knmi10MinApiResponse response = new Knmi10MinApiResponse(Instant.now(), knmi10MinStatistics);

    // extract array data
    try(NetcdfFile file = NetcdfFiles.open(location.toString())) {
      log.info("Getting file name: {}", location.getFileName());
      datasetList.forEach(i -> {
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
