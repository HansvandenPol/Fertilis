//package nl.fertilis.fertilis.configuration;
//
//import javax.sql.DataSource;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
//import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
//import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
//import org.springframework.transaction.TransactionManager;
//
//@Configuration
//@EnableJdbcRepositories
//public class DefaultJdbcConfig extends AbstractJdbcConfiguration {
//  @Bean
//  DataSource dataSource() {
//
//    EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
//    return builder.setType(EmbeddedDatabaseType.HSQL).build();
//  }
//
//  @Bean
//  NamedParameterJdbcOperations namedParameterJdbcOperations(DataSource dataSource) {
//    return new NamedParameterJdbcTemplate(dataSource);
//  }
//
//  @Bean
//  TransactionManager transactionManager(DataSource dataSource) {
//    return new DataSourceTransactionManager(dataSource);
//  }
//}
