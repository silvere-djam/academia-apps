package cm.deepdream.academia.souscription;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.dialect.PostgreSQL10Dialect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import com.zaxxer.hikari.HikariDataSource;

@TestConfiguration
public class AcademiaSouscriptionTestConfiguration {
	@Value("${spring.datasource.url}")
	private String url ;
	@Value("${spring.datasource.username}")
	private String username ;
	@Value("${spring.datasource.password}")
	private String password ;
	@Value("${spring.datasource.driver-class-name}")
	private String driverClassName ;
	
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		emf.setDataSource(dataSource());
		emf.setJpaVendorAdapter(jpaVendorAdapter());
		return emf;
	}
	
	
	private JpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
		jpaVendorAdapter.setShowSql(true);
		jpaVendorAdapter.setGenerateDdl(true);
		jpaVendorAdapter.setDatabasePlatform(PostgreSQL10Dialect.class.getName());
		return jpaVendorAdapter;
	}
	
	
	@Bean
	public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}
	
	
	@Bean
	public DataSource dataSource() { 
		HikariDataSource dataSource = new HikariDataSource() ;
		dataSource.setDriverClassName(driverClassName) ;
		dataSource.setJdbcUrl(url) ;
		dataSource.setUsername(username) ;
		dataSource.setPassword(password) ;
		dataSource.setMinimumIdle(2);
		dataSource.setMaximumPoolSize(5);
		return dataSource ;
	}
}
