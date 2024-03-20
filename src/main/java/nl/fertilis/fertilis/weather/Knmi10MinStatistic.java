package nl.fertilis.fertilis.weather;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Knmi10MinStatistic {
  private String name;
  private String description;
  private List<Knmi10MinStatisticData<?>> data;
}
