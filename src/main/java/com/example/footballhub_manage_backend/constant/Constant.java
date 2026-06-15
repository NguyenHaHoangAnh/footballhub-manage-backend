package com.example.footballhub_manage_backend.constant;

public class Constant {
    public static final class ApiService {
        public static final String PREFIX = "/api/v1/am";
    }

    public interface FORM_STATUS {
        String WON = "W";
        String DRAW = "D";
        String LOST = "L";
    }

    public interface POINT_STATUS {
        Integer WON = 3;
        Integer DRAW = 1;
        Integer LOST = 0;
    }

    public interface MATCH_STATUS {
        // TIMED (SCHEDULED) | LIVE | IN_PLAY | PAUSED | FINISHED | POSTPONED | SUSPENDED | CANCELLED
        String TIMED = "TIMED";
        String LIVE = "LIVE";
        String IN_PLAY = "IN_PLAY";
        String PAUSE = "PAUSE";
        String FINISHED = "FINISHED";
        String POSTPONED = "POSTPONED";
        String SUSPENDED = "SUSPENDED";
        String CANCELLED = "CANCELLED";
    }
}
