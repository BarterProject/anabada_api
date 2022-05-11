package com.anabada.anabada_api.domain.item.service;


import com.anabada.anabada_api.domain.item.entity.ItemImageVO;
import com.anabada.anabada_api.domain.item.entity.ItemVO;
import com.anabada.anabada_api.domain.etc.entity.FileInfo;
import com.anabada.anabada_api.domain.item.repository.ItemImageRepository;
import com.anabada.anabada_api.exception.ApiException;
import com.anabada.anabada_api.exception.ExceptionEnum;
import com.anabada.anabada_api.util.FileUtil;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.NotSupportedException;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class ItemImageService {
    private static final String UPLOADPATH = "/img";

    ItemImageRepository itemImageRepository;

    FileUtil fileUtil;
    int maxFileSize;

    public ItemImageService(ItemImageRepository itemImageRepository, FileUtil fileUtil, @Value("${static.max-file-size}") int maxFileSize) {
        this.itemImageRepository = itemImageRepository;
        this.fileUtil = fileUtil;
        this.maxFileSize = maxFileSize;
    }

    public ItemImageVO save(ItemImageVO vo) {
        return itemImageRepository.save(vo);
    }

    public ItemImageVO save(MultipartFile mf, ItemVO item, Long order){

        String extension =  "." + Objects.requireNonNull(mf.getContentType()).split("/")[1];

        if(!(extension.equals(".jpg") || extension.equals(".jpeg") || extension.equals(".png") || extension.equals(".bmp")
                || extension.equals(".heif") || extension.equals(".heic")))
            throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION);

        if (mf.getSize() > maxFileSize)
            throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION);

        UUID saveName = UUID.randomUUID();
        fileUtil.saveFile(mf, saveName + extension, UPLOADPATH);

        ItemImageVO vo = ItemImageVO.builder()
                .name(saveName.toString())
                .fileInfo(FileInfo.builder()
                        .originalName(mf.getOriginalFilename())
                        .saveName(saveName + extension)
                        .size(mf.getSize())
                        .uploadPath(UPLOADPATH)
                        .extension(extension)
                        .build())
                .number(order)
                .build();

        vo.setItem(item);

        return this.save(vo);
    }

    public byte[] getByName(String name){
        Optional<ItemImageVO> optionalImage = itemImageRepository.findByName(name);

        if(optionalImage.isEmpty())
            throw new ApiException(ExceptionEnum.NOT_FOUND_EXCEPTION);

        return fileUtil.getFile(optionalImage.get().getFileInfo());
    }



}
