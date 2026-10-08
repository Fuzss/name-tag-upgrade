package fuzs.nametagupgrade.common.util;

import fuzs.nametagupgrade.common.NameTagUpgrade;
import fuzs.nametagupgrade.common.config.ServerConfig;
import fuzs.puzzleslib.api.client.input.v1.CharacterEvent;
import fuzs.puzzleslib.api.util.v1.StyleCombiningCharSink;
import net.minecraft.network.chat.*;
import net.minecraft.util.StringDecomposer;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.Objects;

/**
 * @see StringUtil
 */
public class FormattedStringUtil {
    /**
     * A custom style for merging via {@link ComponentUtils#mergeStyles(MutableComponent, Style)} that preserves the
     * style set by the player without any visual changes like italics being applied by vanilla.
     *
     * @see ItemStack#getTooltipLines(Item.TooltipContext, Player, TooltipFlag)
     */
    public static final Style EMPTY = Style.EMPTY.withBold(false)
            .withItalic(false)
            .withUnderlined(false)
            .withStrikethrough(false)
            .withObfuscated(false);

    /**
     * @see CharacterEvent#isAllowedChatCharacter()
     */
    public static boolean isAllowedChatCharacter(CharacterEvent event) {
        if (!NameTagUpgrade.CONFIG.get(ServerConfig.class).renamingSupportsFormatting) {
            return event.isAllowedChatCharacter();
        }

        return isAllowedChatCharacter(event.codepoint());
    }

    /**
     * @see StringUtil#isAllowedChatCharacter(char)
     */
    public static boolean isAllowedChatCharacter(int character) {
        if (!NameTagUpgrade.CONFIG.get(ServerConfig.class).renamingSupportsFormatting) {
            return StringUtil.isAllowedChatCharacter((char) character);
        }

        return StringUtil.isAllowedChatCharacter((char) character) || character == '§';
    }

    /**
     * @see StringUtil#filterText(String)
     */
    public static String filterText(String text) {
        if (!NameTagUpgrade.CONFIG.get(ServerConfig.class).renamingSupportsFormatting) {
            return StringUtil.filterText(text);
        }

        return filterText(text, false);
    }

    /**
     * @see StringUtil#filterText(String, boolean)
     */
    public static String filterText(String text, boolean keepLinesBreaks) {
        if (!NameTagUpgrade.CONFIG.get(ServerConfig.class).renamingSupportsFormatting) {
            return StringUtil.filterText(text, keepLinesBreaks);
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (char character : text.toCharArray()) {
            if (isAllowedChatCharacter(character)) {
                stringBuilder.append(character);
            } else if (keepLinesBreaks && character == '\n') {
                stringBuilder.append(character);
            }
        }

        return stringBuilder.toString();
    }

    /**
     * @see fuzs.puzzleslib.api.util.v1.ComponentHelper#getAsComponent(String)
     */
    public static Component getAsComponent(String text) {
        Objects.requireNonNull(text, "text is null");
        if (!NameTagUpgrade.CONFIG.get(ServerConfig.class).renamingSupportsFormatting) {
            return Component.literal(text);
        }

        return StyleCombiningCharSink.of(text, EMPTY).getAsComponent();
    }

    /**
     * @see fuzs.puzzleslib.api.util.v1.ComponentHelper#getAsString(FormattedText)
     */
    public static String getAsString(Component component) {
        Objects.requireNonNull(component, "component is null");
        if (!NameTagUpgrade.CONFIG.get(ServerConfig.class).renamingSupportsFormatting) {
            return component.getString();
        }

        return StyleCombiningCharSink.of(component, Style.EMPTY).getAsString();
    }

    /**
     * @see String#length()
     */
    public static int stringLength(String text) {
        Objects.requireNonNull(text, "text is null");
        if (!NameTagUpgrade.CONFIG.get(ServerConfig.class).renamingSupportsFormatting) {
            return text.length();
        }

        return StyleCombiningCharSink.of(text, Style.EMPTY).length();
    }

    /**
     * @see String#substring(int)
     */
    public static String substring(String text, int startIndex) {
        Objects.requireNonNull(text, "text is null");
        if (!NameTagUpgrade.CONFIG.get(ServerConfig.class).renamingSupportsFormatting) {
            return text.substring(startIndex);
        }

        return substring(text, startIndex, text.length());
    }

    /**
     * @see String#substring(int, int)
     */
    public static String substring(String text, int startIndex, int endIndex) {
        Objects.requireNonNull(text, "text is null");
        if (!NameTagUpgrade.CONFIG.get(ServerConfig.class).renamingSupportsFormatting) {
            return text.substring(startIndex, endIndex);
        }

        StyleCombiningCharSink styleCombiningCharSink = new StyleCombiningCharSink(Style.EMPTY) {
            @Override
            public boolean accept(int position, Style style, int codePoint) {
                return this.length() < startIndex || this.length() < endIndex && super.accept(position,
                        style,
                        codePoint);
            }
        };
        StringDecomposer.iterateFormatted(FormattedText.of(text), Style.EMPTY, styleCombiningCharSink);
        return styleCombiningCharSink.getAsString();
    }
}
