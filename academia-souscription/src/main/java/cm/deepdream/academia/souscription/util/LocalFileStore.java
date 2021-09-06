package cm.deepdream.academia.souscription.util;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Component;

@Component
public class LocalFileStore {
	private Logger logger = Logger.getLogger(LocalFileStore.class.getName()) ;

    public void upload (String path, String subPath, String fileName,  byte[] bytes)throws IOException {
        try {
        	try {
	            Files.createDirectory(Paths.get(path));
	        } catch (FileAlreadyExistsException ex) {}
		    
		    try {
	            Files.createDirectory(Paths.get(path, subPath));
	        } catch (FileAlreadyExistsException ex) {}
		    
		    Path logoPath = Paths.get(path,  subPath, fileName) ;
		    
		    try {
	            Files.createFile(logoPath);
	        } catch (FileAlreadyExistsException ex) {}
		    
           FileOutputStream outputStream = new FileOutputStream(logoPath.toFile()) ;
           outputStream.write(bytes);
           outputStream.close();
        } catch (IOException ex) {
        	 logger.log(Level.SEVERE, ex.getMessage());
             throw ex ;
        }
    }
    
    

    public byte[] download(String path, String subPath, String fileName) throws IOException{
        try {
        	Path logoPath = Paths.get(path,  subPath, fileName) ;
        	byte[] allBytes = Files.readAllBytes(logoPath);
        	return allBytes ;
        } catch (IOException e) {
           logger.log(Level.SEVERE, e.getMessage());
           return null ;
        }
    }
    
}
