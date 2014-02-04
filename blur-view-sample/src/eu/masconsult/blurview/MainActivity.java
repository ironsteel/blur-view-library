
package eu.masconsult.blurview;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import eu.masconsult.blurview.library.FrameLayoutWithBluredBackground;

public class MainActivity extends Activity {

    private FrameLayoutWithBluredBackground bottom;
    private FrameLayoutWithBluredBackground top;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottom = (FrameLayoutWithBluredBackground) findViewById(R.id.bottom_blured_layout);
        top = (FrameLayoutWithBluredBackground) findViewById(R.id.top_blured_layout);

        SeekBar blurSeekBar = (SeekBar) findViewById(R.id.blur_radius_slider);
        blurSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress > 0) {
                    bottom.setBlurRadius(progress);
                    top.setBlurRadius(progress);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
