package me.nickac.survivalplus;

import com.google.inject.AbstractModule;
import me.nickac.survivalplus.custom.items.CustomItemInformation;

public class SurvivalPlusModule extends AbstractModule {
    protected void configure() {
        requestStaticInjection(CustomItemInformation.class);
        requestStaticInjection(CustomItemInformation.Builder.class);
    }
}
