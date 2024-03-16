//package nl.fertilis.fertilis.netcdf.io;
//
//import java.io.InputStream;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.client.RestClient;
//
//public class HttpFileRetriever {
//  final RestClient restClient = RestClient.create();
//
//  @Value("${knmi.apikey}")
//  private String knmiApikey;
//
//  public InputStream downloadFile() {
//    restClient.get()
//  }
//
//}
