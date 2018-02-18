package c.braedenm.dashesinadot;

import android.content.Context;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Links with the activity_main and controls its activities
 * @author Braeden Muller
 * @author James Zhang
 * @author Katherine Sophery Yap
 * @version 2018.2.18
 */
public class MainActivity extends AppCompatActivity
{
    /* Local Variables */
    private final String DEFAULT_LOCATION = "defaultID";
    private final boolean ACTIVITY_DEFAULT = false;
    private boolean activeTransmit;

    private ArrayList postPacket = new ArrayList();
    private ArrayList pullPacket = new ArrayList();
    private String user;
    private long pullStamp = 0;

    private Iterator pullPacketIterator = pullPacket.iterator();

    private String receiveLocation;

    /* UI Elements */
    private Button transmitButton;
    private Button changeID;
    private EditText editID;
    private TextView currentID;
    private Button changeRC;
    private EditText editRC;
    private TextView currentRC;


    /* Local Services */
    private Vibrator vibrator;
    private Handler handler = new Handler();

    /* Remote Services */
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference transmitDB;
    private DatabaseReference receiveDB;

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
        activeTransmit = ACTIVITY_DEFAULT;

        user = DEFAULT_LOCATION;
        receiveLocation = DEFAULT_LOCATION;
        updateTransmitLocation(DEFAULT_LOCATION);
        updateReceiveLocation(DEFAULT_LOCATION);

        writeDB(postPacket);

        initButtonPairs();
        initNameListener();
        initTransmitListener();
        initPostThread();
        initPullThread();
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
        receiveDB = database.getReference(location);
        initReceiveListener();
    }

    /**
     * Push a new user onto the database
     */
    private void initNameListener()
    {
        changeID.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                user = editID.getText().toString();
                updateTransmitLocation(user);
                currentID.setText(user);
            }
        });

        changeRC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                receiveLocation = editRC.getText().toString();
                updateReceiveLocation(receiveLocation);
                currentRC.setText(receiveLocation);
            }
        });
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

        changeRC = findViewById(R.id.changeRC);
        editRC = findViewById(R.id.enterRC);
        currentRC = findViewById(R.id.currentRC);
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
                    activeTransmit = true;
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP || motionEvent.getAction() == MotionEvent.ACTION_CANCEL)
                {
                    //vibrator.cancel();
                    activeTransmit = false;
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
        receiveDB.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                ArrayList<Boolean> pulledData = (ArrayList<Boolean>)dataSnapshot.child("values").getValue();
                long stamp = dataSnapshot.child("timestamp").getValue(Long.class);
                if (pulledData != null && stamp > pullStamp) {
                    pullPacket = pulledData;
                    pullPacketIterator = pullPacket.iterator();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.w("ReceiveError", "Error retrieving database information");
            }
        });
    }

    /**
     * Creates a new post thread for inputs
     */
    private void initPostThread()
    {
        Runnable postToDataBase = new Runnable()
        {
            @Override
            public void run()
            {
                writeDB(postPacket);
                //Log.d("Transmitting", Arrays.toString(postPacket.toArray()));
                postPacket = new ArrayList();
                handler.postDelayed(this, 3000);
            }
        };

        Runnable appendToPacket = new Runnable()
        {
            @Override
            public void run()
            {
                postPacket.add(activeTransmit);
                handler.postDelayed(this, 100);
            }
        };

        handler.post(postToDataBase);
        handler.post(appendToPacket);
    }

    /**
     * Creates a new pull thread for outputs
     */
    private void initPullThread()
    {
        Runnable readPullPacket = new Runnable()
        {
            @Override
            public void run()
            {
                if (pullPacketIterator.hasNext()) {
                    boolean active = (Boolean)pullPacketIterator.next();

                    if (active) {
                        long[] pattern = {0, 200, 0};
                        vibrator.vibrate(pattern, 0);
                    }
                    else {
                        vibrator.cancel();
                    }
                }
                handler.postDelayed(this, 100);
            }
        };
        handler.post(readPullPacket);
    }

    /**
     * Writes the parameter the database
     * @param write Parameter to be wrote to database
     */
    private void writeDB(ArrayList write)
    {
        transmitDB.child("values").setValue(write);
        transmitDB.child("timestamp").setValue(System.currentTimeMillis());
    }
}