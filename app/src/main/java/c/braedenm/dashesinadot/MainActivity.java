package c.braedenm.dashesinadot;

import android.content.Context;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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
    /* Local Variables */
    private final String DEFAULT_LOCATION = "defaultID";
    private final boolean ACTIVITY_DEFAULT = false;
    private Boolean active;

    /* UI Elements */
    private Button transmitButton;
    private Button changeID;
    private EditText editID;
    private TextView currentID;
    //private TextView activeUsers;
    private Spinner userMenu;


    /* Local Services */
    private Vibrator vibrator;

    /* Remote Services */
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference transmitDB;
    private DatabaseReference recieveDB;

    /**
     * This is called when the application loads activity_main
     * @param savedInstanceState Previous instance of the program
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        /* Setup Window */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Default Values */
        active = ACTIVITY_DEFAULT;

        updateTransmitLocation(DEFAULT_LOCATION);
        updateReceiveLocation(DEFAULT_LOCATION);

        transmitDB.setValue(ACTIVITY_DEFAULT);

        initButtonPairs();
        initTransmitListener();
    }

    /**
     * Updates the userID that this device is transmitting to
     * @param location ID of location that is being transmitted to
     */
    private void updateTransmitLocation(String location)
    {
        transmitDB = database.getReference(location);
    }

    /**
     * Updates the userID that this device is receiving from
     * @param location ID of location that is being received from
     */
    private void updateReceiveLocation(String location)
    {
        recieveDB = database.getReference(location);
        initReceiveListener();
    }

    /**
     * Called in setup, pairs logical references to UI elements
     */
    private void initButtonPairs()
    {
        transmitButton = findViewById(R.id.transmitButton);
        changeID = findViewById(R.id.changeID);
        editID = findViewById(R.id.enterID);
        currentID = findViewById(R.id.currentID);
        userMenu = findViewById(R.id.userMenu);
    }

    /**
     * Called in setup, contains the logic for when the transmit button is pressed
     */
    private void initTransmitListener()
    {
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        transmitButton.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    //long[] pattern = {0, 200, 0}; //0 to start now, 200 to vibrate 200 ms, 0 to sleep for 0 ms.
                    //vibrator.vibrate(pattern, 0); // 0 to repeat endlessly.
                    transmitDB.setValue(true);
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP || motionEvent.getAction() == MotionEvent.ACTION_CANCEL)
                {
                    //vibrator.cancel();
                    transmitDB.setValue(false);
                }
                return true;
            }

        });
    }

    /**
     * Called in setup, contains the logic for when the receive database updates
     */
    private void initReceiveListener()
    {
        recieveDB.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                active = dataSnapshot.getValue(Boolean.class);

                if (active)
                {
                    long[] pattern = {0, 200, 0}; //0 to start now, 200 to vibrate 200 ms, 0 to sleep for 0 ms.
                    vibrator.vibrate(pattern, 0); // 0 to repeat endlessly.
                }
                else
                {
                    vibrator.cancel();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.w("RecieveError", "Error retrieving database information");
            }
        });
    }
}
