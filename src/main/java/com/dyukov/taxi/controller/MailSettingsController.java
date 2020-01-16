package com.dyukov.taxi.controller;

import com.dyukov.taxi.config.JwtTokenUtil;
import com.dyukov.taxi.dao.MailSettingsDao;
import com.dyukov.taxi.service.IMailSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mailSettings")
public class MailSettingsController {

    @Autowired
    private IMailSettingsService mailSettingsService;

    @Autowired
    private JwtTokenUtil tokenUtil;

    @RequestMapping("/update")
    public MailSettingsDao updateUserMailSettings(
            @RequestHeader("usertoken") String token,
            @RequestBody MailSettingsDao settingsDao) {
        return mailSettingsService.updateSettings(tokenUtil.getUserIdFromToken(token), settingsDao);
    }


}
