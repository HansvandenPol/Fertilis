package nl.fertilis.fertilis.netcdf.knmi;

import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KnmiFilesModel {
  private boolean isTruncated;
  private int resultCount;
  private List<KnmiFilesFile> files;
  private int maxResults;
  private String startAfterFilename;
  private String nextPageToken;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  static class KnmiFilesFile {
    private String filename;
    private int size;
    private Instant created;
    private Instant lastModified;
  }
}
