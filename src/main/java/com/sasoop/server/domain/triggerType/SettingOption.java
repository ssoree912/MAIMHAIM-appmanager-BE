package com.sasoop.server.domain.triggerType;

import lombok.Getter;
import lombok.Setter;

public class SettingOption {

    @Getter
    @Setter
    public static class LocationSettings{
        boolean run;
        boolean popUp;
    }

    @Getter
    @Setter
    public static class MotionSettings{
        boolean shaker;
        boolean popUp;
    }

    @Getter
    @Setter
    public static class TimeSettings{
        private String time;
    }

}
