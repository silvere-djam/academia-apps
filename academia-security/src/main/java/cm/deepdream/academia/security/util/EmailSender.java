package cm.deepdream.academia.security.util;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Template;
import freemarker.template.TemplateException;
@Component
public class EmailSender {
	@Autowired
	private FreeMarkerConfigurer freemarkerConfigurer ;
	@Autowired
    private JavaMailSender sender ;
	@Autowired
	private Environment env ;
	 
	
	public void sendMessage(String to, String subject, String templateFile,
			Map<String, Object> templateModel) throws IOException, TemplateException, MessagingException {
	        
	    Template freemarkerTemplate = freemarkerConfigurer.createConfiguration().getTemplate(templateFile) ;
	    String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, templateModel) ;
	 
	    MimeMessage message = sender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
	    helper.setTo(to);
	    helper.setSubject(subject);
	    helper.setText(htmlBody, true);
	    sender.send(message);
	}
	
	public void sendMessage(String to, String subject, String templateFile, String pathToAttachment,
			 Map<String, Object> templateModel) throws IOException, TemplateException, MessagingException {
		
		Template freemarkerTemplate = freemarkerConfigurer.createConfiguration().getTemplate(templateFile) ;
	    String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, templateModel) ;
	    
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8") ;
		helper.setFrom("noreply@baeldung.com");
		helper.setTo(to) ;
		helper.setSubject(subject) ;
		helper.setText(htmlBody, true) ;
		FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
		helper.addAttachment("Invoice", file) ;
		sender.send(message);
	}
	
}
