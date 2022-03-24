package hosp.model.enums;

import java.util.ArrayList;
import java.util.List;

public class TreatmentTime {

    private static List<String> timeList;

   static {
        timeList = new ArrayList<>();
        timeList.add("8:00");
        timeList.add("10:00");
        timeList.add("12:00");
        timeList.add("14:00");
        timeList.add("16:00");
        timeList.add("18:00");
        timeList.add("20:00");
        timeList.add("22:00");
    }

    public static List<String> getTimeList() {
        return timeList;
    }
}
