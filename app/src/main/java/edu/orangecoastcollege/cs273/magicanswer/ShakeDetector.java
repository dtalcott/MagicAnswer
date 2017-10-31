package edu.orangecoastcollege.cs273.magicanswer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


public class ShakeDetector implements SensorEventListener
{
    private static final long ELAPSED_TIME = 1000L;
    //Accelerometer Data uses float
    private static final float THRESHOLD = 20;
    private long previousShake;
    private OnShakeListener mListener;

    public ShakeDetector (OnShakeListener listener)
    {
        mListener = listener;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent)
    {
        //Ignore all other events, only accept accelerometer events
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            //neutralize the effect of gravity
            float gForceX = x - SensorManager.GRAVITY_EARTH;
            float gForceY = y - SensorManager.GRAVITY_EARTH;
            float gForceZ = z - SensorManager.GRAVITY_EARTH;

            float netForce = (float) Math.sqrt(Math.pow(gForceX, 2) + Math.pow(gForceY, 2) + Math.pow(gForceZ, 2));
            if (netForce >= THRESHOLD)
            {
                //get current time
                long currentTime = System.currentTimeMillis();
                if (currentTime >= (previousShake + ELAPSED_TIME))
                {
                    //reset previous shake to current
                    previousShake = currentTime;

                    //register a shake event (it's a SHAKE)
                    mListener.onShake();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i)
    {
        //do nothing, not being used
    }

    //Define an interface for others to implement whenever a true shake occurs
    //Interface = contract (method declaration without implementation)
    //some other class has to implement the method.

    public interface OnShakeListener
    {
        void onShake();
    }
}
