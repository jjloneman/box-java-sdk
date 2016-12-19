package com.box.sdk.example;

import com.box.sdk.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.box.sdk.example.Constants.DEVELOPER_TOKEN;
import static com.box.sdk.example.Constants.MAX_DEPTH;

public final class Main {

    private Main() { }

    public static void main(String[] args) {
        // Turn off logging to prevent polluting the output.
        Logger.getLogger("com.box.sdk").setLevel(Level.OFF);

        BoxAPIConnection api = new BoxAPIConnection(DEVELOPER_TOKEN);

        BoxUser.Info userInfo = BoxUser.getCurrentUser(api).getInfo();
        System.out.format("Welcome, %s <%s>!\n\n", userInfo.getName(), userInfo.getLogin());

        System.out.println("\nListing files...");

        BoxFolder rootFolder = BoxFolder.getRootFolder(api);
        listFolder(rootFolder, 0);

        System.out.println("\nUploading file...");

        // 0 is the root folder
        BoxFolder folder = new BoxFolder(api, "14413766927");
        uploadFile(folder, "hello_world_api.txt", "src/main/resources/hello_world.txt");
    }

    private static void uploadFile(BoxFolder folder, String fileName, String fileLocation) {
        InputStream in = null;

        try {
            in = new FileInputStream(fileLocation);
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found: " + e);
        }

        folder.uploadFile(in, fileName);
    }

    private static void listFolder(BoxFolder folder, int depth) {
        for (BoxItem.Info itemInfo : folder) {
            String indent = "";
            for (int i = 0; i < depth; i++) {
                indent += "    ";
            }

            System.out.println(indent + itemInfo.getName() + ":" + itemInfo.getID());
            if (itemInfo instanceof BoxFolder.Info) {
                BoxFolder childFolder = (BoxFolder) itemInfo.getResource();
                if (depth < MAX_DEPTH) {
                    listFolder(childFolder, depth + 1);
                }
            }
        }
    }
}
