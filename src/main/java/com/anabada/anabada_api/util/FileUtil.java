package com.anabada.anabada_api.util;

import com.anabada.anabada_api.domain.etc.entity.FileInfo;
import com.anabada.anabada_api.exception.ApiException;
import com.anabada.anabada_api.exception.ExceptionEnum;
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

    public byte[] getFile(FileInfo fileInfo) {
        File file = new File(originPath + fileInfo.getUploadPath() +"/"+ fileInfo.getSaveName());
        byte[] byfile = null;

        try{
            byfile = Files.readAllBytes(file.toPath());
        }catch (Exception e){
            throw new ApiException(ExceptionEnum.INTERNAL_SERVER_ERROR);
        }

        return byfile;
    }

    public File saveFile(MultipartFile multipartFile, String name, String uploadPath){

        File file = new File(originPath + uploadPath + "/" + name);

        try{
            multipartFile.transferTo(file);
        }catch (Exception e){
            throw new ApiException(ExceptionEnum.INTERNAL_SERVER_ERROR);
        }
        return file;
    }

}
