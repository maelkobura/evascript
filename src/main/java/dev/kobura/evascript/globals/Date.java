package dev.kobura.evascript.globals;

import dev.kobura.evascript.runtime.context.Scriptable;

import java.util.Calendar;

@Scriptable(defaultName = "date")
public class Date {

    @Scriptable
    public static class DateObject {
        private long timestamp;

        public DateObject() {
            timestamp = System.currentTimeMillis();
        }

        public DateObject(long timestamp) {
            this.timestamp = timestamp;
        }

        @Scriptable
        public long getTimestamp() {
            return timestamp;
        }

        @Scriptable
        public long getDay() {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timestamp);
            return calendar.get(Calendar.DAY_OF_MONTH);
        }

        @Override
        public String toString() {
            return String.valueOf(timestamp);
        }
    }

    @Scriptable
    public DateObject now() {
        return new DateObject();
    }

    @Scriptable
    public DateObject create(long timestamp) {
        return new DateObject(timestamp);
    }



}
