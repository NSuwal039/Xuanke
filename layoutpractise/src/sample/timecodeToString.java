package sample;

public class timecodeToString {
    public static String timecodeToString(int i){
        String thisvalue;
        String day;
        String period;
        int j =i-1;

        switch (j%14){
            case 0: period = "08:00 - 08:45";break;
            case 1: period = "08:55 - 09:40";break;
            case 2: period = "09:55 - 10:40";break;
            case 3: period = "10:50 - 11:35";break;
            case 4: period = "11:45 - 12:30";break;
            case 5: period = "13:30 - 14:15";break;
            case 6: period = "14:25 - 15:10";break;
            case 7: period = "15:20 - 16:05";break;
            case 8: period = "16:15 - 17:00";break;
            case 9: period = "17:10 - 17:55";break;
            case 10: period = "18:30 - 19:15";break;
            case 11: period = "19:25 - 20:10";break;
            case 12: period = "20:20 - 21:05";break;
            case 13: period = "21:15 - 22:00";break;
            default:
                throw new IllegalStateException("Unexpected value: " + j % 14);
        }

        switch (j/14){
            case 1: day ="Mon"; break;
            case 2: day ="Tue"; break;
            case 3: day ="Wed"; break;
            case 4: day ="Thu"; break;
            case 5: day ="Fri"; break;
            case 6: day ="Sat"; break;
            case 0: day ="Sun"; break;
            default:
                throw new IllegalStateException("Unexpected value: " + j / 14);
        }
        thisvalue = day + " " + period;
        return  thisvalue;
    }
}
