package net.taplog.fragmentreplacetestproject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Shell activity contains view with R.id.content that can be filled from espresso test.
 * Created by Ehmer, R.G. on 8/1/17.
 */

public class FragmentManagerTestbedActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    private static AppCompatActivity activity;

    /**
     * Gets the currently displayed activity. Useful for espresso configuration change testing
     * because {@link android.support.test.rule.ActivityTestRule} only references the pre-
     * configuration change activity.
     *
     * @return the active activity (assumes no overlap)
     */
    public static AppCompatActivity getResumedActivity() {
        return activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_layout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
    }

    @Override
    protected void onPause() {
        super.onPause();
        activity = null;
    }

}