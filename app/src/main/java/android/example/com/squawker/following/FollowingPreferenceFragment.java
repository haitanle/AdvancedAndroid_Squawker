/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package android.example.com.squawker.following;

import android.content.SharedPreferences;
import android.example.com.squawker.R;
import android.os.Bundle;
import android.util.Log;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.firebase.messaging.FirebaseMessaging;


/**
 * Shows the list of instructors you can follow
 * This class is response for setting the topics based on Preference setting
 */
public class FollowingPreferenceFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final static String LOG_TAG = FollowingPreferenceFragment.class.getSimpleName();

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Add visualizer preferences, defined in the XML file in res->xml->preferences_squawker
        addPreferencesFromResource(R.xml.following_squawker);
        //register the preference change listener
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        //set the topic according to the preference's key
        Preference preference = findPreference(key);
        if (preference != null){
            boolean isOn = sharedPreferences.getBoolean(key, false);

            if (isOn){
                FirebaseMessaging.getInstance().subscribeToTopic(key);
                Log.d(LOG_TAG, "Subscribing to topic :"+key);
            }else{
                FirebaseMessaging.getInstance().unsubscribeFromTopic(key);
                Log.d(LOG_TAG, "UnSubscribing to topic :"+key);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
