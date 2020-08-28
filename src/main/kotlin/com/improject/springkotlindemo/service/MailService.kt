package com.improject.springkotlindemo.service

import com.improject.springkotlindemo.domain.Mail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.InputStreamSource
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import java.nio.file.Paths
import java.util.*
import javax.mail.MessagingException


@Service
class MailService @Autowired constructor(private val mailSender: JavaMailSender) {
    @Autowired
    lateinit var textTemplateEngine: TemplateEngine

    @Autowired
    lateinit var htmlTemplateEngine: TemplateEngine


    fun sendMail(mail: Mail): String {
       var content = """
                    <html>
                    <head>
                    </head>
                    <body>
                    Yth <span>Director Adi</span>
                    <table>
                        <tr>
                            <td>Mt4 account Name</td>
                            <td>:</td>
                            <td>ptm-mab-an</td>
                        </tr>
                        <tr>
                            <td>Mt4 account number</td>
                            <td>:</td>
                            <td>2100198747</td>
                        </tr>
                        <tr>
                            <td>Score</td>
                            <td>:</td>
                            <td>53.7080</td>
                        </tr>
                        <tr>
                            <td>accountRiskProfile</td>
                            <td>:</td>
                            <td>konservatif</td>
                        </tr>
                    </table>
                    <button>Oke</button>
                    </body>
                    </html>
                """.trimIndent()
        val mimeMessage = mailSender.createMimeMessage()
        val message = MimeMessageHelper(mimeMessage, "UTF-8")
        message.setFrom("sender@example.com")
        message.setTo("iqbal@mailinator.com")
        message.setSubject("This is the message subject")
        message.setText("<button style=color:blue;><b>Oke</b></button>", true)
        mailSender.send(mimeMessage)

        return "success"
    }

    /*
     * Send plain TEXT mail
     */
    @Throws(MessagingException::class)
    fun sendTextMail(mail: Mail,locale: Locale): String {
        // Prepare the evaluation context
        val ctx = Context(locale)
        ctx.setVariable("name", mail.recipientName)
        ctx.setVariable("subscriptionDate", Date())
        ctx.setVariable("hobbies", Arrays.asList("Cinema", "Sports", "Music",mail.recipientName))

        // Prepare message using a Spring helper
        val mimeMessage = mailSender.createMimeMessage()
        val message = MimeMessageHelper(mimeMessage, "UTF-8")
        message.setSubject("Example plain TEXT email")
        message.setFrom("thymeleaf@example.com")
        message.setTo(mail.recipientEmail!!)

        // Create the plain TEXT body using Thymeleaf
        val textContent = this.textTemplateEngine.process(EMAIL_TEXT_TEMPLATE_NAME, ctx)
        message.setText(textContent)

        // Send email
        mailSender.send(mimeMessage)
        return "Sukses"
    }

        /*
     * Send HTML mail (simple)
     */
    @Throws(MessagingException::class)
    fun sendHtmlMail(mail: Mail, bytes: ByteArray,locale: Locale): String {

        // Prepare the evaluation context
        val ctx = Context(locale)
        ctx.setVariable("name", mail.recipientName)
        ctx.setVariable("name", mail.recipientName)
        ctx.setVariable("subscriptionDate", Date())
        ctx.setVariable("hobbies", Arrays.asList("Cinema", "Sports", "Music"))

        // Prepare message using a Spring helper
        val mimeMessage = mailSender!!.createMimeMessage()
        val message = MimeMessageHelper(mimeMessage,true, "UTF-8")
        message.setSubject("Example HTML email (simple)")
        message.setFrom("thymeleaf@example.com")
        message.setTo(mail.recipientEmail!!)
            ctx.setVariable("imageResourceName", "image"); // so that we can reference it from HTML

        // Create the HTML body using Thymeleaf
        val htmlContent = htmlTemplateEngine!!.process(EMAIL_SIMPLE_TEMPLATE_NAME, ctx)
        message.setText(htmlContent, true /* isHtml */)

        //Add the inline image, referenced from the HTML code as "cid:${imageResourceName}"
        val imageSource: InputStreamSource = ByteArrayResource(bytes!!)
        message.addInline("image", imageSource, "image/png")

        // Send email
        mailSender.send(mimeMessage)
            return "Sukses"
    }

    companion object {
        private const val EMAIL_TEXT_TEMPLATE_NAME = "text/email-text"
        private const val EMAIL_SIMPLE_TEMPLATE_NAME = "html/email-simple"
        private const val EMAIL_WITHATTACHMENT_TEMPLATE_NAME = "html/email-withattachment"
        private const val EMAIL_INLINEIMAGE_TEMPLATE_NAME = "html/email-inlineimage"
        private const val EMAIL_EDITABLE_TEMPLATE_CLASSPATH_RES = "classpath:mail/editablehtml/email-editable.html"
        private const val BACKGROUND_IMAGE = "mail/editablehtml/images/background.png"
        private const val LOGO_BACKGROUND_IMAGE = "mail/editablehtml/images/logo-background.png"
        private const val THYMELEAF_BANNER_IMAGE = "mail/editablehtml/images/thymeleaf-banner.png"
        private const val THYMELEAF_LOGO_IMAGE = "mail/editablehtml/images/thymeleaf-logo.png"
        private const val PNG_MIME = "image/png"
    }
}
