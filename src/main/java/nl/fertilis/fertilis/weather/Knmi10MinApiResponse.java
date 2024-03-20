package nl.fertilis.fertilis.weather;

import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Knmi10MinApiResponse {
  private Instant lastModified;
  private List<Knmi10MinStatistic> statistics;
}
