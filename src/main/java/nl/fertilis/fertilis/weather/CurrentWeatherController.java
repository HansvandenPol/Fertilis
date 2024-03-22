package nl.fertilis.fertilis.weather;

import nl.fertilis.fertilis.netcdf.knmi.KnmiDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("weather")
public class CurrentWeatherController {
  private final KnmiDataService knmiDataService;

  public CurrentWeatherController(KnmiDataService knmiDataService) {
    this.knmiDataService = knmiDataService;
  }

  @GetMapping("knmi/all")
  public ResponseEntity<Knmi10MinApiResponse> getAllStatsPast10Min() {
    return ResponseEntity.ok(knmiDataService.retrieveAll10MinKnmiData());
  }

  @GetMapping("knmi/quick")
  public ResponseEntity<Knmi10MinApiResponse> getQuickStatsPast10Min() {
    return ResponseEntity.ok(knmiDataService.getQuickWeatherInformation());
  }
}
