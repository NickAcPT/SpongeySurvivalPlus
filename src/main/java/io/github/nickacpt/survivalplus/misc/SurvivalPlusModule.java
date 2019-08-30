package io.github.nickacpt.survivalplus.misc;

import com.google.inject.AbstractModule;
import io.github.nickacpt.survivalplus.customitems.BlockDebuggerItem;
import io.github.nickacpt.survivalplus.customitems.EnergyMapInspectorItem;
import io.github.nickacpt.survivalplus.customitems.internal.CustomItem;
import io.github.nickacpt.survivalplus.misc.data.impl.CustomItemData;
import io.github.nickacpt.survivalplus.misc.data.impl.CustomItemInfoData;
import io.github.nickacpt.survivalplus.customitems.internal.CustomBlock;
import io.github.nickacpt.survivalplus.customitems.internal.info.CustomItemInformation;
import io.github.nickacpt.survivalplus.customitems.internal.info.CustomItemInformationBuilder;
import io.github.nickacpt.survivalplus.energy.EnergyCircuit;

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
