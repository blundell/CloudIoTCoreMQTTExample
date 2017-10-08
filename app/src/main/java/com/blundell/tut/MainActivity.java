package com.blundell.tut;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;

import com.blundell.iotcore.IotCoreCommunicator;

import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity {

    private IotCoreCommunicator communicator;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup the communication with your Google IoT Core details
        communicator = new IotCoreCommunicator.Builder()
                .withContext(this)
                .withCloudRegion("your-region") // ex: europe-west1
                .withProjectId("your-project-id")   // ex: supercoolproject23236
                .withRegistryId("your-registry-id") // ex: my-devices
                .withDeviceId("a-device-id") // ex: my-test-raspberry-pi
                .withPrivateKeyRawFileId(R.raw.rsa_private)
                .build();

        HandlerThread thread = new HandlerThread("MyBackgroundThread");
        thread.start();
        handler = new Handler(thread.getLooper());
        handler.post(connectOffTheMainThread); // Use whatever threading mechanism you want
    }

    private final Runnable connectOffTheMainThread = new Runnable() {
        @Override
        public void run() {
            communicator.connect();

            handler.post(sendMqttMessage);
        }
    };

    private final Runnable sendMqttMessage = new Runnable() {
        private int i;

        /**
         * We post 100 messages as an example, 1 a second
         */
        @Override
        public void run() {
            if (i == 100) {
                return;
            }

            // events is the default topic for MQTT communication
            String subtopic = "events";
            // Your message you want to send
            String message = "Hello World " + i++;
            communicator.publishMessage(subtopic, message);

            handler.postDelayed(this, TimeUnit.SECONDS.toMillis(1));
        }
    };

    @Override
    protected void onDestroy() {
        communicator.disconnect();
        super.onDestroy();
    }
}
