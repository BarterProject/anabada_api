package com.anabada.anabada_api.service.item;


import com.anabada.anabada_api.domain.FileInfo;
import com.anabada.anabada_api.domain.item.ItemImageVO;
import com.anabada.anabada_api.domain.item.ItemVO;
import com.anabada.anabada_api.dto.item.ItemImageDTO;
import com.anabada.anabada_api.repository.ItemImageRepository;
import com.anabada.anabada_api.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.NotSupportedException;
import java.io.IOException;
import java.util.Objects;
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

    public ItemImageVO save(MultipartFile mf, ItemVO item, Long order) throws NotSupportedException, IOException {

        String extension =  "." + Objects.requireNonNull(mf.getContentType()).split("/")[1];

        if(!(extension.equals(".jpg") || extension.equals(".jpeg") || extension.equals(".png") || extension.equals(".bmp")))
            throw new NotSupportedException("not supported extension : " + extension);

        if (mf.getSize() > maxFileSize)
            throw new NotSupportedException("file size exceed");

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



}
