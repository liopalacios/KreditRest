package com.servicekerdit.repository.dao;

import com.servicekerdit.entity.ClienteCasaCambio;
import com.servicekerdit.repository.ClienteCasaCambioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ClineteCasaCambioDao implements ClienteCasaCambioRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClineteCasaCambioDao.class);
    @Autowired
    @Qualifier("jdbcSlave")
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<ClienteCasaCambio> findByNumdoc(String numdoc, Connection connection) throws SQLException {
        PreparedStatement ps = null;
        List<ClienteCasaCambio> clienteCasaCambios = new ArrayList<>();
        ClienteCasaCambio clienteCasaCambio = null;
        ps = connection.prepareStatement("select id_cliente_casacambio, numdoc, razon_social, id_tipo_documento " +
                "from t_cliente_casacambio ccc " +
                " where numdoc = ? ");
        ps.setString(1,numdoc);
        /*return jdbcTemplate.query("select id_cliente_casacambio, numdoc, razon_social, id_tipo_documento " +
                "from t_cliente_casacambio ccc " +
                " where numdoc ='"+numdoc+"'" +
                ";",
                new RowMapper<ClienteCasaCambio>() {
                    @Override
                    public ClienteCasaCambio mapRow(ResultSet rs, int arg1) throws SQLException {
                    return new ClienteCasaCambio(rs.getInt("id_cliente_casacambio"), rs.getString("numdoc"),rs.getString("razon_social"),
                        rs.getInt("id_tipo_documento"));
                }

        });*/
        LOGGER.info(ps.toString());
        try {
            ResultSet rst = ps.executeQuery();
            while (rst.next()) {
                clienteCasaCambio = new ClienteCasaCambio();
                clienteCasaCambio.setIdClienteCasacambio(rst.getInt("id_cliente_casacambio"));
                clienteCasaCambio.setNumDocu(rst.getString("numdoc"));
                clienteCasaCambio.setNumDocu(rst.getString("razon_social"));
                clienteCasaCambio.setIdTipoDocumento(rst.getInt("id_tipo_documento"));
                clienteCasaCambios.add(clienteCasaCambio);
            }
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        }finally {
            connection.close();
        }
        return clienteCasaCambios;
    }


}
