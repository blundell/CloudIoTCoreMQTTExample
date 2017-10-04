# Cloud IoT Core MQTT Communication Example

Found it hard to understand the docs here: https://cloud.google.com/iot/docs/how-tos/mqtt-bridge and no examples online, so wrote my own.

Using MQTT to talk between an AndroidThings board and Cloud IoT Core Pub/Sub


Based on the example here:

https://github.com/GoogleCloudPlatform/java-docs-samples/blob/master/iot/api-client/mqtt_example/src/main/java/com/google/cloud/iot/examples/MqttExample.java

but written for AndroidThings instead of Java. Also tried to keep it simpler and separated. 

See [MainActivity.java](https://github.com/blundell/CloudIoTCoreMQTTExample/blob/master/app/src/main/java/com/blundell/tut/MainActivity.java) for use.

You connect like this

```
communicator = new IotCoreCommunicator.Builder()
        .withContext(YourActivity.this)
        .withCloudRegion("your-region") // ex: europe-west1
        .withProjectId("your-project-id")   // ex: supercoolproject23236
        .withRegistryId("your-registry-id") // ex: my-devices
        .withDeviceId("a-device-id") // ex: my-test-raspberry-pi
        .withPrivateKeyRawFileId(R.raw.rsa_private)
        .build();
communicator.connect();
```
        
and publish a message like this:

```
communicator.publishMessage("subtopic", "Hello World");
```

finally when finished:

```
communicator.disconnect();
```
        
        
