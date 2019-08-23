package me.nickac.survivalplus;

import com.google.inject.AbstractModule;
import me.nickac.survivalplus.customitems.internal.CustomBlock;
import me.nickac.survivalplus.customitems.internal.info.CustomItemInformation;
import me.nickac.survivalplus.customitems.internal.info.CustomItemInformationBuilder;
import me.nickac.survivalplus.data.impl.CustomItemData;
import me.nickac.survivalplus.data.impl.CustomItemInfoData;

public class SurvivalPlusModule extends AbstractModule {
    protected void configure() {
        requestStaticInjection(CustomBlock.class);
        requestStaticInjection(CustomItemInformation.class);
        requestStaticInjection(CustomItemData.Builder.class);
        requestStaticInjection(CustomItemInfoData.Builder.class);
        requestStaticInjection(CustomItemInformationBuilder.class);
    }
}
