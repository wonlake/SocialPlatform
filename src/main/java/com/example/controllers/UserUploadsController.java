package com.example.controllers;

import com.example.ErrorCode;
import com.example.ResponseMessage;
import com.example.config.SrPlatformSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by meijun on 2016/11/18.
 */
@RestController
@CrossOrigin
public class UserUploadsController {
    @Autowired
    private SrPlatformSettings settings;

    @RequestMapping(path = "user/upload/headIcon", method = RequestMethod.POST)
    public ResponseMessage uploadHeadIcon(@RequestParam("userId") String userId,
                                          @RequestParam("file")MultipartFile file) {
        ResponseMessage response = new ResponseMessage();
        if(file.isEmpty()) {
            response.setErrorCode(ErrorCode.EC_Error);
            response.setMsg("No file uploaded!");
            return response;
        }
        else if(file.getSize() > settings.getMaxImageSize()) {
            response.setErrorCode(ErrorCode.EC_Error);
            response.setMsg("The image size is too large!");
            return response;
        }
        try {
            response.setMsg(new String(file.getBytes()));
        }
        catch (IOException e) {
            System.err.println(e.toString());
        }
        return response;
    }
}
