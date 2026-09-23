package fuzs.nametagupgrade.common.data.client;

import fuzs.nametagupgrade.common.client.gui.components.FormattingGuideWidget;
import fuzs.nametagupgrade.common.client.gui.screens.inventory.NameTagEditScreen;
import fuzs.puzzleslib.common.api.client.data.v3.language.AbstractLanguageProvider;
import fuzs.puzzleslib.common.api.data.v3.core.DataProviderContext;
import net.minecraft.ChatFormatting;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addTranslations() {
        this.add(NameTagEditScreen.EDIT_NAME_TAG_KEY, "Edit %s");
        this.add(FormattingGuideWidget.CHAT_FORMATTING_FORMAT_KEY, "%s - %s");
        for (ChatFormatting chatFormatting : ChatFormatting.values()) {
            String chatFormattingName = FormattingGuideWidget.getChatFormattingName(chatFormatting);
            this.add(FormattingGuideWidget.getChatFormattingKey(chatFormatting), chatFormattingName);
        }
    }
}
