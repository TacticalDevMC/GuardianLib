package net.rubixdevelopment.guardianlibPlugin.impl;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.rubixdevelopment.guardianlibApi.interfaces.Text;

public class TextImpl implements Text {
    private static final LegacyComponentSerializer LEG = LegacyComponentSerializer.legacyAmpersand();
    @Override public String color(String legacy) { return LEG.serialize(LEG.deserialize(legacy)); }
    @Override public Component component(String legacy) { return LEG.deserialize(legacy); }
}