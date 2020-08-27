///*
// * =============================================================================
// *
// *   Copyright (c) 2011-2014, The THYMELEAF team (http://www.thymeleaf.org)
// *
// *   Licensed under the Apache License, Version 2.0 (the "License");
// *   you may not use this file except in compliance with the License.
// *   You may obtain a copy of the License at
// *
// *       http://www.apache.org/licenses/LICENSE-2.0
// *
// *   Unless required by applicable law or agreed to in writing, software
// *   distributed under the License is distributed on an "AS IS" BASIS,
// *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *   See the License for the specific language governing permissions and
// *   limitations under the License.
// *
// * =============================================================================
// */
//package com.improject.springkotlindemo.service
//
//import com.improject.springkotlindemo.config.SpringMailConfig
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.context.ApplicationContext
//import org.springframework.core.io.ByteArrayResource
//import org.springframework.core.io.ClassPathResource
//import org.springframework.core.io.InputStreamSource
//import org.springframework.mail.javamail.JavaMailSender
//import org.springframework.mail.javamail.MimeMessageHelper
//import org.springframework.stereotype.Service
//import org.thymeleaf.TemplateEngine
//import org.thymeleaf.context.Context
//import java.io.IOException
//import java.util.*
//import javax.mail.MessagingException
//
//@Service
//class EmailService {
//    @Autowired
//    private val applicationContext: ApplicationContext? = null
//
//    @Autowired
//    private val mailSender: JavaMailSender? = null
//
//    @Autowired
//    private val htmlTemplateEngine: TemplateEngine? = null
//
//    @Autowired
//    private val textTemplateEngine: TemplateEngine? = null
//
//    @Autowired
//    private val stringTemplateEngine: TemplateEngine? = null
//
//    /*
//     * Send plain TEXT mail
//     */
//    @Throws(MessagingException::class)
//    fun sendTextMail(
//            recipientName: String?, recipientEmail: String?, locale: Locale?) {
//
//        // Prepare the evaluation context
//        val ctx = Context(locale)
//        ctx.setVariable("name", recipientName)
//        ctx.setVariable("subscriptionDate", Date())
//        ctx.setVariable("hobbies", Arrays.asList("Cinema", "Sports", "Music"))
//
//        // Prepare message using a Spring helper
//        val mimeMessage = mailSender!!.createMimeMessage()
//        val message = MimeMessageHelper(mimeMessage, "UTF-8")
//        message.setSubject("Example plain TEXT email")
//        message.setFrom("thymeleaf@example.com")
//        message.setTo(recipientEmail!!)
//
//        // Create the plain TEXT body using Thymeleaf
//        val textContent = textTemplateEngine!!.process(EMAIL_TEXT_TEMPLATE_NAME, ctx)
//        message.setText(textContent)
//
//        // Send email
//        mailSender.send(mimeMessage)
//    }
//
//    /*
//     * Send HTML mail (simple)
//     */
//    @Throws(MessagingException::class)
//    fun sendSimpleMail(
//            recipientName: String?, recipientEmail: String?, locale: Locale?) {
//
//        // Prepare the evaluation context
//        val ctx = Context(locale)
//        ctx.setVariable("name", recipientName)
//        ctx.setVariable("subscriptionDate", Date())
//        ctx.setVariable("hobbies", Arrays.asList("Cinema", "Sports", "Music"))
//
//        // Prepare message using a Spring helper
//        val mimeMessage = mailSender!!.createMimeMessage()
//        val message = MimeMessageHelper(mimeMessage, "UTF-8")
//        message.setSubject("Example HTML email (simple)")
//        message.setFrom("thymeleaf@example.com")
//        message.setTo(recipientEmail!!)
//
//        // Create the HTML body using Thymeleaf
//        val htmlContent = htmlTemplateEngine!!.process(EMAIL_SIMPLE_TEMPLATE_NAME, ctx)
//        message.setText(htmlContent, true /* isHtml */)
//
//        // Send email
//        mailSender.send(mimeMessage)
//    }
//
//    /*
//     * Send HTML mail with attachment.
//     */
//    @Throws(MessagingException::class)
//    fun sendMailWithAttachment(
//            recipientName: String?, recipientEmail: String?, attachmentFileName: String?,
//            attachmentBytes: ByteArray?, attachmentContentType: String?, locale: Locale?) {
//
//        // Prepare the evaluation context
//        val ctx = Context(locale)
//        ctx.setVariable("name", recipientName)
//        ctx.setVariable("subscriptionDate", Date())
//        ctx.setVariable("hobbies", Arrays.asList("Cinema", "Sports", "Music"))
//
//        // Prepare message using a Spring helper
//        val mimeMessage = mailSender!!.createMimeMessage()
//        val message = MimeMessageHelper(mimeMessage, true /* multipart */, "UTF-8")
//        message.setSubject("Example HTML email with attachment")
//        message.setFrom("thymeleaf@example.com")
//        message.setTo(recipientEmail!!)
//
//        // Create the HTML body using Thymeleaf
//        val htmlContent = htmlTemplateEngine!!.process(EMAIL_WITHATTACHMENT_TEMPLATE_NAME, ctx)
//        message.setText(htmlContent, true /* isHtml */)
//
//        // Add the attachment
//        val attachmentSource: InputStreamSource = ByteArrayResource(attachmentBytes!!)
//        message.addAttachment(
//                attachmentFileName!!, attachmentSource, attachmentContentType!!)
//
//        // Send mail
//        mailSender.send(mimeMessage)
//    }
//
//    /*
//     * Send HTML mail with inline image
//     */
//    @Throws(MessagingException::class)
//    fun sendMailWithInline(
//            recipientName: String?, recipientEmail: String?, imageResourceName: String?,
//            imageBytes: ByteArray?, imageContentType: String?, locale: Locale?) {
//
//        // Prepare the evaluation context
//        val ctx = Context(locale)
//        ctx.setVariable("name", recipientName)
//        ctx.setVariable("subscriptionDate", Date())
//        ctx.setVariable("hobbies", Arrays.asList("Cinema", "Sports", "Music"))
//        ctx.setVariable("imageResourceName", imageResourceName) // so that we can reference it from HTML
//
//        // Prepare message using a Spring helper
//        val mimeMessage = mailSender!!.createMimeMessage()
//        val message = MimeMessageHelper(mimeMessage, true /* multipart */, "UTF-8")
//        message.setSubject("Example HTML email with inline image")
//        message.setFrom("thymeleaf@example.com")
//        message.setTo(recipientEmail!!)
//
//        // Create the HTML body using Thymeleaf
//        val htmlContent = htmlTemplateEngine!!.process(EMAIL_INLINEIMAGE_TEMPLATE_NAME, ctx)
//        message.setText(htmlContent, true /* isHtml */)
//
//        // Add the inline image, referenced from the HTML code as "cid:${imageResourceName}"
//        val imageSource: InputStreamSource = ByteArrayResource(imageBytes!!)
//        message.addInline(imageResourceName!!, imageSource, imageContentType!!)
//
//        // Send mail
//        mailSender.send(mimeMessage)
//    }
//
//    /*
//     * Send HTML mail with inline image
//     */
//    @Throws(MessagingException::class)
//    fun sendEditableMail(
//            recipientName: String?, recipientEmail: String?, htmlContent: String?,
//            locale: Locale?) {
//
//        // Prepare message using a Spring helper
//        val mimeMessage = mailSender!!.createMimeMessage()
//        val message = MimeMessageHelper(mimeMessage, true /* multipart */, "UTF-8")
//        message.setSubject("Example editable HTML email")
//        message.setFrom("thymeleaf@example.com")
//        message.setTo(recipientEmail!!)
//
//        // Prepare the evaluation context
//        val ctx = Context(locale)
//        ctx.setVariable("name", recipientName)
//        ctx.setVariable("subscriptionDate", Date())
//        ctx.setVariable("hobbies", Arrays.asList("Cinema", "Sports", "Music"))
//
//        // Create the HTML body using Thymeleaf
//        val output = stringTemplateEngine!!.process(htmlContent, ctx)
//        message.setText(output, true /* isHtml */)
//
//        // Add the inline images, referenced from the HTML code as "cid:image-name"
//        message.addInline("background", ClassPathResource(BACKGROUND_IMAGE), PNG_MIME)
//        message.addInline("logo-background", ClassPathResource(LOGO_BACKGROUND_IMAGE), PNG_MIME)
//        message.addInline("thymeleaf-banner", ClassPathResource(THYMELEAF_BANNER_IMAGE), PNG_MIME)
//        message.addInline("thymeleaf-logo", ClassPathResource(THYMELEAF_LOGO_IMAGE), PNG_MIME)
//
//        // Send mail
//        mailSender.send(mimeMessage)
//    }
//
//    companion object {
//        private const val EMAIL_TEXT_TEMPLATE_NAME = "text/email-text"
//        private const val EMAIL_SIMPLE_TEMPLATE_NAME = "html/email-simple"
//        private const val EMAIL_WITHATTACHMENT_TEMPLATE_NAME = "html/email-withattachment"
//        private const val EMAIL_INLINEIMAGE_TEMPLATE_NAME = "html/email-inlineimage"
//        private const val EMAIL_EDITABLE_TEMPLATE_CLASSPATH_RES = "classpath:mail/editablehtml/email-editable.html"
//        private const val BACKGROUND_IMAGE = "mail/editablehtml/images/background.png"
//        private const val LOGO_BACKGROUND_IMAGE = "mail/editablehtml/images/logo-background.png"
//        private const val THYMELEAF_BANNER_IMAGE = "mail/editablehtml/images/thymeleaf-banner.png"
//        private const val THYMELEAF_LOGO_IMAGE = "mail/editablehtml/images/thymeleaf-logo.png"
//        private const val PNG_MIME = "image/png"
//    }
//}