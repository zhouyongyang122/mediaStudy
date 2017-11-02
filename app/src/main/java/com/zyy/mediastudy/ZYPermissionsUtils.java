package com.zyy.mediastudy;

import android.content.Context;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by ZY on 17/11/1.
 */

public class ZYPermissionsUtils {

    public static boolean hasPermissions(Context context, String permission) {
        return EasyPermissions.hasPermissions(context, permission);
    }
}
