package com.servicekerdit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DataBaseConfig {

    //Crear dos objetos de tipo DATASOURCE
    @Bean(name = "dsSlave")
    @ConfigurationProperties(prefix = "legacy.datasource") //configuración de la base de datos legacy, que en este caso es la database2
    public DataSource slaveDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "dsMaster")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource masterDataSource() {  //devolverá el datasource para la base de datos database1 con el nombre dsMaster
        return DataSourceBuilder.create().build();
    }

    //Crear dosobjetos de tipo JDBCTEMPLATE

    //Para utilizar Spring JDBC es necesario utilizar un objeto de este tipo
    //el método recibe un objeto de tipo datasource el cuál es inyectado gracias a la anotación @Autowired
    //existen 2 beans de este tipo, por esto es necesario incluir la anotación @Qualifier(“dsSlave”)
    @Bean(name = "jdbcSlave")
    @Autowired
    public JdbcTemplate slaveJdbcTemplate(@Qualifier("dsSlave") DataSource dsSlave) {
        return new JdbcTemplate(dsSlave);
    }

    @Bean(name = "jdbcMaster")
    @Autowired
    public JdbcTemplate masterJdbcTemplate(@Qualifier("dsMaster") DataSource dsMaster) {
        return new JdbcTemplate(dsMaster);
    }

}
