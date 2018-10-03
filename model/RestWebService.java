package at.fhooe.studymate.model;


import com.squareup.okhttp.RequestBody;

import java.util.List;

import at.fhooe.studymate.entities.records.AnswerRecord;
import at.fhooe.studymate.entities.records.DvRecord;
import at.fhooe.studymate.entities.ExperimentInfo;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;

/**
 * Providing methods to interact with the StudyMate Web API
 */
public interface RestWebService {

  @GET("/api/experiments/{experiment_id}")
  Call<ExperimentInfo> getExperiment(@Path("experiment_id") String experimentId);

  @Headers("Content-Type: text/plain")
  @POST("/api/experiments/{experiment_id}/measures/{encoded_ivs}/flow")
  Call<Void> postFlowOfTask(@Path("experiment_id") String experimentId, @Path("encoded_ivs") String ivs, @Body String xml);

  @Headers("Content-Type: application/json")
  @POST("/api/experiments/{experiment_id}/log")
  Call<Void> postDvOfTask(@Path("experiment_id") String experimentId, @Body DvRecord dvs);

  @Headers("Content-Type: application/json")
  @POST("/api/experiments/{experiment_id}/answers")
  Call<Void> postAnswersOfQuestionnaire(@Path("experiment_id") String experimentId, @Body List<AnswerRecord> answers);

  @Multipart
  @POST("/api/experiments/{experiment_id}/measures/{conditions}/video")
  Call<Void> postVideo(@Path("experiment_id") String experimentId, @Path("conditions") String conditions,
                       @Part("video\"; filename=\"test.mp4\" ") RequestBody video);

  @GET("/api/experiments/{experiment_id}/measures/videolist")
  Call<List<String>> getVideoList(@Path("experiment_id") String experimentId);
}
