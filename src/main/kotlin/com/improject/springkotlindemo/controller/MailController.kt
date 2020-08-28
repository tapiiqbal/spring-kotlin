package com.improject.springkotlindemo.controller

import com.improject.springkotlindemo.domain.Mail
import com.improject.springkotlindemo.service.MailService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ClassPathResource
import org.springframework.util.StreamUtils
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*


@RestController
@RequestMapping("api/sendmail")
class MailController {
    @Autowired
    lateinit var mailService: MailService

    @PostMapping("mailtext")
    fun postMailText(
            @RequestBody message: Mail,
            locale: Locale
    ) = mailService.sendTextMail(message, locale)

    @PostMapping("mailhtml")
    fun postMailHtml(
            @RequestBody message: Mail,
            locale: Locale
    ){

        val imgFile = ClassPathResource("static/img/icon-email.png")
        val bytes = StreamUtils.copyToByteArray(imgFile.inputStream)
        mailService.sendHtmlMail(message,bytes, locale)
    }
}