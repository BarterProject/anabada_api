package com.anabada.anabada_api.domain.user.service;

import com.anabada.anabada_api.domain.etc.entity.FileInfo;
import com.anabada.anabada_api.domain.user.entity.UserImageVO;
import com.anabada.anabada_api.domain.user.entity.UserVO;
import com.anabada.anabada_api.domain.user.repository.UserImageRepository;
import com.anabada.anabada_api.exception.ApiException;
import com.anabada.anabada_api.exception.ExceptionEnum;
import com.anabada.anabada_api.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserImageService {
    private static final String UPLOADPATH = "/img";

    UserImageRepository userImageRepository;

    FileUtil fileUtil;
    int maxFileSize;


    public UserImageService(UserImageRepository userImageRepository, FileUtil fileUtil, @Value("${static.max-file-size}") int maxFileSize) {
        this.userImageRepository = userImageRepository;
        this.fileUtil = fileUtil;
        this.maxFileSize = maxFileSize;
    }

    public UserImageVO save(UserImageVO vo) {
        return userImageRepository.save(vo);
    }

    public UserImageVO save(MultipartFile mf, UserVO user) {
        if (mf.getSize() == 0L)
            throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION_VALID_ERROR);

        String extension = "." + Objects.requireNonNull(mf.getContentType()).split("/")[1];

        if (!(extension.equals(".jpg") || extension.equals(".jpeg") || extension.equals(".png") || extension.equals(".bmp")
                || extension.equals(".heif") || extension.equals(".heic")))
            throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION_NOT_SUPPORT);

        if (mf.getSize() > maxFileSize)
            throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION_NOT_SUPPORT);

        UUID saveName = UUID.randomUUID();
        fileUtil.saveFile(mf, saveName + extension, UPLOADPATH);

        UserImageVO vo = UserImageVO.builder()
                .name(saveName.toString())
                .fileInfo(FileInfo.builder()
                        .originalName(mf.getOriginalFilename())
                        .saveName(saveName + extension)
                        .size(mf.getSize())
                        .uploadPath(UPLOADPATH)
                        .extension(extension)
                        .build()
                )
                .build();

        vo.setUser(user);
        return this.save(vo);
    }

    public byte[] getByName(String name) {
        Optional<UserImageVO> optionalImage = userImageRepository.findByName(name);

        if (optionalImage.isEmpty())
            throw new ApiException(ExceptionEnum.NOT_FOUND_EXCEPTION);

        return fileUtil.getFile(optionalImage.get().getFileInfo());
    }


}
