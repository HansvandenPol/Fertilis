package nl.fertilis.fertilis.netcdf.knmi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class KnmiDatasetDownloadResult {
  private String fileName;
  private byte[] bytes;
}
