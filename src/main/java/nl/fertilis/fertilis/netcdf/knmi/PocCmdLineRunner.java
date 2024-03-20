package nl.fertilis.fertilis.netcdf.knmi;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import nl.fertilis.fertilis.netcdf.processor.ProcessKnmiData;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
public class PocCmdLineRunner implements CommandLineRunner {
  private final ProcessKnmiData knmiDataService;

  public PocCmdLineRunner(ProcessKnmiData knmiDataService) {
    this.knmiDataService = knmiDataService;
  }

  @Override
  public void run(String... args) throws Exception {
    System.getProperty("user.home");
    String fileSeparator = System.getProperty("file.separator");

//    File f = new File(System.getProperty("user.home") + fileSeparator + ".fertilis" + fileSeparator + "knmi");
//    f.mkdirs();
//    f.createNewFile();

    Path p = Paths.get(System.getProperty("user.home") + fileSeparator + ".fertilis");
    Files.createDirectories(p);
    Files.write(Paths.get(p.toString(), "knmi.txt"), new byte[]{});
    knmiDataService.processSoilTemperature();
  }
}
