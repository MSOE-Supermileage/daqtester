package smv.msoe.edu.daqtester;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends AppCompatActivity {

    Thread t = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        final TextView console = (TextView) findViewById(R.id.console);
        final CheckBox parseCheckbox = (CheckBox) findViewById(R.id.parseCheckbox);
        final Button clearButton = (Button) findViewById(R.id.clear);
        assert scrollView != null;
        assert console != null;
        assert parseCheckbox != null;
        assert clearButton != null;

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                console.setText(R.string.cleared);
            }
        });

        t = new Thread(new Runnable() {

            public void consoleWrite(final String message) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        console.setText(console.getText() + message);
                        scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }

            public void consoleWriteLine(final String message) {
                consoleWrite(message + "\n");
            }

            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    consoleWriteLine("connecting...");
                    try (ServerSocket waitSocket = new ServerSocket(5001)) {
                        // set a second timeout in case we can't connect
//                        waitSocket.setSoTimeout(1000);

                        // blocking call
                        Socket dataCollector = waitSocket.accept();
                        // start with a 1 second timeout, we may need to play with this
                        dataCollector.setSoTimeout(1000);

                        consoleWriteLine("connected.");

                        while (!Thread.currentThread().isInterrupted()) {
                            if (parseCheckbox.isChecked()) {
                                final Bundle dataNode;
                                // blocking until receive
                                JsonReader reader;
                                reader = new JsonReader(new InputStreamReader(dataCollector.getInputStream()));
                                // service doesn't care about which car or what's in the data node,
                                // let the client controller determine how to deal with the data.
                                dataNode = Utility.parseDataNode(reader);
                                consoleWriteLine(dataNode.toString());
                            } else {
                                String line = new BufferedReader(new InputStreamReader(dataCollector.getInputStream())).readLine();
                                // buffered reader returns null if we don't get anything back...
                                if (line != null) {
                                    consoleWrite(line);
                                } else {
                                    throw new IOException("need to reconnect...");
                                }
                            }
                        }
                    } catch (IOException e) {
                        consoleWriteLine(e.getMessage());
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e1) {
                            // do nothing
                        }

                    }
                }
            }
        });
        t.start();
    }

    @Override
    public void onStop() {
        if (t != null) {
            t.interrupt();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        super.onStop();
    }
}
