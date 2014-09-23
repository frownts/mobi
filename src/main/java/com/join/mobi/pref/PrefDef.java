package com.join.mobi.pref;

import org.androidannotations.annotations.sharedpreferences.DefaultBoolean;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-2-25
 * Time: 上午10:34
 */

@SharedPref(value = SharedPref.Scope.UNIQUE)
public interface PrefDef {

    String rpcUserId();

    String userId();

    String showName();

    @DefaultBoolean(true)
    boolean uncompleteDownload();

    @DefaultBoolean(true)
    boolean continueOnWifi();

    @DefaultBoolean(true)
    boolean annonunceWhenDownload();

    @DefaultBoolean(true)
    boolean autoLogin();

    @DefaultBoolean(false)
    boolean isLogin();

}
