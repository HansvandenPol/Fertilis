package nl.fertilis.fertilis.netcdf.knmi;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import nl.fertilis.fertilis.netcdf.io.DatasetFileHandler;
import nl.fertilis.fertilis.netcdf.io.DatasetFileHandler.SUPPORTED_DATASET_PROVIDER;
import nl.fertilis.fertilis.weather.Knmi10MinApiResponse;
import nl.fertilis.fertilis.weather.Knmi10MinStatistic;
import nl.fertilis.fertilis.weather.Knmi10MinStatisticData;
import org.springframework.data.relational.core.sql.In;
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

  public int getStationIndexByCoordinates(String latitude, String longitude) {
    final Map<Integer, String[]> coordinates = getKnmiStationCoords();
    var clostedStationIndex = -1;
    var clostedDistance = Double.MAX_VALUE;

    Set<Entry<Integer, String[]>> entrySet = coordinates.entrySet();
    for(var entry : entrySet) {
      var distance = getDistanceCoordinates(latitude, longitude, entry.getValue()[0], entry.getValue()[1]);
      if(distance < clostedDistance) {
        clostedStationIndex = entry.getKey();
        clostedDistance = distance;
      }
    }

    return clostedStationIndex;
  }

  private Map<Integer, String[]> getKnmiStationCoords() {
    final List<KnmiVariable> datasetList = new ArrayList<>();
    final Map<Integer, String[]> coordinates = new HashMap<>();
    datasetList.add(KnmiVariable.STATION_LAT);
    datasetList.add(KnmiVariable.STATION_LON);
    final Path location = datasetFileHandler.getLatestDatasetPath(SUPPORTED_DATASET_PROVIDER.KNMI);


    try(NetcdfFile file = NetcdfFiles.open(location.toString())) {
      log.info("Getting file name: {}", location.getFileName());

      var latituteValues = datasetList.get(0);
      var longitudeValues = datasetList.get(1);

      Variable latVariable = file.findVariable(latituteValues.variableName);
      Variable lonVariable = file.findVariable(longitudeValues.variableName);

      try {
        Array latArray = latVariable.readArray();
        Array lonArray = lonVariable.readArray();
        Iterator latIterator = latArray.iterator();
        Iterator lonIterator = lonArray.iterator();

        var counter = 0;
        while (latIterator.hasNext() && lonIterator.hasNext()) {
          final String lat = String.valueOf(latIterator.next());
          final String lon = String.valueOf(lonIterator.next());
          coordinates.put(counter++, new String[]{lat, lon});
        }
      } catch (Exception exception) {
        log.error(exception.getMessage(), exception);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return coordinates;
  }


  private double getDistanceCoordinates(String targetLat, String targetLon, String currentLat, String currentLon) {
    var targetLatValue = Double.valueOf(targetLat);
    var targetLonValue = Double.valueOf(targetLon);
    var currentLatValue = Double.valueOf(currentLat);
    var currentLonValue = Double.valueOf(currentLon);

    var differenceLat = targetLatValue - currentLatValue;
    var differenceLong = targetLonValue - currentLonValue;

    return Math.sqrt((Math.pow(differenceLat, 2)) + Math.sqrt(Math.pow(differenceLong,2)));
  }
}
