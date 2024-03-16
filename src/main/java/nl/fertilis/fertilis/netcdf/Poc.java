package nl.fertilis.fertilis.netcdf;

import java.io.IOException;
import java.util.Iterator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import ucar.array.Array;
import ucar.array.Arrays;
import ucar.array.InvalidRangeException;
import ucar.array.Section;
import ucar.nc2.NetcdfFile;
import ucar.nc2.NetcdfFiles;
import ucar.nc2.Variable;

@Slf4j
public class Poc {

  public static void main(String[] args) {
    poc();
  }
  public static void poc() {
    try(NetcdfFile file = NetcdfFiles.open(new ClassPathResource("test.nc").getFile().getPath())) {
      Variable v = file.findVariable("Tx12");
      Array a = v.readArray();
      Array b = v.readArray(new Section(new int[]{2,0}, new int[]{1,1}));
      Iterator i = a.iterator();
      while(i.hasNext()) {
        System.out.println(i.next());
      }

    } catch (IOException e) {
      log.error(e.getMessage(), e);
    } catch (InvalidRangeException e) {
      throw new RuntimeException(e);
    }
  }

}
