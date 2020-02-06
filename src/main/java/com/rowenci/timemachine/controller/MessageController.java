package com.rowenci.timemachine.controller;


import com.alibaba.fastjson.JSON;
import com.rowenci.timemachine.entity.Message;
import com.rowenci.timemachine.service.IMessageService;
import com.rowenci.timemachine.util.CodeInfo.ServiceCodeInfo;
import com.rowenci.timemachine.util.CreateUUID;
import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.errors.MinioException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author rowenci
 * @since 2020-02-06
 */
@RestController
@RequestMapping("/timemachine/message")
public class MessageController {

    final Logger log = LoggerFactory.getLogger(MessageController.class);

    @Resource
    private IMessageService iMessageService;

    @Resource
    private ServiceCodeInfo serviceCodeInfo;

    /**
     * 添加信件
     * @param message
     * @return
     */
    @PostMapping("/")
    public String addMessage(Message message) {
        ModelMap model = new ModelMap();

        //获取uuid
        CreateUUID createUUID = new CreateUUID();
        String messageId = createUUID.create();
        message.setMessageId(messageId);
        boolean res = iMessageService.save(message);

        if (res){
            model.addAttribute("code", serviceCodeInfo.SUCCESS);
            model.addAttribute("data", "");
            model.addAttribute("result", "success");
            model.addAttribute("description", "添加信件成功");
        }else {
            model.addAttribute("code", serviceCodeInfo.ADD_MESSAGE_ERROR);
            model.addAttribute("data", "");
            model.addAttribute("result", "error");
            model.addAttribute("description", "添加信件失败");
        }

        return JSON.toJSONString(model);
    }

    /**-
     * 上传图片
     * @param file
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws InvalidKeyException
     * @throws XmlPullParserException
     */
    @PostMapping("/uploadImage")
    public String uploadImage(@RequestParam("file") MultipartFile file) throws NoSuchAlgorithmException, IOException, InvalidKeyException, XmlPullParserException {

        ModelMap modelMap = new ModelMap();
        CreateUUID createUUID = new CreateUUID();
        List<String> imageType = new ArrayList<>();
        imageType.add("jpg");
        imageType.add("jpeg");
        imageType.add("png");
        imageType.add("bmp");
        imageType.add("gif");
        String originalFilename = file.getOriginalFilename();
        String fileSuffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        String newFileName = createUUID.create() + "." + fileSuffix;
        String dirPath = System.getProperty("user.dir");
        String path = File.separator + "uploadImg" + File.separator + newFileName;
        if (imageType.contains(fileSuffix)) {
            File destFile = new File(dirPath + path);
            if (!destFile.getParentFile().exists()) {
                destFile.getParentFile().mkdirs();
            }
            try {
                file.transferTo(destFile);
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        log.info("文件名----" + newFileName);
        log.info("文件路径----" + dirPath);
        log.info("文件路径----" + path);
        log.info("打开inputstream");
        FileInputStream inputStream = new FileInputStream(new File(dirPath + path));
        try {
            // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
            MinioClient minioClient = new MinioClient("http://192.168.1.21:9001", "minio", "minio123");

            // 检查存储桶是否已经存在
            boolean isExist = minioClient.bucketExists("timemachine");
            if (isExist) {
                log.info("Bucket already exists.");
            } else {
                // 创建一个名为asiatrip的存储桶
                minioClient.makeBucket("timemachine");
            }

            // 使用putObject上传一个文件到存储桶中。
            minioClient.putObject("timemachine", newFileName, inputStream, file.getSize(), "img");
            inputStream.close();
            minioClient.statObject("timemachine", newFileName);
            log.info("关闭inputstream");

            try {

                ObjectStat obj = minioClient.statObject("timemachine", newFileName);
                log.info("statobject----" + obj.bucketName());
                log.info("statobject----" + obj.name());

            }catch (Exception e){

                modelMap.addAttribute("code", serviceCodeInfo.UPLOAD_ERROR);
                modelMap.addAttribute("data", "");
                modelMap.addAttribute("result", "error");
                modelMap.addAttribute("description", "上传文件失败");
                return JSON.toJSONString(modelMap);

            }

        } catch (MinioException e) {

            //上传文件失败
            System.out.println("Error occurred: " + e);
            inputStream.close();

            modelMap.addAttribute("code", serviceCodeInfo.UPLOAD_ERROR);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "error");
            modelMap.addAttribute("description", "上传文件失败");

            return JSON.toJSONString(modelMap);
        }

        log.info("上传文件成功");
        //上传文件成功
        //删除临时文件
        File tempFile = new File(dirPath + path);
        tempFile.delete();

        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("src", "http://192.168.1.21:9001/timemachine/" + newFileName);
        infoMap.put("title", newFileName);
        modelMap.addAttribute("code", serviceCodeInfo.UPLOAD_MESSAGE_PIC_SUCCESS);
        modelMap.addAttribute("msg", "上传文件成功");
        modelMap.addAttribute("data", infoMap);
        return JSON.toJSONString(modelMap);

    }

}
