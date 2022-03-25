package moreinventory.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;

public final class MIMUtils {
    public static int normalIndex(int idx, int size) {
        if (size == 0)
            return 0;
        int tmp = idx % size;
        return (tmp + size) % size;

    }

    //ItemStackHelperがByteだったため
    public static CompoundNBT writeNonNullListShort(CompoundNBT tag, NonNullList<ItemStack> list, boolean saveEmpty) {
        ListNBT listnbt = new ListNBT();

        for (int i = 0; i < list.size(); ++i) {
            ItemStack itemstack = list.get(i);
            if (!itemstack.isEmpty()) {
                CompoundNBT compoundnbt = new CompoundNBT();
                compoundnbt.putShort("Slot", (short) i);
                itemstack.write(compoundnbt);
                listnbt.add(compoundnbt);
            }
        }

        if (!listnbt.isEmpty() || saveEmpty) {
            tag.put("Items", listnbt);
        }

        return tag;
    }

    public static void readNonNullListShort(CompoundNBT tag, NonNullList<ItemStack> list) {
        ListNBT listnbt = tag.getList("Items", 10);

        for (int i = 0; i < listnbt.size(); ++i) {
            CompoundNBT compoundnbt = listnbt.getCompound(i);
            int j = compoundnbt.getShort("Slot");
            if (j >= 0 && j < list.size()) {
                list.set(j, ItemStack.read(compoundnbt));
            }
        }
    }

    public static void setIcon(ItemStack s, byte num) {
        final String key = "CustomModelData";
        CompoundNBT tag = s.getOrCreateTag();
        tag.putByte(key, (byte) num);
        s.setTag(tag);
    }
}
