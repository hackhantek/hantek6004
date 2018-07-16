package com.openhantek.hantek6000.Utils;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UsbDeviceFilter {
    private final List<DeviceFilter> hostDeviceFilters;

    public UsbDeviceFilter(XmlPullParser parser) throws XmlPullParserException,
            IOException {
        hostDeviceFilters = new ArrayList<DeviceFilter>();
        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String tagName = parser.getName();
            if ("usb-device".equals(tagName)
                    && parser.getEventType() == XmlPullParser.START_TAG) {
                hostDeviceFilters.add(DeviceFilter.read(parser));
            }
            eventType = parser.next();
        }
    }

    public boolean matchesHostDevice(UsbDevice device) {
        for (DeviceFilter filter : hostDeviceFilters) {
            if (filter.matches(device)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check whether the equipment match certain conditions
     * @param ctx A non-null application context.
     * @param resourceId The resource ID pointing to a devices filter XML file.
     * @param device target device
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    public static boolean isDeviceMatch(Context ctx, int resourceId, UsbDevice device) throws XmlPullParserException, IOException {
        UsbManager usbManager = (UsbManager) ctx
                .getSystemService(Context.USB_SERVICE);
        XmlResourceParser parser = ctx.getResources().getXml(resourceId);
        UsbDeviceFilter devFilter;

        try {
            devFilter = new UsbDeviceFilter(parser);
        } finally {
            parser.close();
        }

        return devFilter.matchesHostDevice(device);
    }

    public static class DeviceFilter {
        // USB Vendor ID (or -1 for unspecified)
        public final int mVendorId;
        // USB Product ID (or -1 for unspecified)
        public final int mProductId;
        // USB device or interface class (or -1 for unspecified)
        public final int mClass;
        // USB device subclass (or -1 for unspecified)
        public final int mSubclass;
        // USB device protocol (or -1 for unspecified)
        public final int mProtocol;

        private DeviceFilter(int vid, int pid, int clasz, int subclass,
                             int protocol) {
            mVendorId = vid;
            mProductId = pid;
            mClass = clasz;
            mSubclass = subclass;
            mProtocol = protocol;
        }

        private static DeviceFilter read(XmlPullParser parser) {
            int vendorId = -1;
            int productId = -1;
            int deviceClass = -1;
            int deviceSubclass = -1;
            int deviceProtocol = -1;

            int count = parser.getAttributeCount();
            for (int i = 0; i < count; i++) {
                String name = parser.getAttributeName(i);
                // All attribute values are ints
                int value = Integer.parseInt(parser.getAttributeValue(i));

                if ("vendor-id".equals(name)) {
                    vendorId = value;
                } else if ("product-id".equals(name)) {
                    productId = value;
                } else if ("class".equals(name)) {
                    deviceClass = value;
                } else if ("subclass".equals(name)) {
                    deviceSubclass = value;
                } else if ("protocol".equals(name)) {
                    deviceProtocol = value;
                }
            }

            return new DeviceFilter(vendorId, productId, deviceClass,
                    deviceSubclass, deviceProtocol);
        }

        private boolean matches(int clasz, int subclass, int protocol) {
            return ((mClass == -1 || clasz == mClass)
                    && (mSubclass == -1 || subclass == mSubclass)
                    && (mProtocol == -1 || protocol == mProtocol));
        }

        public boolean matches(UsbDevice device) {
            if (mVendorId != -1 && device.getVendorId() != mVendorId)
                return false;
            if (mProductId != -1 && device.getProductId() != mProductId)
                return false;

            // check device class/subclass/protocol
            if (matches(device.getDeviceClass(), device.getDeviceSubclass(),
                    device.getDeviceProtocol()))
                return true;

            // if device doesn't match, check the interfaces
            int count = device.getInterfaceCount();
            for (int i = 0; i < count; i++) {
                UsbInterface intf = device.getInterface(i);
                if (matches(intf.getInterfaceClass(),
                        intf.getInterfaceSubclass(),
                        intf.getInterfaceProtocol()))
                    return true;
            }

            return false;
        }
    }
}
