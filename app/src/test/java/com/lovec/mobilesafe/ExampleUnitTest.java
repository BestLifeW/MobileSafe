package com.lovec.mobilesafe;

import android.test.AndroidTestCase;
import android.util.Log;

import com.lovec.mobilesafe.engine.ContactEngine;

import java.util.HashMap;
import java.util.List;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest extends AndroidTestCase {

    public void TestContacts() {
        List<HashMap<String, String>> list = ContactEngine.getAllContactInfo(getContext());
        for (HashMap<String, String> hashmap : list
                ) {
            Log.i("text", "姓名:" + hashmap.get("name") + "电话" + hashmap.get("phone"));

        }
    }
}