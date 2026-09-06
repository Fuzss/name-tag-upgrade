package fuzs.nametagupgrade.common.client.gui.screens.inventory;

import fuzs.nametagupgrade.common.NameTagUpgrade;
import fuzs.nametagupgrade.common.client.gui.components.FormattableEditBox;
import fuzs.nametagupgrade.common.client.gui.components.FormattingGuideWidget;
import fuzs.nametagupgrade.common.config.ServerConfig;
import fuzs.nametagupgrade.common.network.client.ServerboundEditNameTagMessage;
import fuzs.puzzleslib.api.network.v4.MessageSender;
import fuzs.puzzleslib.api.util.v1.ComponentHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class NameTagEditScreen extends Screen {
    private static final ResourceLocation TEXTURE_LOCATION = NameTagUpgrade.id("textures/gui/edit_name_tag.png");
    private static final ResourceLocation TEXT_FIELD_SPRITE = ResourceLocation.withDefaultNamespace(
            "container/anvil/text_field");
    private static final ResourceLocation TEXT_FIELD_DISABLED_SPRITE = ResourceLocation.withDefaultNamespace(
            "container/anvil/text_field_disabled");
    public static final String EDIT_NAME_TAG_KEY = NameTagUpgrade.id("name_tag").toLanguageKey("gui", "edit");

    private final int imageWidth = 176;
    private final int imageHeight = 48;
    private int leftPos;
    private int topPos;
    private final int titleLabelX = 60;
    private final int titleLabelY = 8;
    private final InteractionHand interactionHand;
    private final String initialItemName;
    private EditBox name;

    public NameTagEditScreen(ItemStack itemStack, InteractionHand interactionHand) {
        super(Component.translatable(EDIT_NAME_TAG_KEY, itemStack.getItem().getName(itemStack)));
        this.interactionHand = interactionHand;
        this.initialItemName = ComponentHelper.getAsString(itemStack.getHoverName());
    }

    @Override
    protected void init() {
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = this.height / 4;
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (Button button) -> {
            MessageSender.broadcast(new ServerboundEditNameTagMessage(this.interactionHand, this.name.getValue()));
            this.onClose();
        }).bounds(this.width / 2 - 100, this.height / 4 + 120, 200, 20).build());
        this.name = this.createEditBox(this.font, this.leftPos + 62, this.topPos + 26, 103, 12, this.title);
        this.name.setCanLoseFocus(false);
        this.name.setTextColor(-1);
        this.name.setTextColorUneditable(-1);
        this.name.setBordered(false);
        this.name.setMaxLength(50);
        this.name.setValue(this.initialItemName);
        this.addRenderableWidget(this.name);
        this.setInitialFocus(this.name);
        if (NameTagUpgrade.CONFIG.get(ServerConfig.class).renamingSupportsFormatting) {
            this.addRenderableOnly(new FormattingGuideWidget(this.leftPos + this.imageWidth - 7,
                    this.topPos + this.titleLabelY,
                    this.font));
        }
    }

    protected EditBox createEditBox(Font font, int x, int y, int width, int height, Component message) {
        if (NameTagUpgrade.CONFIG.get(ServerConfig.class).renamingSupportsFormatting) {
            return new FormattableEditBox(font, x, y, width, height, message);
        } else {
            return new EditBox(font, x, y, width, height, message);
        }
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        String inputValue = this.name.getValue();
        super.resize(minecraft, width, height);
        this.name.setValue(inputValue);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawString(this.font,
                this.title,
                this.leftPos + this.titleLabelX,
                this.topPos + this.titleLabelY,
                0xFF404040,
                false);
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderTransparentBackground(guiGraphics);
        guiGraphics.blit(TEXTURE_LOCATION,
                this.leftPos,
                this.topPos,
                0,
                0,
                this.imageWidth,
                this.imageHeight,
                256,
                256);
        guiGraphics.blitSprite(TEXT_FIELD_SPRITE, this.leftPos + 59, this.topPos + 22, 110, 16);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(2.0F, 2.0F, 2.0F);
        guiGraphics.renderFakeItem(new ItemStack(Items.NAME_TAG), (this.leftPos + 17) / 2, (this.topPos + 8) / 2);
        guiGraphics.pose().popPose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
