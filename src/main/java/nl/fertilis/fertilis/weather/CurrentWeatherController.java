package nl.fertilis.fertilis.weather;

import lombok.extern.slf4j.Slf4j;
import nl.fertilis.fertilis.netcdf.knmi.KnmiDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("weather")
@Slf4j
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
  public ResponseEntity<Knmi10MinApiResponse> getQuickStatsPast10Min(@RequestParam(value = "lat", required = false) String latitute, @RequestParam(value = "lon", required = false) String longitude) {
    if(latitute != null) log.info("latitude: {}", latitute);
    if(longitude != null) log.info("longitude: {}", longitude);

    return ResponseEntity.ok(knmiDataService.getQuickWeatherInformation(latitute, longitude));
  }
}
