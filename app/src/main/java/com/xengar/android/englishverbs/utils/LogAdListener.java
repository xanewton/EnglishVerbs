/*
 * Copyright (C) 2017 Angel Garcia
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xengar.android.englishverbs.utils;

import com.google.android.gms.ads.AdListener;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Locale;

import static com.xengar.android.englishverbs.utils.Constants.TYPE_AD;

/**
 * LogAdListener to listen for Ad Events.
 * https://firebase.google.com/docs/admob/android/ad-events
 */
public class LogAdListener extends AdListener {

    private String page;
    private FirebaseAnalytics analytics;

    //
     // Constructor
     // @param analytics FirebaseAnalytics
     // @param page page
     //
    public LogAdListener(FirebaseAnalytics analytics, String page){
        super();
        this.analytics = analytics;
        this.page = page;
    }

    @Override
    public void onAdLoaded() {
        // Code to be executed when an ad finishes loading.
        ActivityUtils.firebaseAnalyticsLogEventSelectContent(
                analytics, "Ad finished loading", page, TYPE_AD);
    }

    @Override
    public void onAdFailedToLoad(int errorCode) {
        // Code to be executed when an ad request fails.
        String msg = String.format(Locale.ENGLISH, "Ad failed to load with error code %d.",
                errorCode);
        ActivityUtils.firebaseAnalyticsLogEventSelectContent(analytics, msg, page, TYPE_AD);
    }

    @Override
    public void onAdOpened() {
        // Code to be executed when an ad opens an overlay that covers the screen.
        ActivityUtils.firebaseAnalyticsLogEventSelectContent(analytics, "Ad opened", page, TYPE_AD);
    }

    @Override
    public void onAdLeftApplication() {
        // Code to be executed when the user has left the app.
        ActivityUtils.firebaseAnalyticsLogEventSelectContent(
                analytics, "Ad left the application", page, TYPE_AD);
    }

    @Override
    public void onAdClosed() {
        // Code to be executed when when the user is about to return to the app after tapping on an ad.
        ActivityUtils.firebaseAnalyticsLogEventSelectContent(analytics, "Ad closed!", page, TYPE_AD);
    }
}
