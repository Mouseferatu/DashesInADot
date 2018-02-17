package c.braedenm.dashesinadot;

import android.content.Context;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity
{
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference("test");

    Button b_vibe;
    Vibrator v;

/**
 * Links with the activity_main and controls its activities
 */
public class MainActivity extends AppCompatActivity
{
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference("test");
    private DatabaseReference person = database.getReference("person1");
    private TextView readout;
    private Button button;
    private Boolean active;

    /**
     * This is called when the application loads activity_main
     * @param savedInstanceState Previous instance of the program
     */
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
