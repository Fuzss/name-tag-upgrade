package fuzs.nametagupgrade.data.client;

import fuzs.nametagupgrade.client.gui.components.FormattingGuideWidget;
import fuzs.nametagupgrade.client.gui.screens.inventory.NameTagEditScreen;
import fuzs.puzzleslib.api.client.data.v2.AbstractLanguageProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.ChatFormatting;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addTranslations(TranslationBuilder builder) {
        builder.add(NameTagEditScreen.EDIT_NAME_TAG_KEY, "Edit %s");
        builder.add(FormattingGuideWidget.CHAT_FORMATTING_FORMAT_KEY, "§%s - %s");
        for (ChatFormatting chatFormatting : ChatFormatting.values()) {
            String chatFormattingName = FormattingGuideWidget.getChatFormattingName(chatFormatting);
            builder.add(FormattingGuideWidget.getChatFormattingKey(chatFormatting), chatFormattingName);
        }
    }
}
