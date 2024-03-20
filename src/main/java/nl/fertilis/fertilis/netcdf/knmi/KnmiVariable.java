package nl.fertilis.fertilis.netcdf.knmi;

import nl.fertilis.fertilis.weather.Knmi10MinStatistic;

public enum KnmiVariable {
  STATION_ID ("station","Station id"),
  STATION_NAME ("stationname","Station name"),
  STATION_LAT ("lat","station  latitude"),
  STATION_LON ("lon","station  longitude"),
  RAINFALL_DURATION_1H ("D1H","Rainfall Duration in last Hour"),
  WIND_DIRECTION_AVG_10MIN ("dd","Wind Direction 10 Min Average with MD"),
  WIND_SPEED_10METER_AVG_10MIN ("ff","Wind Speed at 10m 10 Min Average with MD"),
  RAINFALL_12H ("R12H","Rainfall in last 12 Hours"),
  RAINFALL_1H ("R1H","Rainfall in last Hour"),
  RAINFALL_24H ("R24H","Rainfall in last 24 Hours"),
  RAINFALL_6H ("R6H","Rainfall in last 6 Hours"),
  SOIL_TEMP_5CM_AVG ("tb1","Soil Temperature -5cm 10 Min Average"),
  SOIL_TEMP_10CM_AVG ("tb2","Soil Temperature -10cm 10 Min Average");

  final String variableName;
  final String description;

  KnmiVariable(String  variableName, String description) {
    this.variableName = variableName;
    this.description = description;
  }
}
