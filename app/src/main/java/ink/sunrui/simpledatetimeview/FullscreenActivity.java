package ink.sunrui.simpledatetimeview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {
    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private String displayMode = "全部";
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHideRunnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_fullscreen);

        mContentView = findViewById(R.id.layout);

        setUIByConfiguration(getResources().getConfiguration());
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        hide();
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        mHideHandler.postDelayed(mHideRunnable, UI_ANIMATION_DELAY);
        startCounting();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setUIByConfiguration(newConfig);
    }

    private void setUIByConfiguration(Configuration configuration) {
        final TextView textDate = getDisplayDate();
        final TextView textTime = getDisplayTime();
        final TextView textDiff = getDisplayDiff();
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            textDate.setTextSize(100);
            textTime.setTextSize(300);
            textDiff.setTextSize(100);
        } else {
            textDate.setTextSize(50);
            textTime.setTextSize(150);
            textDiff.setTextSize(50);
        }
    }

    private TextView getDisplayDate() {
        return findViewById(R.id.textViewDate);
    }

    private TextView getDisplayTime() {
        return findViewById(R.id.textViewTime);
    }

    private TextView getDisplayDiff() {
        return findViewById(R.id.textViewDiff);
    }

    private void startCounting() {
        final Handler h = new Handler();
        final TextView textDate = getDisplayDate();
        final TextView textTime = getDisplayTime();
        final TextView textDiff = getDisplayDiff();
        h.postDelayed(new Runnable() {
            private long time = 0;

            @Override
            public void run() {
                // do stuff then
                // can call h again after work!
                time += 1;
                try {
                    Calendar cl = Calendar.getInstance();
                    textDate.setText(getDateString(cl));
                    textTime.setText(getTimeString(cl));
                    textDiff.setText(getDiffString(cl));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                h.postDelayed(this, 1000);
            }
        }, 1000); // 1 second delay (takes millis)
    }

    private String getDateString(Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(calendar.getTime());
    }

    private String getTimeString(Calendar calendar) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        return timeFormat.format(calendar.getTime());
    }

    private String getDiffString(Calendar calendar) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDay = dateFormat.parse("2020-05-31");
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        Date currentDate = calendar.getTime();
        long days = (currentDate.getTime() - birthDay.getTime()) / (1000 * 60 * 60 * 24);
        return String.format("%d 天\n%d 月 %d 天", days, (days / 30), (days % 30));
    }
}
