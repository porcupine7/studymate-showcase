package at.fhooe.studymate.model;

import android.content.Context;
import android.content.pm.PackageManager;
import android.provider.Settings;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import at.fhooe.studymate.entities.ivs.App;
import at.fhooe.studymate.events.SetupEvent;
import at.fhooe.studymate.entities.ParticipantInfo;
import at.fhooe.studymate.interfaces.IDataManager;
import at.fhooe.studymate.interfaces.ISetupManager;

/**
 * Implementation of ISetupManager
 */
public class SetupManager implements ISetupManager {
    private final Context context;
    private final List<SetupEvent> expectedEvents;
    private final List<SetupEvent> actualEvents;
    private final ExperimentLoader expProvider;

    private String experimentUri;
    private final IDataManager dataManager;
    private ParticipantInfo participant;

    public SetupManager(Context context) {
        this.context = context;
        dataManager = Manager.INSTANCE.getDataProvider(context);
        expProvider = new ExperimentLoader(context, dataManager);
        expectedEvents = new ArrayList<>();
        expectedEvents.add(SetupEvent.QR_CODE_DONE);
        expectedEvents.add(SetupEvent.EXPERIMENT_DONE);
        expectedEvents.add(SetupEvent.PARTICIPANT_DONE);
        expectedEvents.add(SetupEvent.APP_DONE);
        actualEvents = new ArrayList<>();
    }

    @Override
    public void loadNext() {
        if (!actualEvents.contains(SetupEvent.QR_CODE_DONE)) {
            //Do nothing, another QR code has to be scanned!
        } else if (!actualEvents.contains(SetupEvent.EXPERIMENT_DONE)) {
            loadExperiment();
        } else if (!actualEvents.contains(SetupEvent.PARTICIPANT_DONE)) {
            loadParticipant();
        } else if (!actualEvents.contains(SetupEvent.APP_DONE)) {
            checkForApps();
        } else if (!actualEvents.contains(SetupEvent.ACCESSIBILITY_DONE)) {
            checkForAccServices();
        }
    }

    @Override
    public void parseQrCode(String qrCodeData) {
        if (qrCodeData == null || qrCodeData.isEmpty()) {
            EventBus.getDefault().post(SetupEvent.QR_CODE_FAIL);
            return;
        }

        String[] splitString = qrCodeData.split("\\?");
        if (splitString.length != 2) {
            EventBus.getDefault().post(SetupEvent.QR_CODE_FAIL);
            return;
        }
        experimentUri = splitString[0];
        dataManager.cacheBaseUrl(experimentUri);

        //Log.d("uptown", "QR: " + qrCodeData + "->" + splitString[1]);
        String[] queryParams = splitString[1].split("&");
        String participantId = "PXX";
        String blockId = "0";
        for (String param : queryParams) {
            String[] keyValuePair = param.split("=");
            if (keyValuePair[0].equals("par")) {
                participantId = keyValuePair[1];
            } else if (keyValuePair[0].equals("bl")) {
                blockId = keyValuePair[1];
            }
        }
        participant = new ParticipantInfo(participantId, Integer.valueOf(blockId));
        EventBus.getDefault().post(SetupEvent.QR_CODE_DONE);
    }

    @Override
    public void loadExperiment() {
        expProvider.requestExperiment(experimentUri);
        //TODO post setup events here
    }

    @Override
    public void loadParticipant() {
        if (participant != null) {
            dataManager.cacheParticipant(participant);
            EventBus.getDefault().post(SetupEvent.PARTICIPANT_DONE);
        } else {
            EventBus.getDefault().post(SetupEvent.PARTICIPANT_FAIL);
        }
    }

    @Override
    public void checkForApps() {
        List<App> requiredApps = dataManager.getExperiment().getApps();
        for (App app : requiredApps) {
            boolean appPackageInstalled = isAppPackageInstalled(app.getPackageName());
            if (!appPackageInstalled) {
                EventBus.getDefault().post(SetupEvent.APP_FAIL);
                return;
            }
        }
        EventBus.getDefault().post(SetupEvent.APP_DONE);
    }

    private boolean isAppPackageInstalled(String appPackage) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(appPackage, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @Override
    public void checkForAccServices() {
        //TODO not needed on new devices
        //try {
        //  String accFlag = Settings.Secure.ACCESSIBILITY_ENABLED;
        //  int accMagicNumber = Settings.Secure.getInt(context.getContentResolver(), accFlag);
        //  boolean accEnabled = accMagicNumber == 1;
        //  if (accEnabled) {
        //    return;
        //  }
        //} catch (Settings.SettingNotFoundException e) {
        //  e.printStackTrace();
        //}
        String settingValue = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
        boolean studyMateEnabled = settingValue.contains("at.fhooe.studymate");
        EventBus.getDefault().post(studyMateEnabled ?
                SetupEvent.ACCESSIBILITY_DONE : SetupEvent.ACCESSIBILITY_FAIL);
    }

    @Override
    public void addSetupEvent(SetupEvent setupEvent) {
        actualEvents.add(setupEvent);
    }

    @Override
    public boolean isSetupDone() {
        //Check if all expected events are contained in actual events
        for (SetupEvent curEvent : expectedEvents) {
            if (!actualEvents.contains(curEvent)) {
                return false;
            }
        }
        return true;
    }
}
