/*
 * Copyright (C) 2017 Angel Newton
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

/**
 * Constants
 */
public final class Constants {

    public static final String LAST_ACTIVITY = "last_activity";
    public static final String MAIN_ACTIVITY = "main_activity";

    public static final String SHARED_PREF_NAME = "com.xengar.android.englishverbs";
    public static final String VERB_TYPE = "verb_type";
    public static final String REGULAR = "regular";
    public static final String IRREGULAR = "irregular";
    public static final String BOTH = "both (regular, irregular)";
    public static final String FAVORITES = "favorites";
    public static final String SORT_TYPE = "sort_type";
    public static final String ALPHABET = "alphabet";
    public static final String COLOR = "color";
    public static final String VERBS_ED = "verbs_ed";
    public static final String COMMON_TYPE = "common_type";
    public static final String MOST_COMMON_25 = "25";
    public static final String MOST_COMMON_50 = "50";
    public static final String MOST_COMMON_100 = "100";
    public static final String MOST_COMMON_500 = "500";
    public static final String MOST_COMMON_ALL = "all";

    public static final String ITEM_TYPE = "item_type";
    public static final String CARD = "card";
    public static final String LIST = "list";

    public static final String CURRENT_PAGE = "current_page";
    public static final String PAGE_VERBS = "Verbs";
    public static final String PAGE_CARDS = "Cards";
    public static final String PAGE_FAVORITES = "Favorites";

    public static final String VERB_ID = "verb_id";
    public static final String VERB_NAME = "verb_name";
    public static final String DEMO_MODE = "demo_mode";
    public static final String DELETED_VERB_ID = "deleted";
    public static final int REQUEST_CODE = 1;

    // Translation languages
    public static final String NONE = "None";
    public static final String FRENCH = "fr";
    public static final String SPANISH = "es";

    // Firebase strings
    public static final String TYPE_PAGE = "page";
    public static final String TYPE_AD = "Ad";
    public static final String DETAILS_ACTIVITY = "details_activity";
    public static final String PAGE_VERB_DETAILS = "Verb Details";
    public static final String PAGE_SEARCH = "Search";
    public static final String PAGE_EDITOR = "Editor";
    public static final String PAGE_HELP = "Help";
    public static final String TYPE_ADD_FAV = "add to Favorites";
    public static final String TYPE_DEL_FAV = "remove from Favorites";
    public static final String TYPE_ADD_USER_VERB = "add user Verb";
    public static final String TYPE_DEL_USER_VERB = "remove user Verb";
    public static final String TYPE_EDI_USER_VERB = "edit user Verb";
    public static final String VERBS = "verbs";


    /**
     * Boolean used to log or not lines
     * Usage:
     *      if (LOG) {
     *           if (condition) Log.i(...);
     *      }
     *  When you set LOG to false, the compiler will strip out all code inside such checks
     * (since it is a static final, it knows at compile time that code is not used.)
     * http://stackoverflow.com/questions/2446248/remove-all-debug-logging-calls-before-publishing-are-there-tools-to-do-this
     */
    public static final boolean LOG = true;

    /**
     * Enable test ads for AdMob
     * See ActivityUtils.createAdMobBanner()
     */
    public static final boolean USE_TEST_ADS = true;

}
