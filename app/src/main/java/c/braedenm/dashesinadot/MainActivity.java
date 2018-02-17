package c.braedenm.dashesinadot;

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
        readout = findViewById(R.id.textView2);
        button = findViewById(R.id.button);
        active = false;

        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    active = true;
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    active = false;
                }
                System.out.println(active);
                reference.setValue(active);
                return true;
            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                /* Blank for now */
            }

            @Override
            public void onDataChange(DataSnapshot snapshot) {
            }
        });
    }
}
