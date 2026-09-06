package fuzs.nametagupgrade.common.client.gui.components;

import fuzs.nametagupgrade.common.client.util.FormattedStringSplitter;
import fuzs.nametagupgrade.common.client.util.LengthLimitedCharSink;
import fuzs.nametagupgrade.common.util.FormattedStringDecomposer;
import fuzs.nametagupgrade.common.util.FormattedStringUtil;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.FormattedCharSink;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * An extension to {@link EditBox} that supports {@link net.minecraft.ChatFormatting} by allowing '§' to be used.
 */
public class FormattableEditBox extends EditBox {

    public FormattableEditBox(Font font, int x, int y, int width, int height, Component narration) {
        this(font, x, y, width, height, null, narration);
    }

    public FormattableEditBox(Font font, int x, int y, int width, int height, @Nullable EditBox oldBox, Component narration) {
        super(font, x, y, width, height, oldBox, narration);
        // custom formatter for applying formatting codes directly to the text preview
        this.setFormatter((String displayText, Integer displayPos) -> {
            List<FormattedCharSequence> list = new ArrayList<>();
            FormattedCharSink sink = new LengthLimitedCharSink(displayText.length(), displayPos);
            // We apply the format to the whole value.
            // We need the formatting to apply correctly and not get interrupted by the cursor being placed in between a formatting code.
            FormattedStringDecomposer.iterateFormatted(this.value,
                    Style.EMPTY,
                    (int position, Style style, int codePoint) -> {
                        if (sink.accept(position, style, codePoint)) {
                            list.add((FormattedCharSink formattedCharSink) -> formattedCharSink.accept(position,
                                    style,
                                    codePoint));
                        }

                        return true;
                    });
            return FormattedCharSequence.composite(list);
        });
    }

    @Override
    public void setValue(String value) {
        // Custom text length handling so we ignore formatting codes.
        if (FormattedStringUtil.stringLength(value) > this.maxLength) {
            this.value = FormattedStringUtil.substring(value, 0, this.maxLength);
        } else {
            this.value = value;
        }

        this.moveCursorToEnd(false);
        this.setHighlightPos(this.cursorPos);
        this.onValueChange(value);
    }

