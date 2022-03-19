package com.anabada.anabada_api.util;

import com.anabada.anabada_api.domain.FileInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Component
public class FileUtil {

    private final String originPath;

    public FileUtil(@Value("${static.path}") String originPath) {
        this.originPath = originPath;
    }

    public byte[] getFile(FileInfo fileInfo) throws IOException {
        File file = new File(originPath + fileInfo.getUploadPath() +"/"+ fileInfo.getSaveName());
        byte[] byfile = null;

        byfile = Files.readAllBytes(file.toPath());

        return byfile;
    }

    public File saveFile(MultipartFile multipartFile, String name, String uploadPath) throws IOException {

        File file = new File(originPath + uploadPath + "/" + name);

        multipartFile.transferTo(file);
        return file;
    }

}
