package com.improject.springkotlindemo.controller

import org.springframework.beans.factory.annotation.Autowired
import com.improject.springkotlindemo.service.*
import com.improject.springkotlindemo.domain.*
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
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
    ) = mailService.sendTextMail(message,locale)
}