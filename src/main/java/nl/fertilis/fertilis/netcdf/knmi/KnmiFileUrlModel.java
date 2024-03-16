package nl.fertilis.fertilis.netcdf.knmi;

import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KnmiFileUrlModel {
  private String contentType;
  private Instant lastModified;
  private int size;
  private String temporaryDownloadUrl;
}
