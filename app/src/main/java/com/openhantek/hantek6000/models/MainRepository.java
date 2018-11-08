package com.openhantek.hantek6000.models;

import android.os.Environment;
import android.util.Log;

import com.hantek.ht6000api.HantekDeviceListener;
import com.hantek.ht6000api.HantekSdk;
import com.hantek.ht6000api.ht6000.AttenuationFactor;
import com.hantek.ht6000api.ht6000.AutomeasureType;
import com.hantek.ht6000api.ht6000.InputCoupling;
import com.hantek.ht6000api.ht6000.TriggerSlope;
import com.hantek.ht6000api.ht6000.TriggerSweep;
import com.openhantek.hantek6000.BuildConfig;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainRepository implements MainDataSource{

    private static MainRepository mInstance = new MainRepository();
    private static final String TAG = "MainRepository";
    // The directory to save APP files.
    private static final String APP_DIR = "Hantek6000";

    public static MainRepository getInstance() {
        return mInstance;
    }

    //region 重载方法
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
    public void setAttenuationFacotr(int chIndex, AttenuationFactor attenuationFactor) {
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
    public InputCoupling getChInputCoupling(int chIndex) {
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

        // 获取文件全路径名
        String appDir = getAppFileDirectory();
        if (appDir == null) return null;
        String fileName = getFileName();
        String fullName = appDir + fileName + ".csv";

        // 获取要保存的文件内容
        byte[] bytes = HantekSdk.getCsvContents(chIndex);
        if (bytes == null) return null;

        // 保存文件
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
    public void loadData(String fileName) {
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
    //endregion 重载方法

    //region 辅助方法

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
    //endregion 辅助方法


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
}
