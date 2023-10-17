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
        int start = 0;
        if(args.length != 1){
            throw new Exception("Need to input the starting index to rename the files");
        }
        start = Integer.parseInt(args[0]);

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

        for(Path filePath : filePaths){
            String fileExtension = Files.getFileExtension(filePath.getFileName().toString());
            File newFile = new File(start +  "." + fileExtension);
            Path newFilePath = Paths.get(".", "resources", start +  "." + fileExtension);

            if(newFile.exists()){
                throw new Exception("A file with the new name already exists");
            }

            java.nio.file.Files.move(filePath, newFilePath);

            start++;
        }
    }
}
