package com.sky.controller.admin;

import com.sky.constant.FileUploadConstant;
import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 通用接口
 */
@Slf4j
@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
public class CommonController {

    /**
     * 上传图片的地址
     * @param file
     * @return 返回文件的http访问路径
     */
    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload (MultipartFile file){
        log.info("文件上传：{}",file.getOriginalFilename());

        try {
            //1.获取原始文件名并提取后缀
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

            //2.校验文件类型
            if (!isValidImageExtension(extension)) {
                return Result.error("只支持上传 .png, .jpg, .jpeg 格式的图片文件");
            }

            //3.生成唯一文件名，防止重名覆盖
            String newFilename = UUID.randomUUID().toString() + extension;

            //4.创建目标文件对象，并确保父目录存在
            File targetFile = new File(FileUploadConstant.FILE_UPLOAD_PATH + newFilename);
            if (!targetFile.getParentFile().exists()){
                targetFile.getParentFile().mkdirs();
            }

            //5.执行文件传输，将 MultipartFile 保存到磁盘
            file.transferTo(targetFile);

            // 6. 返回可访问的URL给前端
            String fileUrl = FileUploadConstant.FILE_VISIT_PATH + newFilename;
            log.info("文件上传成功，访问路径: {}", fileUrl);
            return Result.success(fileUrl);

        } catch (IOException e) {
            log.info("文件上传失败：",e);
            return Result.error(MessageConstant.UPLOAD_FAILED);
        }
    }

    /**
     * 校验文件类型是否正确
     * @param extension
     * @return
     */
    private boolean isValidImageExtension(String extension) {
        return ".png".equalsIgnoreCase(extension) ||
                ".jpg".equalsIgnoreCase(extension) ||
                ".jpeg".equalsIgnoreCase(extension);
    }
}
