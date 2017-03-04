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

/**
 * Constants
 */
public final class Constants {

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

    public static final String LAST_ACTIVITY = "last_activity";
    public static final String MAIN_ACTIVITY = "main_activity";

    public static final String SHARED_PREF_NAME = "com.xengar.android.englishverbs";
    public static final String ITEM_CATEGORY = "item_category";
    public static final String PAGE_HOME = "Home";
    public static final String PAGE_REGULAR = "Regular";
    public static final String PAGE_IRREGULAR = "Irregular";


}
