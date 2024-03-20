package nl.fertilis.fertilis.netcdf.processor;

import java.io.IOException;
import java.util.Iterator;
import lombok.extern.slf4j.Slf4j;
import nl.fertilis.fertilis.netcdf.knmi.KnmiDataService;
import nl.fertilis.fertilis.netcdf.knmi.KnmiDatasetDownloadResult;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import ucar.array.Array;
import ucar.array.InvalidRangeException;
import ucar.array.Section;
import ucar.nc2.NetcdfFile;
import ucar.nc2.NetcdfFiles;
import ucar.nc2.Variable;

@Service
@Slf4j
public class ProcessKnmiData {
  private final KnmiDataService knmiDataService;
  public ProcessKnmiData(KnmiDataService knmiDataService) {
    this.knmiDataService = knmiDataService;
  }

  public void processSoilTemperature() {
    final KnmiDatasetDownloadResult knmiDatasetDownloadResult = knmiDataService.retrieveLatestDataset("Actuele10mindataKNMIstations");

//    try(NetcdfFile file = NetcdfFiles.open(new ByteArrayResource(knmiDatasetDownloadResult.getBytes()).getFile().getPath())) {
//      Variable v = file.findVariable("tb1");
//      Array a = v.readArray();
//      Array b = v.readArray(new Section(new int[]{2,0}, new int[]{1,1}));
//      Iterator i = a.iterator();
//      while(i.hasNext()) {
//        System.out.println(i.next());
//      }
//
//    } catch (IOException e) {
//      log.error(e.getMessage(), e);
//    } catch (InvalidRangeException e) {
//      throw new RuntimeException(e);
//    }
  }
}
