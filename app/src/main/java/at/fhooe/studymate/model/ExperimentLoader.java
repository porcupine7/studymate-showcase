package at.fhooe.studymate.model;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import at.fhooe.studymate.events.SetupEvent;
import at.fhooe.studymate.entities.ExperimentInfo;
import at.fhooe.studymate.interfaces.IDataManager;
import at.fhooe.studymate.interfaces.IExperimentLoader;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Implementation of IExperimentLoader
 */
public class ExperimentLoader implements IExperimentLoader {
    public static final String PREFIX_ASSETS = "assets/";

    private final Context context;
    private ExperimentInfo experimentInfo;

    private final IDataManager dataManager;
    public static final String API_NAME = "/api/experiments";

    ExperimentLoader(Context context, IDataManager dataManager) {
        this.context = context;
        this.dataManager = dataManager;
    }

    @Override
    public void requestExperiment(final String uri) {
        AsyncTask<Void, Void, Boolean> asyncTask = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return loadExperiment(uri);
            }

            @Override
            protected void onPostExecute(Boolean loaded) {
                super.onPostExecute(loaded);
                if (loaded) {
                    dataManager.cacheExperiment(experimentInfo.asJson());
                    EventBus.getDefault().post(SetupEvent.EXPERIMENT_DONE);
                } else {
                    EventBus.getDefault().post(SetupEvent.EXPERIMENT_FAIL);
                }
            }
        };
        asyncTask.execute();
    }

    private boolean loadExperiment(String uri) {
        if (uri.startsWith(PREFIX_ASSETS)) {
            String fileName = uri.replaceAll(PREFIX_ASSETS, "");
            return loadAssets(fileName);
        } else {
            return loadFromServer(uri);
        }
    }

    private boolean loadAssets(String assetFileName) {
        try {
            InputStream inputStream = context.getAssets().open(assetFileName);
            final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            experimentInfo = new Gson().fromJson(reader, ExperimentInfo.class);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean loadFromServer(String url) {
        int splitDex = url.indexOf(API_NAME);
        String baseUrl = url.substring(0, splitDex);
        String expId = url.substring(splitDex + API_NAME.length() + 1);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build();
        RestWebService expService = retrofit.create(RestWebService.class);
        Call<ExperimentInfo> experimentCall = expService.getExperiment(expId);
        try {
            Response<ExperimentInfo> response = experimentCall.execute();
            experimentInfo = response.body();
            return experimentInfo != null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
