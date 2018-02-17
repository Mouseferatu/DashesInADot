package c.braedenm.dashesinadot;

import android.content.Context;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity
{
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference("test");

    Button b_vibe;
    Vibrator v;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reference.setValue("quack");

        //b_vibe = (Button) findViewById(R.id.button2);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        b_vibe.setOnClickListener (new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //vibrate in ms
                v.vibrate(100);
            }
        });
    }
}
