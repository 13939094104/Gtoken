package vdsMain;

import java.util.Calendar;
import java.util.Formatter;

public final class DateTimeToolkit {
    /* renamed from: a */
    //m11515a
    public static final String getCurrentTimeForamtDefaultCalendar() {
        return getCurrentTimeFormat(null);
    }

    //m11516a
    public static final String getCurrentTimeFormat(Calendar calendar) {
        if (calendar == null) {
            calendar = Calendar.getInstance();
        }
        int i = calendar.get(1);
        int i2 = calendar.get(2) + 1;
        int i3 = calendar.get(5);
        Formatter formatter = new Formatter();
        formatter.format("%d-%02d-%02d", new Object[]{Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3)});
        return formatter.toString();
    }
}
