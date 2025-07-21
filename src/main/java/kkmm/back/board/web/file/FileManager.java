package kkmm.back.board.web.file;

import kkmm.back.board.domain.model.UploadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileManager {

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String fileName) {
        return fileDir + fileName;
    }

    public void deleteFiles(List<String> fileNames) {
        if (fileNames == null || fileNames.isEmpty()) { return;}
        for (String fileName : fileNames) {
            deleteFile(fileName);
        }
    }

    public void deleteFile(String fileName) {
        File file = new File(getFullPath(fileName));
        if (file.exists()) {
            file.delete();
        }
    }

    public List<UploadFile> storeFiles(List<MultipartFile> files)  {
        List<UploadFile> storeFileResult = new ArrayList<>();

        if (files == null || files.isEmpty()) { return storeFileResult;}

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                storeFileResult.add(storeFile(file));
            }
        }
        return storeFileResult;
    }

    public UploadFile storeFile(MultipartFile file) {
        if (file.isEmpty()) {
            return null;
        }

        String originalFilename = file.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);

        try {
            file.transferTo(new File(getFullPath(storeFileName)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new UploadFile(originalFilename, storeFileName);
    }

    private static String createStoreFileName(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(originalFilename);
       return uuid + "." + ext;
    }

    private static String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
