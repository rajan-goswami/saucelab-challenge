package com.example.saucelabchallenge;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in a service on a
 * separate handler thread.
 */
public class SolutionService extends IntentService {

  private static final String TAG = SolutionService.class.getSimpleName();

  private static final String ACTION_PRINT_SERVICES =
      "com.example.saucelabchallenge.action.PRINT_SERVICES";
  private static final String ACTION_CHECK_BATTERY_STATUS =
      "com.example.saucelabchallenge.action.CHECK_BATTER_STATUS";

  public SolutionService() {
    super(TAG);
  }

  /**
   * Starts this service to perform action PRINT_SERVICES with the given parameters. If the service
   * is already performing a task this action will be queued.
   *
   * @see IntentService
   */
  public static void startActionPrintServices(Context context) {
    Intent intent = new Intent(context, SolutionService.class);
    intent.setAction(ACTION_PRINT_SERVICES);
    context.startService(intent);
  }

  /**
   * Starts this service to perform action CHECK_BATTER_STATUS with the given parameters. If the
   * service is already performing a task this action will be queued.
   *
   * @see IntentService
   */
  public static void startActionCheckBatteryStatus(Context context) {
    Intent intent = new Intent(context, SolutionService.class);
    intent.setAction(ACTION_CHECK_BATTERY_STATUS);
    context.startService(intent);
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    if (intent != null) {
      final String action = intent.getAction();
      if (ACTION_PRINT_SERVICES.equals(action)) {
        handleActionPrintServices();
      } else if (ACTION_CHECK_BATTERY_STATUS.equals(action)) {
        handleActionCheckBatteryStatus();
      }
    }
  }

  /** Print all available services from ServiceManager system class */
  private void handleActionPrintServices() {
    Field[] declaredFields = Context.class.getDeclaredFields();
    for (Field field : declaredFields) {
      if (java.lang.reflect.Modifier.isStatic(field.getModifiers())
          && field.getType().equals(String.class)) {
        try {
          String serviceName = (String) field.get(String.class);
          boolean available = checkIfServiceAvailable(serviceName);
          if (available) {
            Log.d(TAG, "Service:" + serviceName + " is available");
          }
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private boolean checkIfServiceAvailable(String serviceName) {
    boolean available = false;
    try {
      Class serviceManagerClass = Class.forName("android.os.ServiceManager");
      Method getServiceMethod =
          serviceManagerClass.getDeclaredMethod("getService", new Class[] {String.class});
      if (getServiceMethod != null) {
        IBinder binder =
            (IBinder) getServiceMethod.invoke(serviceManagerClass, new Object[] {serviceName});
        if (binder != null) {
          available = true;
        }
      }
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
    return available;
  }

  /** Check battery status */
  private void handleActionCheckBatteryStatus() {
    try {
      Class serviceManagerClass = Class.forName("android.os.ServiceManager");
      Method getServiceMethod =
          serviceManagerClass.getDeclaredMethod("getService", new Class[] {String.class});
      if (getServiceMethod != null) {
        IBinder binder =
            (IBinder)
                getServiceMethod.invoke(
                    serviceManagerClass, new Object[] {Context.BATTERY_SERVICE});
        if (binder != null) {
          IBatteryStats iBatteryStats = IBatteryStats.Stub.asInterface(binder);
          Log.d(
              TAG,
              "Battery status is " + (iBatteryStats.isCharging() ? "CHARGING" : "NOT-CHARGING"));
        }
      }
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }
}
