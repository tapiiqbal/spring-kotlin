package com.improject.springkotlindemo.config

import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.EnvironmentAware
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.PropertySource
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.core.env.Environment
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.thymeleaf.TemplateEngine
import org.thymeleaf.spring5.SpringTemplateEngine
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import org.thymeleaf.templateresolver.ITemplateResolver
import org.thymeleaf.templateresolver.StringTemplateResolver
import java.io.IOException
import java.util.*


@Configuration
@PropertySource("classpath:mail/emailconfig.properties")
class SpringMailConfig : ApplicationContextAware, EnvironmentAware {
    private var applicationContext: ApplicationContext? = null
    private var environment: Environment? = null

    @Throws(BeansException::class)
    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.applicationContext = applicationContext
    }

    override fun setEnvironment(environment: Environment) {
        this.environment = environment
    }

    /*
     * SPRING + JAVAMAIL: JavaMailSender instance, configured via .properties files.
     */
    @Bean
    @Throws(IOException::class)
    fun mailSender(): JavaMailSender {
        val mailSender = JavaMailSenderImpl()

        // Basic mail sender configuration, based on emailconfig.properties
        mailSender.host = environment!!.getProperty(HOST)
        mailSender.port = environment!!.getProperty(PORT)!!.toInt()
        mailSender.protocol = environment!!.getProperty(PROTOCOL)
        mailSender.username = environment!!.getProperty(USERNAME)
        mailSender.password = environment!!.getProperty(PASSWORD)

        // JavaMail-specific mail sender configuration, based on javamail.properties
        val javaMailProperties = Properties()
        javaMailProperties.load(applicationContext!!.getResource(JAVA_MAIL_FILE).inputStream)
        mailSender.javaMailProperties = javaMailProperties
        return mailSender
    }

    /*
     *  Message externalization/internationalization for emails.
     *
     *  NOTE we are avoiding the use of the name 'messageSource' for this bean because that
     *       would make the MessageSource defined in SpringWebConfig (and made available for the
     *       web-side template engine) delegate to this one, and thus effectively merge email
     *       messages into web messages and make both types available at the web side, which could
     *       bring undesired collisions.
     *
     *  NOTE also that given we want this specific message source to be used by our
     *       SpringTemplateEngines for emails (and not by the web one), we will set it explicitly
     *       into each of the TemplateEngine objects with 'setTemplateEngineMessageSource(...)'
     */
    @Bean
    fun emailMessageSource(): ResourceBundleMessageSource {
        val messageSource = ResourceBundleMessageSource()
        messageSource.setBasename("mail/MailMessages")
        return messageSource
    }

    @Bean
    @Primary
    fun emailTemplateEngine(): TemplateEngine? {
        val templateEngine = SpringTemplateEngine()
        // Resolver for TEXT emails
        templateEngine.addTemplateResolver(textTemplateResolver())
        // Resolver for HTML emails (except the editable one)
        templateEngine.addTemplateResolver(htmlTemplateResolver())
        // Resolver for HTML editable emails (which will be treated as a String)
        templateEngine.addTemplateResolver(stringTemplateResolver())
        // Message source, internationalization specific to emails
        templateEngine.setTemplateEngineMessageSource(emailMessageSource())
        return templateEngine
    }

    /* ******************************************************************** */ /*  THYMELEAF-SPECIFIC ARTIFACTS FOR EMAIL                              */ /*  TemplateResolver(3) <- TemplateEngine                               */ /* ******************************************************************** */

    private fun textTemplateResolver(): ITemplateResolver {
        val templateResolver = ClassLoaderTemplateResolver()
        templateResolver.setPrefix("/templates/")
        templateResolver.setSuffix(".txt")
        templateResolver.setTemplateMode(TemplateMode.TEXT)
        templateResolver.setCharacterEncoding(EMAIL_TEMPLATE_ENCODING)
        templateResolver.setOrder(Integer.valueOf(1))
        templateResolver.setResolvablePatterns(setOf("text/*"))
        templateResolver.setCheckExistence(true);
        templateResolver.setCacheable(false)
        return templateResolver
    }

    private fun htmlTemplateResolver(): ITemplateResolver {
        val templateResolver = ClassLoaderTemplateResolver()
        templateResolver.setOrder(Integer.valueOf(2))
        templateResolver.setResolvablePatterns(setOf("html/*"))
        templateResolver.setPrefix("/templates/")
        templateResolver.setSuffix(".html")
        templateResolver.setTemplateMode(TemplateMode.HTML)
        templateResolver.setCharacterEncoding(EMAIL_TEMPLATE_ENCODING)
        templateResolver.setCacheable(false)
        return templateResolver
    }

    private fun stringTemplateResolver(): ITemplateResolver {
        val templateResolver = StringTemplateResolver()
        templateResolver.setOrder(Integer.valueOf(3))
        // No resolvable pattern, will simply process as a String template everything not previously matched
        templateResolver.setTemplateMode("HTML")
        templateResolver.setCacheable(false)
        return templateResolver
    }

    companion object {
        const val EMAIL_TEMPLATE_ENCODING = "UTF-8"
        private const val JAVA_MAIL_FILE = "classpath:mail/javamail.properties"
        private const val HOST = "mail.server.host"
        private const val PORT = "mail.server.port"
        private const val PROTOCOL = "mail.server.protocol"
        private const val USERNAME = "mail.server.username"
        private const val PASSWORD = "mail.server.password"
    }
}
