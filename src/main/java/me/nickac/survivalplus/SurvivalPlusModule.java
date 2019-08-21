package me.nickac.survivalplus;

import com.google.inject.AbstractModule;
import me.nickac.survivalplus.customitems.internal.CustomItemInformation;

public class SurvivalPlusModule extends AbstractModule {
    protected void configure() {
        requestStaticInjection(CustomItemInformation.class);
    }
}
