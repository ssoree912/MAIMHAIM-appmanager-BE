package com.sasoop.server.controller.dto.response;

import com.sasoop.server.domain.appTrigger.AppTrigger;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class TriggerResponse {

    @Getter
    public static class AppTriggers{
        private AppResponse.AppInfo app;
        private List<Trigger> triggers;

        public AppTriggers(AppResponse.AppInfo app, List<Trigger> triggers) {
            this.app = app;
            this.triggers = triggers;
        }
    }
    @Getter
    public static class Trigger<T>{
        private Long triggerId;
        private boolean activate;
        private String name;
        private String url;
        private boolean foreGround;
        private T triggerValue;


        public Trigger(AppTrigger appTrigger, T triggerValue) {
            this.triggerId = appTrigger.getTriggerId();
            this.activate = appTrigger.isActivate();
            this.name = appTrigger.getName();
            this.foreGround = appTrigger.isForeGround();
            this.triggerValue = triggerValue;
        }

    }
    public static Trigger of(AppTrigger appTrigger, Object triggerValue) {
        return new Trigger(appTrigger, triggerValue);
    }


}
