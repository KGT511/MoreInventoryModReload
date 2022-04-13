package moreinventory.util;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;

public final class MIMUtils {
    public static int normalIndex(int idx, int size) {
        if (size == 0)
            return 0;
        int tmp = idx % size;
        return (tmp + size) % size;

    }

    //ItemStackHelperがByteだったため
    public static CompoundTag writeNonNullListShort(CompoundTag tag, NonNullList<ItemStack> list, boolean saveEmpty) {
        ListTag listnbt = new ListTag();

        for (int i = 0; i < list.size(); ++i) {
            ItemStack itemstack = list.get(i);
            if (!itemstack.isEmpty()) {
                CompoundTag compoundnbt = new CompoundTag();
                compoundnbt.putShort("Slot", (short) i);
                itemstack.save(compoundnbt);
                listnbt.add(compoundnbt);
            }
        }

        if (!listnbt.isEmpty() || saveEmpty) {
            tag.put("Items", listnbt);
        }

        return tag;
    }

    public static void readNonNullListShort(CompoundTag tag, NonNullList<ItemStack> list) {
        ListTag listnbt = tag.getList("Items", 10);

        for (int i = 0; i < listnbt.size(); ++i) {
            CompoundTag compoundnbt = listnbt.getCompound(i);
            int j = compoundnbt.getShort("Slot");
            if (j >= 0 && j < list.size()) {
                list.set(j, ItemStack.of(compoundnbt));
            }
        }
    }

    public static void setIcon(ItemStack s, byte num) {
        final String key = "CustomModelData";
        CompoundTag tag = s.getOrCreateTag();
        tag.putByte(key, (byte) num);
        s.setTag(tag);
    }

    public static boolean intToBool(int val) {
        return val == 0 ? false : true;
    }
}
