package part2;

import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.DefaultApi;
import io.swagger.client.model.AlbumInfo;
import io.swagger.client.model.AlbumsProfile;
import io.swagger.client.model.ImageMetaData;
import java.io.File;

public class RequestHandler2 {

  private static final int MAX_REQUESTS = 5;

  public static ResponseData post(DefaultApi albumApi) {
    long startTime = System.currentTimeMillis();
    int curr = 0;
    AlbumsProfile profile = new AlbumsProfile();
    profile.setArtist("string");
    profile.setTitle("string");
    profile.setYear("string");

    File image = new File("nmtb.png");

    while (curr < MAX_REQUESTS) {
      try {
        ApiResponse<ImageMetaData> data = albumApi.newAlbumWithHttpInfo(image, profile);
        long endTime = System.currentTimeMillis();
        long latency = endTime - startTime;
        return new ResponseData(startTime, "POST", latency, data.getStatusCode());
      } catch (ApiException e) {
        if (e.getCode() >= 400 && e.getCode() < 600) {
          curr++;
        } else {
          break;
        }
      }
    }
    if (curr >= MAX_REQUESTS) {
      System.err.println("Unable to post to server");
    }
    return null;
  }

  public static ResponseData get(DefaultApi albumApi) {
    long startTime = System.currentTimeMillis();
    int curr = 0;
    while (curr < MAX_REQUESTS) {
      try {
        ApiResponse<AlbumInfo> album = albumApi.getAlbumByKeyWithHttpInfo("1");
        long endTime = System.currentTimeMillis();
        long latency = endTime - startTime;
        //success++
        return new ResponseData(startTime, "GET", latency, album.getStatusCode());
      } catch (ApiException e) {
        if (e.getCode() >= 400 && e.getCode() < 600) {
          curr++;
        } else {
          break;
        }
      }
    }
    if (curr >= MAX_REQUESTS) {
      System.err.println("Unable to get from server");
      //failed++;
    }
    return null;
  }
}
