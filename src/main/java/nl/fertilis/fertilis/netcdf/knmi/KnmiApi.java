package nl.fertilis.fertilis.netcdf.knmi;

import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.relational.core.sql.In;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
public class KnmiApi {
  private RestClient restClient = RestClient.create();
  @Value("${knmi.baseurl}")
  private String knmiApiBaseUrl;

  @Value("${knmi.apikey}")
  private String apikey;

  public KnmiFilesModel getFiles(String dataset, String versionId) {
    try {
      return restClient.get()
                                             .uri(knmiApiBaseUrl + dataset + "/versions/" + versionId + "/files?sorting=desc&maxKeys=1")
                                             .header("Authorization", apikey)
                                             .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                                             .retrieve()
                                             .body(KnmiFilesModel.class);
    } catch (Exception exception) {
      log.error(exception.getMessage(), exception);
      return null;
    }
  }

  public KnmiFileUrlModel getTemporaryUrl(String dataset, String versionId, String fileName) {
    try {
      return restClient.get()
                                             .uri(knmiApiBaseUrl + dataset + "/versions/" + versionId + "/files/" + fileName + "/url")
                                             .header("Authorization", apikey)
                                             .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                                             .retrieve()
                                             .body(KnmiFileUrlModel.class);
    } catch (Exception exception) {
      log.error(exception.getMessage(), exception);
      return null;
    }
  }

  public byte[] downloadDataset(String url) {
    try {
      return restClient.get()
                       .uri(new URI(new String(url)))
          .header("accept", MediaType.ALL_VALUE)
                       .retrieve()
                       .body(byte[].class);
    } catch (Exception exception) {
      log.error(exception.getMessage(), exception);
      return null;
    }
  }
}
