package vdsMain;

import android.app.Activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Stack;

//aci
public class ActivityManager {
    //f230a
    private static Stack<Activity> activityStack;

    /* f231b */
    private static ActivityManager activityManager;

    // f232c
    private WeakReference<Activity> activityWeakReference;

    private ActivityManager() {
    }

    //m197a
    //915 m198a
    public static ActivityManager getInstance() {
        if (activityManager == null) {
            activityManager = new ActivityManager();
        }
        return activityManager;
    }

    //mo212a
    public void removeActivity(Activity activity) {
        if (activity != null) {
            Stack<Activity> stack = activityStack;
            if (stack != null) {
                if (stack.contains(activity)) {
                    activityStack.remove(activity);
                }
                if (!activityStack.empty()) {
                    this.activityWeakReference = new WeakReference<>(activityStack.lastElement());
                }
            }
        }
    }

    //mo215b
    public void removeAndFinishActivity(Activity activity) {
        if (activity != null) {
            activity.finish();
            if (activityStack.contains(activity)) {
                activityStack.remove(activity);
            }
        }
    }

    //mo214b
    public Activity getLastActivity() {
        Stack<Activity> stack = activityStack;
        if (stack == null || stack.empty()) {
            return null;
        }
        return (Activity) activityStack.lastElement();
    }

    //mo218c
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    //mo213a
    //915 mo226a
    public void removeAndFinishAll(Class cls) {
        Stack<Activity> stack = activityStack;
        if (stack != null && !stack.empty()) {
            for (Activity activity : new ArrayList<Activity>(activityStack)) {
                if (!activity.getClass().equals(cls)) {
                    removeAndFinishActivity(activity);
                }
            }
        }
    }

    //mo216b
    public void removeAllAndClearReference(Class cls) {
        Stack<Activity> stack = activityStack;
        if (stack != null && !stack.empty()) {
            for (Activity activity : new ArrayList<Activity>(activityStack)) {
                if (activity.getClass().equals(cls)) {
                    removeAndFinishActivity(activity);
                }
            }
            WeakReference<Activity> weakReference = this.activityWeakReference;
            if (weakReference != null && weakReference.get() != null && ((Activity) this.activityWeakReference.get()).getClass().equals(cls)) {
                this.activityWeakReference = null;
            }
        }
    }

    //mo217c
    //915 mo230c
    public void removeAll() {
        removeAndFinishAll((Class) null);
    }

    //mo219c
    public boolean hasClass(Class cls) {
        Stack<Activity> stack = activityStack;
        if (stack != null && !stack.empty()) {
            for (Activity activity : new ArrayList<Activity>(activityStack)) {
                if (activity.getClass().equals(cls)) {
                    return true;
                }
            }
        }
        return false;
    }
}
