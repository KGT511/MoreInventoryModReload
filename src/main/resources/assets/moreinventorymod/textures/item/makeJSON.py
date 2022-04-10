import os
import pathlib
import json
import shutil


def main():
    this_file_dir = (os.path.dirname(os.path.abspath(__file__)))
    texture_list = [pathlib.Path(f).stem for f in os.listdir(this_file_dir)]
    texture_list.remove("transporter")
    texture_list.remove("makeJSON")
    print(texture_list)
    for texture in texture_list:
        if "storagebox" in texture:
            print(texture)
            new_name = texture.replace("storagebox", "storage_box")
            print(new_name)
            shutil.copy(os.path.join(this_file_dir, texture + ".png"), os.path.join(this_file_dir, new_name + ".png"))
            os.remove(os.path.join(this_file_dir, texture + ".png"))
        # text = {"parent": "item/generated",
        #         "textures": {"layer0": "moreinventorymod:item/" + texture}}
        # print(text)
        # with open(os.path.join(json_dir, texture + ".json"), "w")as f:
        #     print(json.dump(text, f, indent=2))


if __name__ == "__main__":
    main()
    print("hello")

print("aaa")
