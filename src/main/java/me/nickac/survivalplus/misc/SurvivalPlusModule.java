package me.nickac.survivalplus.misc;

import com.google.inject.AbstractModule;
import me.nickac.survivalplus.customitems.BlockDebuggerItem;
import me.nickac.survivalplus.customitems.EnergyMapInspectorItem;
import me.nickac.survivalplus.customitems.internal.CustomBlock;
import me.nickac.survivalplus.customitems.internal.CustomItem;
import me.nickac.survivalplus.customitems.internal.info.CustomItemInformation;
import me.nickac.survivalplus.customitems.internal.info.CustomItemInformationBuilder;
import me.nickac.survivalplus.energy.EnergyCircuit;
import me.nickac.survivalplus.misc.data.impl.CustomItemData;
import me.nickac.survivalplus.misc.data.impl.CustomItemInfoData;

public class SurvivalPlusModule extends AbstractModule {
    protected void configure() {
        requestStaticInjection(CustomItem.class);
        requestStaticInjection(CustomBlock.class);
        requestStaticInjection(EnergyCircuit.class);
        requestStaticInjection(CustomItemInformation.class);
        requestStaticInjection(CustomItemData.Builder.class);
        requestStaticInjection(CustomItemInfoData.Builder.class);
        requestStaticInjection(CustomItemInformationBuilder.class);
        requestStaticInjection(BlockDebuggerItem.class);
        requestStaticInjection(EnergyMapInspectorItem.class);
    }
}
