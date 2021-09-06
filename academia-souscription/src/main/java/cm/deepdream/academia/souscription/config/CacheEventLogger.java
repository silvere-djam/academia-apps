package cm.deepdream.academia.souscription.config;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;

public class CacheEventLogger implements CacheEventListener<Object, Object>{
	private Logger logger = Logger.getLogger(CacheEventLogger.class.getName()) ;
	
	@Override
    public void onEvent(
      CacheEvent<? extends Object, ? extends Object> cacheEvent) {
		logger.log(Level.INFO, "Cache key="+cacheEvent.getKey()+", Old key value="+cacheEvent.getOldValue()+", New key value="+cacheEvent.getNewValue());
    }
}
