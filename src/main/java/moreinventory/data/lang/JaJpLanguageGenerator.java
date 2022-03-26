package moreinventory.data.lang;

import moreinventory.block.Blocks;
import moreinventory.core.MoreInventoryMOD;
import moreinventory.item.Items;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class JaJpLanguageGenerator extends LanguageProvider {
    public JaJpLanguageGenerator(DataGenerator generator, String modid) {
        super(generator, modid, "ja_jp");
    }

    @Override
    public String getName() {
        return MoreInventoryMOD.MODID + " " + super.getName();
    }

    @Override
    protected void addTranslations() {
        add(Blocks.CATCHALL, "ガラクタ入れ");
        add(Items.TRANSPORTER, "トランスポーター");
        add(Items.SPANNER, "スパナ");

        add(Blocks.WOOD_STORAGE_BOX, "木のコンテナ");
        add(Blocks.IRON_STORAGE_BOX, "鉄のコンテナ");
        add(Blocks.GOLD_STORAGE_BOX, "金のコンテナ");
        add(Blocks.DIAMOND_STORAGE_BOX, "ダイヤモンドのコンテナ");
        add(Blocks.EMERALD_STORAGE_BOX, "エメラルドのコンテナ");
        add(Blocks.COPPER_STORAGE_BOX, "銅のコンテナ");
        add(Blocks.TIN_STORAGE_BOX, "錫のコンテナ");
        add(Blocks.BRONZE_STORAGE_BOX, "青銅のコンテナ");
        add(Blocks.SILVER_STORAGE_BOX, "銀のコンテナ");
        add(Blocks.GLASS_STORAGE_BOX, "ガラスのコンテナ");

        add(Blocks.IMPORTER, "インポーター");
        add(Blocks.EXPORTER, "エクスポーター");
        add(Text.importerRegister, "登録");
        add(Text.importerRegisterOn, "する");
        add(Text.importerRegisterOff, "しない");
        add(Text.importerRegisterOnDetail, "未登録のアイテムがある場合コンテナに新規登録して移動します");
        add(Text.importerRegisterOffDetail, "コンテナに登録されていないアイテムを移動しません");
        add(Text.importerMove, "搬入");
        add(Text.importerMoveWhite, "する");
        add(Text.importerMoveBlack, "しない");
        add(Text.importerMoveWhiteDetail, "リストのアイテムのみ輸送します");
        add(Text.importerMoveBlackDetail, "リスト以外のアイテムを輸送します");

    }
}
