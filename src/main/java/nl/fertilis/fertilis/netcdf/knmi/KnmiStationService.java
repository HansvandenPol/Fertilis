package nl.fertilis.fertilis.netcdf.knmi;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import nl.fertilis.fertilis.netcdf.io.DatasetFileHandler;
import nl.fertilis.fertilis.netcdf.io.DatasetFileHandler.SUPPORTED_DATASET_PROVIDER;
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
public class KnmiStationService {
  private static final String DEFAULT_STATION_NAME = "DE BILT AWS";

  private final DatasetFileHandler datasetFileHandler;

  public KnmiStationService(DatasetFileHandler datasetFileHandler) {
    this.datasetFileHandler = datasetFileHandler;
  }

  public int getStationIndexByCoordinates(String latitude, String longitude, Knmi10MinApiResponse apiResponse) {
    final List<KnmiVariable> datasetList = new ArrayList<>();
    datasetList.add(KnmiVariable.STATION_LAT);
    datasetList.add(KnmiVariable.STATION_LON);

    final Path location = datasetFileHandler.getLatestDatasetPath(SUPPORTED_DATASET_PROVIDER.KNMI);

    try(NetcdfFile file = NetcdfFiles.open(location.toString())) {
      log.info("Getting file name: {}", location.getFileName());
      datasetList.forEach(i -> {
          Variable v = file.findVariable(i.variableName);
          try {
            Array a = v.readArray();
            Iterator it = a.iterator();

            int counter = 0;

            while (it.hasNext()) {
              final String item = String.valueOf(it.next());
            }
          } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
          }
      });
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }

return 0;
  }
}
