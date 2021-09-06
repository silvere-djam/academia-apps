package cm.deepdream.academia.souscription.util;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;

@Component
public class FileStore {
	private Logger logger = Logger.getLogger(FileStore.class.getName()) ;
	@Autowired
	private  AmazonS3 amazonS3;

    public void upload (String path, String fileName, Optional<Map<String, String>> optionalMetaData, InputStream inputStream) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        optionalMetaData.ifPresent(map -> {
            if (!map.isEmpty()) {
                map.forEach(objectMetadata::addUserMetadata);
            }
        });
        try {
            amazonS3.putObject(path, fileName, inputStream, objectMetadata);
        } catch (AmazonServiceException e) {
            throw new IllegalStateException("Failed to upload the file", e);
        }
    }


    public byte[] download(String path, String key) {
        try {
            S3Object object = amazonS3.getObject(path, key);
            S3ObjectInputStream objectContent = object.getObjectContent();
            byte[] bytes = IOUtils.toByteArray(objectContent) ;
            try{
            	Path logoPath = Paths.get(path, key) ;
            	try{
      	            Files.createFile(logoPath);
      	        } catch (FileAlreadyExistsException ex) {}
            	FileOutputStream outputStream = new FileOutputStream(logoPath.toFile()) ;
            	outputStream.write(bytes);
            	outputStream.close();
            }catch(IOException ex) {
            	logger.log(Level.SEVERE, ex.getMessage());
            }
            return bytes ;
        } catch (AmazonServiceException | IOException e) {
            throw new IllegalStateException("Failed to download the file", e);
        }
    }
}
