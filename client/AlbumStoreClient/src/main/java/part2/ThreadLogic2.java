package part2;

import io.swagger.client.ApiClient;
import io.swagger.client.Configuration;
import io.swagger.client.api.DefaultApi;
import io.swagger.client.api.LikeApi;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadLogic2 implements Runnable {

  private final String path;
  private final int numRequests;
  private final CountDownLatch completed;

  private final ConcurrentLinkedDeque<ResponseData> data;

  private final AtomicInteger success;
  private final AtomicInteger failed;

  public ThreadLogic2(String path, int numRequests, CountDownLatch completed,
      ConcurrentLinkedDeque<ResponseData> data, AtomicInteger success, AtomicInteger failed) {
    this.path = path;
    this.numRequests = numRequests;
    this.completed = completed;
    this.data = data;
    this.success = success;
    this.failed = failed;
  }

  @Override
  public void run() {
    ApiClient apiClient = Configuration.getDefaultApiClient();
    apiClient.setBasePath(this.path);
    DefaultApi albumsApi = new DefaultApi(apiClient);
    LikeApi likeApi = new LikeApi(apiClient);

    for (int j = 0; j < numRequests; j++) {

      // Post album
      ResponseData responsePost = RequestHandler2.postAlbum(albumsApi);
      if (this.data != null && responsePost != null) {
        data.add(responsePost);
        if (success != null) {
          success.incrementAndGet();
        }
      } else {
        if (failed != null) {
          failed.incrementAndGet();
        }
      }

      // Post 2 likes for album
      for (int i = 0; i < 2; i++) {
        ResponseData responsePostLike = RequestHandler2.postLike(likeApi);
        if (this.data != null && responsePostLike != null) {
          data.add(responsePostLike);
          if (success != null) {
            success.incrementAndGet();
          }
        } else {
          if (failed != null) {
            failed.incrementAndGet();
          }
        }
      }

      // Post dislike to album
      ResponseData responsePostDislike = RequestHandler2.postDislike(likeApi);
      if (this.data != null && responsePostDislike != null) {
        data.add(responsePostDislike);
        if (success != null) {
          success.incrementAndGet();
        }
      } else {
        if (failed != null) {
          failed.incrementAndGet();
        }
      }

//      ResponseData responseGet = RequestHandler2.getAlbum(albumsApi);
//      if (this.data != null && responseGet != null) {
//        data.add(responseGet);
//        if (success != null) {
//          success.incrementAndGet();
//        }
//      } else {
//        if (failed != null) {
//          failed.incrementAndGet();
//        }
//      }
    }

    System.out.println(Thread.currentThread().getName() + " has finished");
    this.completed.countDown();
  }
}