    @Override
    public void insertText(String input) {
        int start = Math.min(this.cursorPos, this.highlightPos);
        int end = Math.max(this.cursorPos, this.highlightPos);
        String string = FormattedStringUtil.filterText(input);
        // Delete the selected character range from the current value.
        StringBuilder builder = new StringBuilder(this.value).replace(start, end, "");
        String updatedValue = builder.toString();
        // Insert new characters one by one, checking after each if the value is still below the max allowed length.
        int insertionLength = 0;
        for (; insertionLength < string.length(); insertionLength++) {
            char character = string.charAt(insertionLength);
            // Special handling for surrogate pairs as done in the vanilla super method.
            if (Character.isHighSurrogate(character)) {
                if (insertionLength + 1 < string.length()) {
                    builder.insert(start + insertionLength, character);
                    insertionLength++;
                    builder.insert(start + insertionLength, string.charAt(insertionLength));
                } else {
                    break;
                }
            } else {
                builder.insert(start + insertionLength, character);
            }

            if (FormattedStringUtil.stringLength(builder.toString()) <= this.maxLength) {
                updatedValue = builder.toString();
            } else {
                break;
            }
        }

        this.value = updatedValue;
        this.setCursorPosition(start + insertionLength);
        this.setHighlightPos(this.cursorPos);
        this.onValueChange(this.value);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (!this.canConsumeInput()) {
            return false;
        } else if (FormattedStringUtil.isAllowedChatCharacter(codePoint)) {
            // Custom text length handling so we ignore formatting codes.
            if (this.isEditable) {
                this.insertText(Character.toString(codePoint));
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        int positionInText = Mth.clamp(Mth.floor(mouseX) - this.textX(), 0, this.getInnerWidth());
        int clickedPositionInText = FormattedStringSplitter.plainIndexAtWidth(this.font.getSplitter(),
                this.value,
                positionInText,
                this.displayPos);
        this.moveCursorTo(clickedPositionInText, Screen.hasShiftDown());
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        if (this.isVisible()) {
            if (this.isBordered()) {
                ResourceLocation sprite = SPRITES.get(this.isActive(), this.isFocused());
                graphics.blitSprite(sprite, this.getX(), this.getY(), this.getWidth(), this.getHeight());
            }

            int color = this.isEditable ? this.textColor : this.textColorUneditable;
            int relCursorPos = this.cursorPos - this.displayPos;
            String displayed = FormattedStringSplitter.plainSubstrByWidth(this.font.getSplitter(),
                    this.value,
                    this.getInnerWidth(),
                    this.displayPos);
            boolean cursorOnScreen = relCursorPos >= 0 && relCursorPos <= displayed.length();
            boolean showCursor =
                    this.isFocused() && (Util.getMillis() - this.focusedTime) / 300L % 2L == 0L && cursorOnScreen;
            int drawX = this.textX();
            int relHighlightPos = Mth.clamp(this.highlightPos - this.displayPos, 0, displayed.length());
            if (!displayed.isEmpty()) {
                String half = cursorOnScreen ? displayed.substring(0, relCursorPos) : displayed;
                FormattedCharSequence charSequence = this.formatter.apply(half, this.displayPos);
                graphics.drawString(this.font, charSequence, drawX, this.textY(), color);
                drawX += this.font.width(charSequence) + 1;
            }

            boolean insert = this.cursorPos < this.value.length()
                    || FormattedStringUtil.stringLength(this.value) >= this.getMaxLength();
            int cursorX = drawX;
            if (!cursorOnScreen) {
                cursorX = relCursorPos > 0 ? this.textX() + this.width : this.textX();
            } else if (insert) {
                cursorX = drawX - 1;
                drawX--;
            }

            if (!displayed.isEmpty() && cursorOnScreen && relCursorPos < displayed.length()) {
                graphics.drawString(this.font,
                        this.formatter.apply(displayed.substring(relCursorPos), this.cursorPos),
                        drawX,
                        this.textY(),
                        color);
            }

            if (this.hint != null && displayed.isEmpty() && !this.isFocused()) {
                graphics.drawString(this.font, this.hint, drawX, this.textY(), color);
            }

            if (!insert && this.suggestion != null) {
                graphics.drawString(this.font, this.suggestion, cursorX - 1, this.textY(), -8355712);
            }

            if (relHighlightPos != relCursorPos) {
                int highlightPos = this.displayPos + relHighlightPos;
                int highlightX = this.textX() + FormattedStringSplitter.width(this.font.getSplitter(),
                        this.value,
                        this.displayPos,
                        highlightPos);
                graphics.fill(RenderType.guiTextHighlight(),
                        Math.min(cursorX, this.getX() + this.width),
                        this.textY() - 1,
                        Math.min(highlightX - 1, this.getX() + this.width),
                        this.textY() + 1 + 9,
                        0XFF0000FF);
            }

            if (showCursor) {
                if (insert) {
                    graphics.fill(cursorX, this.textY() - 1, cursorX + 1, this.textY() + 1 + 9, color);
                } else {
                    graphics.drawString(this.font, "_", cursorX, this.textY(), color);
                }
            }
        }
    }

    private int textY() {
        return this.bordered ? this.getY() + (this.height - 8) / 2 : this.getY();
    }

    private int textX() {
        return this.getX() + (this.bordered ? 4 : 0);
    }

    @Override
    public void scrollTo(int pos) {
        if (this.font != null) {
            this.displayPos = Math.min(this.displayPos, this.value.length());
            int innerWidth = this.getInnerWidth();
            String displayed = FormattedStringSplitter.plainSubstrByWidth(this.font.getSplitter(),
                    this.value,
                    innerWidth,
                    this.displayPos);
            int lastPos = displayed.length() + this.displayPos;
            if (pos == this.displayPos) {
                this.displayPos -= FormattedStringSplitter.plainSubstrByWidth(this.font.getSplitter(),
                        this.value,
                        innerWidth,
                        true).length();
            }

            if (pos > lastPos) {
                this.displayPos += pos - lastPos;
            } else if (pos <= this.displayPos) {
                this.displayPos -= this.displayPos - pos;
            }

            this.displayPos = Mth.clamp(this.displayPos, 0, this.value.length());
        }
    }

    @Override
    public int getScreenX(int charIndex) {
        return charIndex > this.value.length() ? this.getX() :
                this.getX() + FormattedStringSplitter.width(this.font.getSplitter(),
                        this.value.substring(0, charIndex));
    }
}
