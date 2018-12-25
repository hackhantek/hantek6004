package com.openhantek.hantek6000.views;

import com.openhantek.hantek6000.models.MainDataSource;
import com.openhantek.hantek6000.models.MainRepository;

// To decouple class dependency.
public class Injection {
    // To handle permission;
    private static PermissionHandler mPermissionHandler;

    /**
     * Get permission handler;
     * @return permission handler;
     */
    public static PermissionHandler providePermissionHandler() {
        if (mPermissionHandler == null) {
            mPermissionHandler = new PermissionHandlerAndroid();
        }
        return mPermissionHandler;
    }

    /**
     * Provide data source.
     * @return data source.
     */
    static MainDataSource provideDataSource() {
        return MainRepository.getInstance();
    }
}
