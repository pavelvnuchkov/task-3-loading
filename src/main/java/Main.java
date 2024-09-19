import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {

        String pathDirectory = "Game/savegame/";
        createDirectory();
        GameProgress progress1 = new GameProgress(1, 2, 111, 1.0);
        GameProgress progress2 = new GameProgress(2, 3, 222, 2.0);
        GameProgress progress3 = new GameProgress(3, 4, 333, 3.0);
        String pathFile = pathDirectory + "progress1.dat";
        saveGame(pathFile, progress1);
        pathFile = pathDirectory + "progress2.dat";
        saveGame(pathFile, progress2);
        pathFile = pathDirectory + "progress3.dat";
        saveGame(pathFile, progress3);
        zipFiles(pathDirectory, listPathFile(pathDirectory));
        openZip(pathDirectory + "zip.zip", pathDirectory);

        GameProgress progressResult = openProgress(pathDirectory + "progress3.dat");
        System.out.println(progressResult);
    }

    public static GameProgress openProgress(String pathFile) {
        GameProgress gameProgress = null;
        try (ObjectInputStream objectInput = new ObjectInputStream(new FileInputStream(pathFile))) {

            gameProgress = (GameProgress) objectInput.readObject();

        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return gameProgress;
    }

    public static void openZip(String pathZip, String pathOpen) {
        try (ZipInputStream zipInput = new ZipInputStream(new FileInputStream(pathZip))) {

            ZipEntry entry;
            while ((entry = zipInput.getNextEntry()) != null) {
                FileOutputStream fileOutput = new FileOutputStream(pathOpen + entry.getName());
                for (int c = zipInput.read(); c != -1; c = zipInput.read()) {
                    fileOutput.write(c);
                }
                fileOutput.flush();
                zipInput.closeEntry();
                fileOutput.close();
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    //Метод создает список путей к файлам.
    public static List<String> listPathFile(String path) {
        List<String> listPath = new ArrayList<>();
        File directory = new File(path);
        for (File f : directory.listFiles()) {
            listPath.add(f.getPath());
        }
        return listPath;
    }

    public static void zipFiles(String path, List<String> listPathFile) {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(path + "zip.zip"))) {

            for (String pathFile : listPathFile) {
                String fileName = pathFile.substring(pathFile.lastIndexOf('\\') + 1, pathFile.length());
                ZipEntry entry = new ZipEntry(fileName);
                zipOutputStream.putNextEntry(entry);
                FileInputStream inputStream = new FileInputStream(pathFile);
                byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);
                zipOutputStream.write(buffer);

                zipOutputStream.closeEntry();
                inputStream.close();

                File fileProcess = new File(pathFile);
                fileProcess.delete();
            }


        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void saveGame(String path, GameProgress gameProgress) {
        try (FileOutputStream outputStream = new FileOutputStream(path);
             ObjectOutputStream objectOutput = new ObjectOutputStream(outputStream)) {

            objectOutput.writeObject(gameProgress);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createDirectory() {
        StringBuilder builder = new StringBuilder();
        //------1--------
        File dirGame = new File("Game");
        builder.append("Папка " + dirGame.getName() + " создана  - " + dirGame.mkdir() + "\n");
        File dirSrc = new File(dirGame, "src");
        builder.append("Папка " + dirSrc.getName() + " создана  - " + dirSrc.mkdir() + "\n");
        File dirRes = new File(dirGame, "res");
        builder.append("Папка " + dirRes.getName() + " создана  - " + dirRes.mkdir() + "\n");
        File dirSaveGame = new File(dirGame, "savegame");
        builder.append("Папка " + dirSaveGame.getName() + " создана  - " + dirSaveGame.mkdir() + "\n");
        File dirTemp = new File(dirGame, "temp");
        builder.append("Папка " + dirTemp.getName() + " создана  - " + dirTemp.mkdir() + "\n");
        //-------2--------
        File dirMain = new File(dirSrc, "main");
        builder.append("Папка " + dirMain.getName() + " создана  - " + dirMain.mkdir() + "\n");
        File dirTest = new File(dirSrc, "test");
        builder.append("Папка " + dirTest.getName() + " создана  - " + dirTest.mkdir() + "\n");
        //-------3------
        File fileMain = new File(dirMain, "Main.java");
        File fileUtils = new File(dirMain, "Utils.java");
        try {
            builder.append("Файл " + fileMain.getName() + " создан  - " + fileMain.createNewFile() + "\n");
            builder.append("Файл " + fileUtils.getName() + " создан  - " + fileUtils.createNewFile() + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //-------4------
        File dirDrawables = new File(dirRes, "drawables");
        builder.append("Папка " + dirDrawables.getName() + " создана  - " + dirDrawables.mkdir() + "\n");
        File dirVectors = new File(dirRes, "vectors");
        builder.append("Папка " + dirVectors.getName() + " создана  - " + dirVectors.mkdir() + "\n");
        File dirIcons = new File(dirRes, "icons");
        builder.append("Папка " + dirIcons.getName() + " создана  - " + dirIcons.mkdir() + "\n");
        //-------5------
        File fileTemp = new File(dirTemp, "temp.txt");
        try {
            builder.append("Файл " + fileTemp.getName() + " создан  - " + fileTemp.createNewFile() + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (FileWriter writer = new FileWriter(fileTemp)) {
            writer.write(String.valueOf(builder));
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
