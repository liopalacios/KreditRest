package com.servicekerdit.repository.dao;

import com.servicekerdit.entity.MovimientoCasaCambio;
import com.servicekerdit.repository.MovimientoCasaCambioRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Component
public class MovimientoCasaCambioDao implements MovimientoCasaCambioRepository {

    @Autowired
    @Qualifier("jdbcSlave")
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<MovimientoCasaCambio> findAll() {
        return jdbcTemplate.query("select mc.id_movimiento_casacambio,mc.id_caja,mc.idusuario,mc.id_tipo_operacion,mc.cantidad_cambiar,mc.cliente_recibe," +
                "mc.orden_operacion,mc.nro_ticket,mc.fecha_reg,usu.login,tope.descripcion,timo.signo from t_movimiento_casacambio mc " +
                "inner join t_usuario usu on usu.idusuario=mc.idusuario " +
                "inner join t_tipo_operacion tope on tope.id_tipo_operacion=mc.id_tipo_operacion " +
                "left join t_tipo_moneda timo on timo.id_tipo_moneda=mc.id_moneda_origen " +
                "order by mc.id_movimiento_casacambio desc " +
                "OFFSET " +
                "LIMIT ", new RowMapper<MovimientoCasaCambio>() {

            @Override
            public MovimientoCasaCambio mapRow(ResultSet rs, int arg1) throws SQLException {
                return new MovimientoCasaCambio(rs.getInt("id_movimiento_casacambio"), rs.getInt("id_caja"),
                        rs.getInt("idusuario"),rs.getInt("id_tipo_operacion"),rs.getDouble("cantidad_cambiar"),
                        rs.getDouble("cliente_recibe"),rs.getInt("orden_operacion"),rs.getString("nro_ticket"),
                        rs.getDate("fecha_reg"),rs.getString("login"),rs.getString("descripcion"),rs.getString("signo"));
            }

        });
    }

    @Override
    public List<MovimientoCasaCambio> findFecRegNoEnv(String date1,int page, int limite) {
        return jdbcTemplate.query("select mc.id_movimiento_casacambio,mc.id_caja,mc.idusuario,mc.id_tipo_operacion,mc.cantidad_cambiar,mc.cliente_recibe,mc.orden_operacion,mc.nro_ticket,mc.fecha_reg,usu.login,tope.descripcion,timo.signo from t_movimiento_casacambio mc " +
                "inner join t_usuario usu on usu.idusuario=mc.idusuario " +
                "inner join t_tipo_operacion tope on tope.id_tipo_operacion=mc.id_tipo_operacion " +
                "left join t_tipo_moneda timo on timo.id_tipo_moneda=mc.id_moneda_origen " +
                " where fecha_reg='"+date1+"'" +
                " order by mc.id_movimiento_casacambio desc;", new RowMapper<MovimientoCasaCambio>() {

            @Override
            public MovimientoCasaCambio mapRow(ResultSet rs, int arg1) throws SQLException {
                return new MovimientoCasaCambio(rs.getInt("id_movimiento_casacambio"), rs.getInt("id_caja"), rs.getInt("idusuario"),
                        rs.getInt("id_tipo_operacion"),rs.getDouble("cantidad_cambiar"),rs.getDouble("cliente_recibe"),
                        rs.getInt("orden_operacion"),rs.getString("nro_ticket"),rs.getDate("fecha_reg"),
                        rs.getString("login"),rs.getString("descripcion"),rs.getString("signo"));
            }

        });
    }

    @Override
    public int countCasacambioByDate(String numdni, String date1) {

        Date date = new Date();

        String sql = "SELECT count(mc.id_movimiento_casacambio) FROM t_movimiento_casacambio mc " +
                "inner join t_usuario usu on usu.idusuario = mc.idusuario WHERE mc.fecha_reg = ?  and usu.dni = ? ";
        int result = 0;

        int count = this.jdbcTemplate.queryForObject(sql,new Object[]{date, numdni},Integer.class);

        if (count > 0) {
            result = count;
        }

        return result;


    }

    @Override
    public List<MovimientoCasaCambio> findFecReg(String format, int page, int limite) {
        return jdbcTemplate.query("select mc.id_movimiento_casacambio,mc.id_caja,mc.idusuario,mc.id_tipo_operacion,mc.cantidad_cambiar,mc.cliente_recibe,mc.orden_operacion,mc.nro_ticket,mc.fecha_reg,usu.login,tope.descripcion,timo.signo from t_movimiento_casacambio mc " +
                "inner join t_usuario usu on usu.idusuario=mc.idusuario " +
                "inner join t_tipo_operacion tope on tope.id_tipo_operacion=mc.id_tipo_operacion " +
                "left join t_tipo_moneda timo on timo.id_tipo_moneda=mc.id_moneda_origen " +
                " where fecha_reg='"+format+"'" +
                " order by mc.id_movimiento_casacambio desc" +
                " OFFSET " +page+
                " LIMIT " +limite+
                "" +
                ";", new RowMapper<MovimientoCasaCambio>() {

            @Override
            public MovimientoCasaCambio mapRow(ResultSet rs, int arg1) throws SQLException {
                return new MovimientoCasaCambio(rs.getInt("id_movimiento_casacambio"), rs.getInt("id_caja"), rs.getInt("idusuario"),
                        rs.getInt("id_tipo_operacion"),rs.getDouble("cantidad_cambiar"),rs.getDouble("cliente_recibe"),
                        rs.getInt("orden_operacion"),rs.getString("nro_ticket"),rs.getDate("fecha_reg"),
                        rs.getString("login"),rs.getString("descripcion"),rs.getString("signo"));
            }

        });
    }

    @Override
    public List<MovimientoCasaCambio> findNoSend(String format) {

        return jdbcTemplate.query("select mc.id_movimiento_casacambio,mc.id_caja,mc.idusuario,mc.id_tipo_operacion,mc.cantidad_cambiar,mc.cliente_recibe,mc.orden_operacion,mc.nro_ticket,mc.fecha_reg,usu.login,tope.descripcion,timo.signo from t_movimiento_casacambio mc " +
                "inner join t_usuario usu on usu.idusuario=mc.idusuario " +
                "inner join t_tipo_operacion tope on tope.id_tipo_operacion=mc.id_tipo_operacion " +
                "left join t_tipo_moneda timo on timo.id_tipo_moneda=mc.id_moneda_origen " +
                " where fecha_reg='"+format+"'" +
                " and estadoenv = 0;", new RowMapper<MovimientoCasaCambio>() {

            @Override
            public MovimientoCasaCambio mapRow(ResultSet rs, int arg1) throws SQLException {
                return new MovimientoCasaCambio(rs.getInt("id_movimiento_casacambio"), rs.getInt("id_caja"), rs.getInt("idusuario"),
                        rs.getInt("id_tipo_operacion"),rs.getDouble("cantidad_cambiar"),rs.getDouble("cliente_recibe"),
                        rs.getInt("orden_operacion"),rs.getString("nro_ticket"),rs.getDate("fecha_reg"),
                        rs.getString("login"),rs.getString("descripcion"),rs.getString("signo"));
            }

        });
    }
}



