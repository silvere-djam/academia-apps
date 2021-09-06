package cm.deepdream.academia.web.util;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.springframework.stereotype.Component;

@Component
public class CaptchaFileStrGenerator {
	public static final String FILE_EXTENSION = "jpg";
	
	public String genererStr (int taille, String idSession) throws FileNotFoundException, IOException{
		 String saltCharacters = "1234567890abranNumcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
         StringBuffer captchaStrBuff = new StringBuffer();
         java.util.Random ranNum = new java.util.Random();
                
         // Build a random captchaLength chars salt        
         while (captchaStrBuff.length() < taille){
              int index = (int) (ranNum.nextFloat() * saltCharacters.length());
              captchaStrBuff.append(saltCharacters.substring(index, index+1));
         }
         
         String captchaStr = captchaStrBuff.toString();
         
         int width = 200;         
         int height = 80;
         
         Color fg = new Color(21, 96, 189);
         Color bg = new Color(255, 255, 255);
         
         Font font = new Font("Arial", Font.BOLD, 40);
         BufferedImage captchaImage = new BufferedImage(width, height, BufferedImage.OPAQUE);
         Graphics graphics = captchaImage.createGraphics();
         graphics.setFont(font);
         graphics.setColor(bg);
         graphics.fillRect(0, 0, width, height);
         graphics.setColor(fg);
         graphics.drawString(captchaStr,30,55) ;   
         
         File captchaFolder = getCaptchaFolder() ;
         FileOutputStream outputStream = new FileOutputStream(new File(captchaFolder, "captcha-"+idSession+"."+FILE_EXTENSION)) ;
         
         ImageIO.write(captchaImage, FILE_EXTENSION, outputStream);
         outputStream.close();
         return captchaStr ;
	}
	
	
	private File getCaptchaFolder() {
		File parent = new File("./captcha") ;
		if(!parent.exists()) {
			parent.mkdir() ;
		}
		return parent ;
	}
	
	
}
