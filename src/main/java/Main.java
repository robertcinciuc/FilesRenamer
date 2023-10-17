import com.google.common.io.Files;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws Exception {
        int index = 0;
        int len = 0;
        if(args.length != 2){
            throw new Exception("Need to input the starting index & length of numbers to rename the files");
        }
        index = Integer.parseInt(args[0]);
        len = Integer.parseInt(args[1]);

        File resources = new File(Paths.get(".", "resources").toString());

        if(!resources.exists()){
            throw new Exception("Resources folder doesn't exist");
        }

        File[] files = resources.listFiles();
        if(files == null){
            throw new Exception("Resources folder content is null");
        }

        List<Path> filePaths = new ArrayList<>();
        Stream.of(files).forEach(f -> filePaths.add(f.toPath()));

        Collections.sort(filePaths);

//        Check for intersecting segments
        int segmentPos = checkIntersectingSegments(filePaths, index);

        if(segmentPos == 0 || segmentPos == -1){
            moveFilesSimpleCase(index, len, filePaths);
        }else if(segmentPos == 1){
            moveWhenTargetIsAbove(index, len, filePaths);
        }

    }

    private static int checkIntersectingSegments(List<Path> filePaths, int targetStartIndex){
        int firstFileNumber = Integer.parseInt(Files.getNameWithoutExtension(filePaths.get(0).getFileName().toString()));
        int lastFileNumber = firstFileNumber + filePaths.size();

        if(firstFileNumber == targetStartIndex){
            return -2;
        }

        if(lastFileNumber < targetStartIndex || targetStartIndex + filePaths.size() < firstFileNumber){
            return 0;
        }

        if(firstFileNumber < targetStartIndex && targetStartIndex < lastFileNumber){
            return 1;
        }else if(firstFileNumber > targetStartIndex){
            return -1;
        }

        return 0;
    }

    private static void moveFilesSimpleCase(int targetIndex, int len, List<Path> filePaths) throws Exception {
        for(Path filePath : filePaths){
            renameFile(targetIndex, len, filePath);

            targetIndex++;
        }
    }

    private static void moveWhenTargetIsAbove(int targetIndex, int len, List<Path> filePaths) throws Exception {
        int upperTarget = targetIndex + filePaths.size() - 1;
        for(int i = filePaths.size() - 1; i >= 0; --i){
            renameFile(upperTarget, len, filePaths.get(i));

            upperTarget--;
        }
    }

    private static void renameFile(int index, int len, Path filePath) throws Exception {
        String actualFormat = "%0" + len + "d";
        String fileExtension = Files.getFileExtension(filePath.getFileName().toString());
        String newFullName = String.format(actualFormat, index) +  "." + fileExtension;
        File newFile = new File(newFullName);
        Path newFilePath = Paths.get(".", "resources", newFullName);

        if(newFile.exists()){
            throw new Exception("A file with the new name already exists");
        }

        java.nio.file.Files.move(filePath, newFilePath);
    }

}
