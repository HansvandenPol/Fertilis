package nl.fertilis.fertilis.netcdf.knmi;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KnmiFileRequestModel {
  private String dataset;
  private String version;
  private FileRequestSortingOrder order;
  private int maxRequestSize;

  enum FileRequestSortingOrder {
    ASC, DESC;

    public String toLower() {
      return this.name().toLowerCase();
    }
  }
}
