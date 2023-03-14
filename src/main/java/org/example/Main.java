package org.example;



import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;

import java.util.List;

public class Main {

    public static void main(String[] args){

        GsonParser parser = new GsonParser();

        Root root = parser.parser();

        ArrayList<Integer> array = new ArrayList<>();

        for (Ticket tickets : root.getTicket()){
            array.add(flightTime(tickets));
        }

        System.out.println("average flight time from Vladivostok to Tel Aviv " + Duration.ofMinutes(averageValue(array)));
        System.out.println("90s percentile flight time from Vladivostok to Tel Aviv " + Duration.ofMinutes(percentile(array, 90.0)));
    }

    public static int averageValue(List<Integer> array){
        int value = array.stream().mapToInt(a -> a).sum()/array.size();
        return  value;
    }

    public static int percentile(List<Integer> array, double percentile) {
        Collections.sort(array);
        int index = (int) Math.ceil(percentile / 100.0 * array.size());
        return array.get(index - 1);
    }

    public static int flightTime (Ticket tickets){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern( "dd.MM.uu H:mm" );

        String DeptInput = tickets.getDeparture_date() + " " + tickets.getDeparture_time();
        LocalDateTime localDateTime = LocalDateTime.parse(DeptInput , dateTimeFormatter);
        ZoneId zoneId = ZoneId.of("Asia/Vladivostok");
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);

        String ArrInput = tickets.getArrival_date() + " " + tickets.getArrival_time();
        LocalDateTime localDateTimeArr = LocalDateTime.parse(ArrInput , dateTimeFormatter);
        ZoneId zoneIdArr = ZoneId.of("Etc/GMT-3");
        ZonedDateTime zonedDateTimeArr = localDateTimeArr.atZone(zoneIdArr);

        return (int) ChronoUnit.MINUTES.between(zonedDateTime, zonedDateTimeArr);
    }

    }



