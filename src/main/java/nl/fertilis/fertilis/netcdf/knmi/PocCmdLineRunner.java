package nl.fertilis.fertilis.netcdf.knmi;

import java.io.InputStream;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
public class PocCmdLineRunner implements CommandLineRunner {
  private final KnmiApi knmiApi;

  public PocCmdLineRunner(KnmiApi knmiApi) {
    this.knmiApi = knmiApi;
  }

  @Override
  public void run(String... args) throws Exception {
    final KnmiFilesModel model = knmiApi.getFiles("Actuele10mindataKNMIstations", "2");

    final KnmiFileUrlModel urlModel = knmiApi.getTemporaryUrl("Actuele10mindataKNMIstations", "2", model.getFiles().get(0).getFilename());

    final byte[] bytes = knmiApi.downloadDataset(urlModel.getTemporaryDownloadUrl());
    System.out.println(urlModel.getTemporaryDownloadUrl());
  }
}
