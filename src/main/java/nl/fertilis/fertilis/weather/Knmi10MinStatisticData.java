package nl.fertilis.fertilis.weather;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Knmi10MinStatisticData<T> {
  private int stationIndex;
  private T value;
}
