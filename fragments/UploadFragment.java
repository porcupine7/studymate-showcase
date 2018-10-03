package at.fhooe.studymate.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import at.fhooe.studymate.R;
import at.fhooe.studymate.logging.manager.VideoRecordingManager;
import at.fhooe.studymate.logging.manager.persistence.TaskFlowLocalPersistenceManager;
import at.fhooe.studymate.model.ExperimentLoader;
import at.fhooe.studymate.model.RestWebService;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Fragment showing consent message enabling the participant to accept or decline.
 */
public class UploadFragment extends Fragment {
  public static final String API_ENDING = "/measures/videolist";
  private static final String EXTRA_API_URL = "api_url";

  @Bind(R.id.txt_cur_video_count)
  TextView videoCountTxt;

  @Bind(R.id.txt_already)
  TextView alreadyTxt;

  @Bind(R.id.txt_pending)
  TextView pendingTxt;

  @Bind(R.id.progressBar)
  ProgressBar progressBar;

  private RestWebService webService;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.unbind(this);
    ButterKnife.bind(this, view);

    String apiUrl = getArguments().getString(EXTRA_API_URL, "");
    if (apiUrl.isEmpty()) {
      Toast.makeText(getActivity(), "No API url for Upload! Invalid State!", Toast.LENGTH_SHORT).show();
      return;
    }

    uploadVideos(apiUrl);

  }

  private void uploadVideos(String apiUrl) {
    //Parse API Url
    apiUrl = apiUrl.replace(API_ENDING, "");
    int splitDex = apiUrl.indexOf(ExperimentLoader.API_NAME);
    String baseUrl = apiUrl.substring(0, splitDex);
    final String expId = apiUrl.substring(splitDex + ExperimentLoader.API_NAME.length() + 1);

    //Check local files
    File expFolder = TaskFlowLocalPersistenceManager.getExpFolder(getActivity(), expId);
    File videosFolder = new File(expFolder, VideoRecordingManager.VIDEOS_FOLDER);
    final File[] videoFiles = videosFolder.listFiles();
    if (videoFiles == null || videoFiles.length == 0) {
      videoCountTxt.setText(String.format(getString(R.string.upload_cur_state), 0));
      Toast.makeText(getActivity(), "No video files on this device!", Toast.LENGTH_SHORT).show();
      return;
    }
    videoCountTxt.setText(String.format(getString(R.string.upload_cur_state), videoFiles.length));

    //Create Web service
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build();
    webService = retrofit.create(RestWebService.class);


    webService.getVideoList(expId).enqueue(new Callback<List<String>>() {
      @Override
      public void onResponse(Response<List<String>> response, Retrofit retrofit) {
        List<String> videosOnServer = response.body();
        if (videosOnServer == null) {
          Log.e("uptown", "NULLLL");
          return;
        }
        List<File> videosToUpload = new ArrayList<>();
        for (File localVideo : videoFiles) {
          if (!videosOnServer.contains(localVideo.getName())) {
            videosToUpload.add(localVideo);
          }
        }
        Log.d("uptown", "To Upload: " + videosToUpload);
        int alreadyUploaded = videoFiles.length - videosToUpload.size();
        alreadyTxt.setText(String.format(getString(R.string.upload_already), alreadyUploaded));
        pendingTxt.setText(String.format(getString(R.string.upload_pending), videosToUpload.size()));
        if (videosToUpload.size() == 0) {
          progressBar.setVisibility(View.INVISIBLE);
          return;
        }
        for (File fileToUpload : videosToUpload) {
          postVideo(expId, fileToUpload, videosToUpload.size());
        }
      }

      @Override
      public void onFailure(Throwable t) {
        Log.d("uptown", "Failed: " + t);
      }
    });
  }

  private int uploadCount;

  private void postVideo(String expId, File videoFile, final int totalUploadCount) {
    String conditionString = videoFile.getName().replace("_video.mp4", "");
    final RequestBody requestBody = RequestBody.create(MediaType.parse("video/mp4"), videoFile);
    webService.postVideo(expId, conditionString, requestBody).enqueue(new Callback<Void>() {
      @Override
      public void onResponse(Response<Void> response, Retrofit retrofit) {
        Log.d("uptown", "Video done: " + response.message());
        uploadCount++;
        if (response.code() != 204){
          Toast.makeText(getActivity(), "Error uploading one video file!", Toast.LENGTH_SHORT).show();
        }
        if (uploadCount == totalUploadCount) {
          progressBar.setVisibility(View.INVISIBLE);
          pendingTxt.setText(R.string.upload_done);
        }
      }

      @Override
      public void onFailure(Throwable t) {
        Log.d("uptown", "Video failed: " + t.getMessage());
        uploadCount++;
        Toast.makeText(getActivity(), "Error uploading one video file!", Toast.LENGTH_SHORT).show();
      }
    });
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_video_upload, container, false);
  }

  @OnClick(R.id.btn_return)
  void returnToQrScan(View view) {
    getActivity().finish();
  }

  public static UploadFragment newInstance(String apiUrl) {
    Bundle args = new Bundle();
    args.putString(UploadFragment.EXTRA_API_URL, apiUrl);
    UploadFragment fragment = new UploadFragment();
    fragment.setArguments(args);
    return fragment;
  }
}
