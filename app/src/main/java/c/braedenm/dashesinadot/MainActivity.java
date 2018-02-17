package c.braedenm.dashesinadot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Links with the activity_main and controls its activities
 */
public class MainActivity extends AppCompatActivity
{
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference("test");

    /**
     * This is called when the application laods activity_main
     * @param savedInstanceState Previous instance of the program
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reference.setValue("quack");
    }
}
