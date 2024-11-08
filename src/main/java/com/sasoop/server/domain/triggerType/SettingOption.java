package com.sasoop.server.domain.triggerType;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class SettingOption {

    LocationSettings locationSettings;
    TimeSettings timeSettings;

    public SettingOption(LocationSettings locationSettings) {
        this.locationSettings = locationSettings;
    }


    public SettingOption(TimeSettings timeSettings) {
        this.timeSettings = timeSettings;
    }
    @Getter
    @Setter
    public static class LocationSettings{

        boolean run;
        boolean popUp;
        List<String> locations;
    }


    @Getter
    @Setter
    public static class TimeSettings{
        private String time;
    }

    public static SettingOption of(LocationSettings locationSettings) {
        return new SettingOption(locationSettings);
    }

    public static SettingOption of(TimeSettings timeSettings) {
        return new SettingOption(timeSettings);
    }

}
