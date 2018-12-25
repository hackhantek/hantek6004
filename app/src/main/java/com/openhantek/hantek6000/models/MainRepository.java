package com.openhantek.hantek6000.models;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import com.hantek.ht6000api.Channel;
import com.hantek.ht6000api.HantekDeviceListener;
import com.hantek.ht6000api.HantekSdk;
import com.hantek.ht6000api.HtScopeSettings;
import com.hantek.ht6000api.MathChannel;
import com.hantek.ht6000api.ht6000.AttenuationFactor;
import com.hantek.ht6000api.ht6000.AutomeasureType;
import com.hantek.ht6000api.ht6000.CaptureMode;
import com.hantek.ht6000api.ht6000.InputCoupling;
import com.hantek.ht6000api.ht6000.TimeBase;
import com.hantek.ht6000api.ht6000.TriggerSlope;
import com.hantek.ht6000api.ht6000.TriggerSweep;
import com.hantek.ht6000api.ht6000.VoltsPerDivision;
import com.openhantek.hantek6000.BuildConfig;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainRepository implements MainDataSource{

    private static MainRepository mInstance = new MainRepository();
    private static final String TAG = "MainRepository";
    // The directory to save APP files.
    private static final String APP_DIR = "Hantek6000";
    private static SharedPreferences mSharedPreferences;
    // Scope settings saved when the app exits.
    private HtScopeSettings mScopeSettings;
    // Message listeners
    private List<DataSourceListener> mListeners = new ArrayList<>();
    public static MainRepository getInstance() {
        return mInstance;
    }

    public static MainDataSource getInstance(SharedPreferences preferences) {
        mSharedPreferences = preferences;
        return mInstance;
    }

    //region Overload method
    @Override
    public int getAnalogChannelCount() {
        return HantekSdk.getAnalogChannelsCount();
    }

    @Override
    public boolean isChannelEnabled(int i) {
        return HantekSdk.getChannelStatus()[i];
    }

    @Override
    public void addDeviceListener(HantekDeviceListener listener) {
        HantekSdk.addDeviceListener(listener);
    }

    @Override
    public void removeDeviceListener(HantekDeviceListener listener) {
        HantekSdk.removeDeviceListener(listener);
    }

    @Override
    public String getVoltsDivString(int chIndex) {
        return HantekSdk.getVoltsDivString(chIndex);
    }

    @Override
    public String getTimeBaseString() {
        return HantekSdk.getTimeBaseString();
    }

    @Override
    public boolean isRunning() {
        return HantekSdk.isScopeRunning();
    }

    @Override
    public boolean isTriggered() {
        return HantekSdk.isTriggered();
    }

    @Override
    public void switchChannelEnabled(int i) {
        HantekSdk.switchChannelEnabled(i);
    }

    @Override
    public void switchRunStop() {
        HantekSdk.switchRunStop();
    }

    @Override
    public void startAutoset() {
        HantekSdk.startAutoset();
    }

    @Override
    public boolean isDisconnected() {
        return HantekSdk.isDisConnected();
    }

    @Override
    public boolean isSelfCalibrating() {
        return HantekSdk.isSelfCalibrating();
    }

    @Override
    public boolean isInRealMode() {
        return HantekSdk.isInRealMode();
    }

    @Override
    public int getChannelPos(int chIndex) {
        if (HantekSdk.getDevice() == null) return -1;
        return HantekSdk.getDevice().getChannels()[chIndex].getLevel();
    }

    @Override
    public void setChannelPos(int i, int position) {
        HantekSdk.setChannelPos(i, position);
    }

    @Override
    public int getTriggerLevelPos() {
        return HantekSdk.getTriggerLevel();
    }

    @Override
    public void setTriggerLevelPos(int position) {
        HantekSdk.setTriggerLevel(position);
    }

    @Override
    public void setCoupling(int chIndex, InputCoupling inputCoupling) {
        HantekSdk.setCoupling(chIndex, inputCoupling);
    }

    @Override
    public InputCoupling getCoupling(int chIndex) {
        return HantekSdk.getCoupling(chIndex);
    }

    @Override
    public AttenuationFactor getAttenuationFactor(int chIndex) {
        return HantekSdk.getAttenuationFactor(chIndex);
    }

    @Override
    public void setAttenuationFactor(int chIndex, AttenuationFactor attenuationFactor) {
        HantekSdk.setAttenuationFactor(chIndex, attenuationFactor);
    }

    @Override
    public void setTriggerSource(int source) {
        HantekSdk.setTriggerSource(source);
    }

    @Override
    public void setTriggerSlope(TriggerSlope slope) {
        HantekSdk.setTriggerSlope(slope);
    }

    @Override
    public void setTriggerSweep(TriggerSweep sweep) {
        HantekSdk.setTriggerSweep(sweep);
    }

    @Override
    public TriggerSweep getTriggerSweep() {
        return HantekSdk.getTriggerSweep();
    }

    @Override
    public int getTriggerSource() {
        return HantekSdk.getTriggerSource();
    }

    @Override
    public TriggerSlope getTriggerSlope() {
        return HantekSdk.getTriggerSlope();
    }

    @Override
    public void centerChannelLevel(int chIndex) {
        HantekSdk.centerChannelLevel(chIndex);
    }

    @Override
    public void centerTriggerLevel() {
        HantekSdk.centerTriggerLevel();
    }

    @Override
    public InputCoupling getInputCoupling(int chIndex) {
        return HantekSdk.getCoupling(chIndex);
    }

    @Override
    public void increaseVoltsPerDiv() {
        HantekSdk.increaseVoltsPerDiv();
    }

    @Override
    public void decreaseVoltsPerDiv() {
        HantekSdk.decreaseVoltsPerDiv();
    }

    @Override
    public void increaseTimebase() {
        HantekSdk.increaseTimebase();
    }

    @Override
    public void decreaseTimebase() {
        HantekSdk.decreaseTimebase();
    }

    @Override
    public void setSelectedChannel(int chIndex) {
        HantekSdk.setSelectedChannel(chIndex);
    }

    @Override
    public boolean isLargestVoltsPerDiv() {
        return HantekSdk.isLargestVoltsPerDiv();
    }

    @Override
    public boolean isSmallestVoltsPerDiv() {
        return HantekSdk.isSmallestVoltsPerDiv();
    }

    @Override
    public boolean isLargestTimebase() {
        return HantekSdk.isLargestTimebase();
    }

    @Override
    public boolean isSmallestTimebase() {
        return HantekSdk.isSmallestTimebase();
    }

    @Override
    public void clearChannels() {
        HantekSdk.clearSamples();
    }

    @Override
    public void addAutoMeasure(AutomeasureType type) {
        HantekSdk.addAutoMeasure(type);
    }

    @Override
    public void setAutoMeasureSource(int chIndex) {
        HantekSdk.setAutoMeasureSource(chIndex);
    }

    @Override
    public int getAutoMeasureSource() {
        return HantekSdk.getAutoMeasureSource();
    }

    @Override
    public void resetToFactory() {
        HantekSdk.resetToFactory();
    }

    @Override
    public boolean isInRealDeviceMode() {
        return HantekSdk.isInRealMode();
    }

    @Override
    public void startSelfCalibration() {
        HantekSdk.startSelfCalibration();
    }

    @Override
    public boolean isCursorMeasureEnabled() {
        return HantekSdk.isCursorEnabled();
    }

    @Override
    public String saveData(int chIndex) {

        // Get the full path name of the file.
        String appDir = getAppFileDirectory();
        if (appDir == null) return null;
        String fileName = getFileName();
        String fullName = appDir + fileName + ".csv";

        // Get the file content to save.
        byte[] bytes = HantekSdk.getCsvContents(chIndex);
        if (bytes == null) return null;

        // Save file.
        if(writeContentToFile(fullName, bytes)) {
            return fullName;
        } else {
            return null;
        }
    }

    @Override
    public String[] getFiles(String ext) {
        if (ext == null) return null;

        String appDir = getAppFileDirectory();
        if (appDir == null) return null;

        return getFileArray(appDir, ext);
    }

    @Override
    public void loadCsvData(String fileName) {
        if (fileName == null) return;

        String appDir = getAppFileDirectory();
        if (appDir == null) return;

        if (!fileName.toLowerCase().endsWith(".csv")) {
            Log.e(TAG, "file extension must be csv");
            return;
        }

        String fullName = appDir + fileName;
        byte[] contents = getFileBytes(fullName);

        HantekSdk.loadCsvData(contents);
    }

    @Override
    public void loadRfcData(String fileName) {
        if (fileName == null) return;

        String appDir = getAppFileDirectory();
        if (appDir == null) return;

        if (!fileName.toLowerCase().endsWith(".rfc")) {
            Log.e(TAG, "file extension must be rfc");
            return;
        }

        String fullName = appDir + fileName;
        byte[] contents = getFileBytes(fullName);
        boolean loadOk = HantekSdk.loadRfcData(contents);
        if (loadOk) {
            // Open the reference waveform as soon as it is loaded successfully.
            setRefEnable(true);
            for (DataSourceListener listener : mListeners) {
                listener.onLoadRfcFileSuccessfully(getRefChannel());
            }
        }
    }

    @Override
    public void setRefEnable(boolean enabled) {
        HantekSdk.setRefEnabled(enabled);
    }

    @Override
    public Channel getRefChannel() {
        return HantekSdk.getRefChannel();
    }

    @Override
    public int getRefChannelPos() {
        if (HantekSdk.getDevice() == null) return -1;
        return HantekSdk.getDevice().getRef().getLevel();
    }

    @Override
    public void setRefLevelPos(int position) {
        HantekSdk.setRefLevelPos(position);
    }

    @Override
    public int getMathChannelPos(){
        if (HantekSdk.getDevice() == null) return -1;
        return HantekSdk.getDevice().getMath().getLevel();
    }

    @Override
    public void setMathLevelPos(int position) {
        HantekSdk.setMathLevelPos(position);
    }

    @Override
    public void sendSetRefEnabledMessage(boolean enabled) {
        for(DataSourceListener listener: mListeners) {
            listener.onSetRefEnabled(enabled);
        }
    }

    @Override
    public MathChannel getMathChannel() {
        return HantekSdk.getMathChannel();
    }

    @Override
    public void sendUpdateScopeViewMathChannelSettingsMessage(Channel mathChannel) {
        for(DataSourceListener listener: mListeners) {
            listener.onUpdateScopeViewMathChannelSettings(mathChannel);
        }
    }

    @Override
    public void setMathSourceAIndex(int index) {
        if (HantekSdk.getDevice() == null) return;
        HantekSdk.getDevice().getMath().setSourceAIndex(index);
    }

    @Override
    public void setMathSourceBIndex(int index) {
        if (HantekSdk.getDevice() == null) return;
        HantekSdk.getDevice().getMath().setSourceBIndex(index);
    }

    @Override
    public void setMathVoltsPerDiv(VoltsPerDivision voltsPerDivision) {
        HantekSdk.setMathVoltsPerDiv(voltsPerDivision);
    }

    @Override
    public int getMathSourceAIndex() {
        return HantekSdk.getMathSourceAIndex();
    }

    @Override
    public int getMathSourceBIndex() {
        return HantekSdk.getMathSourceBIndex();
    }

    @Override
    public int getMathVoltsIndex() {
        return HantekSdk.getMathVoltsIndex();
    }

    @Override
    public int getMathOperator() {
        return HantekSdk.getMathOperator();
    }

    @Override
    public boolean isMathEnabled() {
        return HantekSdk.isMathEnabled();
    }

    @Override
    public void sendSetMathEnabledMessage(boolean enabled) {
        for (DataSourceListener listener: mListeners) {
            listener.onSetMathEnabled(enabled);
        }
    }

    @Override
    public void addListeners(DataSourceListener listener) {
        mListeners.add(listener);
    }

    @Override
    public void removeListener(DataSourceListener listener) {
        mListeners.remove(listener);
    }

    @Override
    public boolean isRefEnabled() {
        return HantekSdk.isRefEnabled();
    }

    @Override
    public void sendUpdateScopeViewRefChannelSettingsMessage(Channel refChannel) {
        for (DataSourceListener listener: mListeners) {
            listener.onUpdateScopeViewRefChannelSetting(refChannel);
        }
    }

    @Override
    public void sendSetRefViewPortPosMessage(int refViewPortPos) {
        for (DataSourceListener listener: mListeners) {
            listener.onSetRefViewPortPos(refViewPortPos);
        }
    }
    //endregion Overload method

    //region Auxiliary method

    /**
     * Get APP file directory.
     * <p>
     *     APP saves files to APP file directory.
     * </p>
     * @return APP file directory.
     */
    private String getAppFileDirectory() {
        String state = Environment.getExternalStorageState();
        if (!(Environment.MEDIA_MOUNTED.equals(state))) return null;

        String dir = Environment.getExternalStorageDirectory().getAbsolutePath();
        dir = dir + File.separator + APP_DIR + File.separator;

        // 如果文件夹不存在，创建
        File f = new File(dir);
        if (!f.isDirectory()) {
            if (!f.mkdir()) {
                Log.e(TAG, "Failed to create the directory:" + dir);
                return null;
            }
        }

        return dir;
    }

    /**
     * 将内容写入文件
     *
     * @param fullName 要保存的文件路径 例如：/storage/emulated/0/Hantek6000/2018-06-06_10-15-40.csv
     * @param contents 文件内容
     */
    private static boolean writeContentToFile(String fullName, byte[] contents) {
        if (null == fullName) return false;
        if (null == contents) return false;

        boolean result = true;

        FileOutputStream outputStream;
        File file = new File(fullName);
        if (file.exists()) {
            if (!file.delete()) {
                Log.w(TAG, "FAIL: delete file：" + fullName);
            }
        }
        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(contents);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }

        return result;
    }


    /**
     * Get a file name. Like: "2018-04-21_14-23-45"
     * @return the file name
     */
    private String getFileName() {
        Date currentTime = Calendar.getInstance().getTime();
        // HH:24 小时制
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault());
        return df.format(currentTime);
    }

    /**
     * Get The file names in the directory which extension is {@param dir}.
     * @param dir The full path directory to search.
     * @param ext file extension.
     * @return file names
     */
    private String[] getFileArray(String dir, final String ext) {
        if (dir == null) return null;
        if (ext == null) return null;

        File directory = new File(dir);
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(ext);
            }
        };

        File[] files = directory.listFiles(filter);
        if (files == null) return null;

        if (files.length > 0) {
            String[] fileArray = new String[files.length];
            for (int i = 0; i < files.length; i++) {
                fileArray[i] = files[i].getName();
            }
            return fileArray;
        } else {
            return null;
        }
    }

    /**
     * Get file contents.
     * @param fullName full path file name.
     * @return file contents.
     */
    private byte[] getFileBytes(String fullName) {
        if (fullName == null) return null;

        File file = new File(fullName);
        BufferedInputStream buf;
        int size = (int) file.length();
        byte[] bytes = new byte[size];

        try {
            buf = new BufferedInputStream(new FileInputStream(file));
            int read = buf.read(bytes, 0, bytes.length);
            if (read < 1) {
                bytes = null;
            }
            buf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }

    private void setScopeSettings(HtScopeSettings settings) {
        mScopeSettings = settings;
    }

    /**
     * Get current scope settings.
     * @return current scope settings
     */
    private HtScopeSettings getCurrentScopeSettings() {
        HtScopeSettings settings = new HtScopeSettings();

        for (int i = 0; i < getAnalogChannelCount(); i++) {
            settings.setChEnabled(i, isChEnabled(i));
            settings.setChZeroLevel(i, getChannelPos(i));
            settings.setVoltsPerDivision(i, getVoltsPerDivision(i).ordinal());
            settings.setAttenuationFactor(i, getAttenuationFactor(i).ordinal());
            settings.setInputCoupling(i, getInputCoupling(i).ordinal());
        }

        settings.setTriggerSweep(getTriggerSweep().ordinal());
        settings.setTriggerSource(getTriggerSource());
        settings.setTriggerLevel(getTriggerLevelPos());
        settings.setTriggerXPos(getTriggerXPos());
        settings.setTriggerSlope(getTriggerSlope().ordinal());

        settings.setRunning(isRunning());
        settings.setMemoryDepth(getMemoryDepth());
        settings.setTimeBase(getTimeBase().ordinal());
        settings.setCaptureMode(getCaptureMode().ordinal());
        settings.setFrequencyMeterEnabled(isFrequencyMeterEnabled());
        settings.setCounterEnabled(isCounterEnabled());

        settings.setAutoMeasureNumber(getAutoMeasureNumber());
        for (int i = 0; i < settings.getAutoMeasureNumber(); i++ ) {
            updateAutoMeasureTypes(settings.getAutoMeasureTypes());
        }
        for (int i = 0; i < settings.getAutoMeasureNumber(); i++ ) {
            updateAutoMeasureSources(settings.getAutoMeasureSources());
        }

        return settings;
    }
    //endregion Auxiliary method

    @Override
    public String getAppVersion() {
        return BuildConfig.VERSION_NAME;
    }

    @Override
    public String getFpgaVersion() {
        return HantekSdk.getFpgaVersion();
    }

    @Override
    public String getDriverVersion() {
        return HantekSdk.getDriverVersion();
    }

    @Override
    public String getProductSn() {
        return HantekSdk.getProductSn();
    }

    @Override
    public void searchWifiDevice() {

    }

    @Override
    public  TimeBase getTimeBase() {
        return HantekSdk.getTimeBase();
    }

    @Override
    public boolean isFrequencyMeterEnabled() {
        return HantekSdk.isFrequencyMeterEnabled();
    }

    @Override
    public boolean isCounterEnabled() {
        return HantekSdk.isCounterEnabled();
    }

    @Override
    public boolean isChEnabled(int chIndex) {
        return HantekSdk.getChannelStatus()[chIndex];
    }

    @Override
    public VoltsPerDivision getVoltsPerDivision(int chIndex) {
        return HantekSdk.getVoltsPerDivision(chIndex);
    }

    @Override
    public int getTriggerXPos() {
        return HantekSdk.getTriggerXPos();
    }

    @Override
    public int getMemoryDepth() {
        return HantekSdk.getMemoryDepth();
    }

    @Override
    public int getAutoMeasureNumber() {
        if (HantekSdk.getAutoMeasureItems() == null) return 0;
        return HantekSdk.getAutoMeasureItems().size();
    }

    @Override
    public void updateAutoMeasureTypes(int[] autoMeasureTypes) {
        if (autoMeasureTypes == null) return;
        if (HantekSdk.getAutoMeasureItems() == null) return;
        if (autoMeasureTypes.length < HantekSdk.getAutoMeasureItems().size()) return;

        for (int i = 0; i < HantekSdk.getAutoMeasureItems().size(); i++) {
            autoMeasureTypes[i] = HantekSdk.getAutoMeasureItems().get(i).type.ordinal();
        }
    }

    @Override
    public void updateAutoMeasureSources(int[] autoMeasureSources) {
        if (autoMeasureSources == null) return;
        if (HantekSdk.getAutoMeasureItems() == null) return;
        if (autoMeasureSources.length < HantekSdk.getAutoMeasureItems().size()) return;

        for (int i = 0; i < HantekSdk.getAutoMeasureItems().size(); i++) {
            autoMeasureSources[i] = HantekSdk.getAutoMeasureItems().get(i).channel;
        }
    }

    @Override
    public void loadScopeSettings() {
        if (mSharedPreferences == null) return;

        // If no settings be saved before, do nothing.
        if (!mSharedPreferences.contains("ch1ZeroLevel")) return;
        Log.d(TAG, "Loading saved settings...");

        HtScopeSettings settings = new HtScopeSettings();
        int level; // zero level
        boolean enabled; // channel status
        int index; // enum index

        String[] keys = new String[]{"ch1Enabled", "ch2Enabled", "ch3Enabled", "ch4Enabled"};
        for (int i = 0; i < keys.length; i++) {
            enabled = mSharedPreferences.getBoolean(keys[i], false);
            settings.setChEnabled(i, enabled);
        }

        keys = new String[]{"ch1ZeroLevel", "ch2ZeroLevel", "ch3ZeroLevel", "ch4ZeroLevel"};
        for (int i = 0; i < keys.length; i++) {
            level = mSharedPreferences.getInt(keys[i], 0);
            settings.setChZeroLevel(i, level);
        }

        keys = new String[]{"ch1VoltsPerDiv", "ch2VoltsPerDiv", "ch3VoltsPerDiv", "ch4VoltsPerDiv"};
        for (int i = 0; i < keys.length; i++) {
            index = mSharedPreferences.getInt(keys[i], 0);
            settings.setVoltsPerDivision(i, index);
        }

        keys = new String[]{"ch1AttenuationFactor", "ch2AttenuationFactor",
                "ch3AttenuationFactor", "ch4AttenuationFactor"};
        for (int i = 0; i < keys.length; i++) {
            index = mSharedPreferences.getInt(keys[i], 0);
            settings.setAttenuationFactor(i, index);
        }

        keys = new String[]{"ch1InputCoupling", "ch2InputCoupling",
                "ch3InputCoupling", "ch4InputCoupling"};
        for (int i = 0; i < keys.length; i++) {
            index = mSharedPreferences.getInt(keys[i], 0);
            settings.setInputCoupling(i, index);
        }

        settings.setTriggerSweep(mSharedPreferences.getInt("triggerSweep", 0));
        settings.setTriggerSource(mSharedPreferences.getInt("triggerSource", 0));
        settings.setTriggerLevel(mSharedPreferences.getInt("triggerLevel", 0));
        settings.setTriggerXPos(mSharedPreferences.getInt("triggerXPos", 0));
        settings.setTriggerSlope(mSharedPreferences.getInt("triggerSlope", 0));

        settings.setRunning(mSharedPreferences.getBoolean("running", true));
        settings.setMemoryDepth(mSharedPreferences.getInt("memoryDepth", 0));
        settings.setTimeBase(mSharedPreferences.getInt("timeBase", 0));
        settings.setCaptureMode(mSharedPreferences.getInt("captureMode", 0));
        settings.setFrequencyMeterEnabled(mSharedPreferences.getBoolean("isFrequencyMeterEnabled", false));
        settings.setCounterEnabled(mSharedPreferences.getBoolean("isCounterEnabled", false));

        settings.setAutoMeasureNumber(mSharedPreferences.getInt("autoMeasureNumber", 0));
        for (int i = 0; i < settings.getAutoMeasureTypes().length; i++) {
            int value = mSharedPreferences.getInt("autoMeasureType" + String.valueOf(i), 0);
            settings.getAutoMeasureTypes()[i] = value;
        }
        for (int i = 0; i < settings.getAutoMeasureSources().length; i++) {
            int value = mSharedPreferences.getInt("autoMeasureSource" + String.valueOf(i), 0);
            settings.getAutoMeasureSources()[i] = value;
        }

        setScopeSettings(settings);
    }

    @Override
    @SuppressLint("ApplySharedPref")
    public void saveScopeSettings() {
        if (mSharedPreferences == null) return;
        Log.d(TAG, "Save scope settings...");
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        HtScopeSettings settings = getCurrentScopeSettings();

        String[] keys = new String[]{"ch1Enabled", "ch2Enabled", "ch3Enabled", "ch4Enabled"};
        for (int i = 0; i < keys.length; i++) {
            editor.putBoolean(keys[i], settings.getChEnabled(i));
        }

        keys = new String[]{"ch1ZeroLevel", "ch2ZeroLevel", "ch3ZeroLevel", "ch4ZeroLevel"};
        for (int i = 0; i < keys.length; i++) {
            editor.putInt(keys[i], settings.getChZeroLevel(i));
        }

        keys = new String[]{"ch1VoltsPerDiv", "ch2VoltsPerDiv", "ch3VoltsPerDiv", "ch4VoltsPerDiv"};
        for (int i = 0; i < keys.length; i++) {
            editor.putInt(keys[i], settings.getVoltsPerDivision(i));
        }

        keys = new String[]{"ch1AttenuationFactor", "ch2AttenuationFactor",
                "ch3AttenuationFactor", "ch4AttenuationFactor"};
        for (int i = 0; i < keys.length; i++) {
            editor.putInt(keys[i], settings.getAttenuationFactor(i));
        }

        keys = new String[]{"ch1InputCoupling", "ch2InputCoupling",
                "ch3InputCoupling", "ch4InputCoupling"};
        for (int i = 0; i < keys.length; i++) {
            editor.putInt(keys[i], settings.getInputCoupling(i));
        }

        editor.putInt("triggerSource", settings.getTriggerSource());
        editor.putInt("triggerLevel", settings.getTriggerLevel());
        editor.putInt("triggerXPos", settings.getTriggerXPos());
        editor.putInt("triggerSlope", settings.getTriggerSlope());
        editor.putInt("triggerSweep", settings.getTriggerSweep());

        editor.putBoolean("running", settings.isRunning());
        editor.putInt("memoryDepth", settings.getMemoryDepth());
        editor.putInt("timeBase", settings.getTimeBase());
        editor.putInt("captureMode", settings.getCaptureMode());
        editor.putBoolean("isFrequencyMeterEnabled", settings.isFrequencyMeterEnabled());
        editor.putBoolean("isCounterEnabled", settings.isCounterEnabled());

        editor.putInt("autoMeasureNumber", settings.getAutoMeasureNumber());
        for (int i = 0; i < settings.getAutoMeasureTypes().length; i++) {
            int value = settings.getAutoMeasureTypes()[i];
            editor.putInt("autoMeasureType" + String.valueOf(i), value);
        }
        for (int i = 0; i < settings.getAutoMeasureSources().length; i++) {
            int value = settings.getAutoMeasureSources()[i];
            editor.putInt("autoMeasureSource" + String.valueOf(i), value);
        }

        // Commit the edits!
        editor.commit();
    }

    @Override
    public HtScopeSettings getScopeSettings() {
        return mScopeSettings;
    }

    @Override
    public void setTimebase(int timebaseIndex) {
        // If same as current timebase, do nothing.
        if (timebaseIndex == HantekSdk.getTimeBase().ordinal() ) return;

        HantekSdk.setTimebase(timebaseIndex);
    }

    @Override
    public boolean isInRollMode() {
        return getCaptureMode() == CaptureMode.Roll;
    }

    @Override
    public String saveRef(int chIndex) {

        // 获取文件全路径名
        String appDir = getAppFileDirectory();
        if (appDir == null) return null;
        String fileName = getFileName();
        String fullName = appDir + fileName + ".rfc";
        // 获取要保存的文件内容
        byte[] bytes = HantekSdk.getRefFileContents(chIndex);
        if (bytes == null) return null;

        // 保存文件
        if(writeContentToFile(fullName, bytes)) {
            return fullName;
        } else {
            return null;
        }
    }

    @Override
    public CaptureMode getCaptureMode() {
        return HantekSdk.getCaptureMode();
    }
}
