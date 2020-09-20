package com.servicekerdit.repository.dao;

import com.servicekerdit.entity.MovimientoCasaCambio;
import com.servicekerdit.repository.MovimientoCasaCambioRepository;
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
import java.util.Date;
import java.util.List;

@Component
public class MovimientoCasaCambioDao implements MovimientoCasaCambioRepository {

    @Autowired
    @Qualifier("jdbcSlave")
    private JdbcTemplate jdbcTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(ClineteCasaCambioDao.class);
    @Override
    public List<MovimientoCasaCambio> findAll() {
        return jdbcTemplate.query("select mc.id_movimiento_casacambio,mc.id_caja,mc.idusuario,mc.id_tipo_operacion,mc.cantidad_cambiar," +
                "mc.cliente_recibe,mc.fisico,mc.orden_operacion,mc.id_operacion_padre,mc.id_moneda_destino,mc.id_moneda_origen,mc.nro_ticket," +
                "mc.num_docu,mc.fecha_reg,mc.fecha_default,mc.tipo_cambio,mc.idusuario,mc.estadoenv,mc.estadomnsag,mc.mnsagsunat," +
                "mc.linkticket,mc.linkpdf,mc.linkxml,mc.idvoucher,mc.id_tipo_documento,usu.login,tope.descripcion,timo.signo " +
                "from t_movimiento_casacambio mc " +
                "inner join t_usuario usu on usu.idusuario=mc.idusuario " +
                "inner join t_tipo_operacion tope on tope.id_tipo_operacion=mc.id_tipo_operacion " +
                "left join t_tipo_moneda timo on timo.id_tipo_moneda=mc.id_moneda_origen " +
                "order by mc.id_movimiento_casacambio desc " +
                "OFFSET " +
                "LIMIT ", new RowMapper<MovimientoCasaCambio>() {

            @Override
            public MovimientoCasaCambio mapRow(ResultSet rs, int arg1) throws SQLException {
                return new MovimientoCasaCambio(rs.getInt("id_movimiento_casacambio"), rs.getInt("id_caja"),
                        rs.getInt("idusuario"),rs.getInt("id_tipo_operacion"),
                        rs.getBigDecimal("cantidad_cambiar"),rs.getDouble("fisico"),
                        rs.getBigDecimal("cliente_recibe"),
                        rs.getInt("id_operacion_padre"),rs.getInt("id_moneda_destino"),rs.getInt("id_moneda_origen"),
                        rs.getString("nro_ticket"),rs.getString("num_docu"),
                        rs.getDate("fecha_reg"),rs.getDate("fecha_default"),
                        rs.getDouble("tipo_cambio"),
                        rs.getInt("idusuario"),rs.getInt("estadoenv"),rs.getInt("estadomnsag"),
                        rs.getString("mnsagsunat"),
                        rs.getString("linkticket"),rs.getString("linkpdf"),rs.getString("linkxml"),
                        rs.getString("descripcion"),rs.getInt("idvoucher"),rs.getInt("id_tipo_documento"));
            }

        });
    }

    @Override
    public List<MovimientoCasaCambio> findFecRegNoEnv(String date1,int page, int limite) {
        return jdbcTemplate.query("select mc.id_movimiento_casacambio,mc.id_caja,mc.idusuario,mc.id_tipo_operacion,mc.cantidad_cambiar," +
                "mc.cliente_recibe,mc.fisico,mc.orden_operacion,mc.id_operacion_padre,mc.id_moneda_destino,mc.id_moneda_origen,mc.nro_ticket," +
                "mc.num_docu,mc.fecha_reg,mc.fecha_default,mc.tipo_cambio,mc.idusuario,mc.estadoenv,mc.estadomnsag,mc.mnsagsunat," +
                "mc.linkticket,mc.linkpdf,mc.linkxml,mc.idvoucher,mc.id_tipo_documento,usu.login,tope.descripcion,timo.signo " +
                "from t_movimiento_casacambio mc " +
                "inner join t_usuario usu on usu.idusuario=mc.idusuario " +
                "inner join t_tipo_operacion tope on tope.id_tipo_operacion=mc.id_tipo_operacion " +
                "left join t_tipo_moneda timo on timo.id_tipo_moneda=mc.id_moneda_origen " +
                " where fecha_reg='"+date1+"' AND estado_ticket like '%CONFORME%'  " +
                " order by mc.id_movimiento_casacambio desc;", new RowMapper<MovimientoCasaCambio>() {

            @Override
            public MovimientoCasaCambio mapRow(ResultSet rs, int arg1) throws SQLException {
                return new MovimientoCasaCambio(rs.getInt("id_movimiento_casacambio"), rs.getInt("id_caja"),
                        rs.getInt("idusuario"),rs.getInt("id_tipo_operacion"),
                        rs.getBigDecimal("cantidad_cambiar"),rs.getDouble("fisico"),
                        rs.getBigDecimal("cliente_recibe"),
                        rs.getInt("id_operacion_padre"),rs.getInt("id_moneda_destino"),rs.getInt("id_moneda_origen"),
                        rs.getString("nro_ticket"),rs.getString("num_docu"),
                        rs.getDate("fecha_reg"),rs.getDate("fecha_default"),
                        rs.getDouble("tipo_cambio"),
                        rs.getInt("idusuario"),rs.getInt("estadoenv"),rs.getInt("estadomnsag"),
                        rs.getString("mnsagsunat"),
                        rs.getString("linkticket"),rs.getString("linkpdf"),rs.getString("linkxml"),
                        rs.getString("descripcion"),rs.getInt("idvoucher"),rs.getInt("id_tipo_documento"));
            }

        });
    }

    @Override
    public int countCasacambioByDate(int userid, String date1, Connection connectionb) throws SQLException {
        PreparedStatement ps = null;
        Date date = new Date();
        int result = 0;
        /*String sql = "SELECT count(mc.id_movimiento_casacambio) FROM t_movimiento_casacambio mc " +
                "inner join t_usuario usu on usu.idusuario = mc.idusuario WHERE mc.fecha_reg = ? " +
                " and mc.id_tipo_operacion IN(10,11,15,14) AND estado_ticket like '%CONFORME%' and id_caja = ? " +
                "  ";*/
        ps = connectionb.prepareStatement("SELECT count(mc.id_movimiento_casacambio) FROM t_movimiento_casacambio mc " +
                "inner join t_usuario usu on usu.idusuario = mc.idusuario WHERE mc.fecha_reg = to_date( ? , 'YYYY-MM-DD') " +
                " and mc.id_tipo_operacion IN(10,11,15,14) AND estado_ticket like '%CONFORME%' and id_caja = ? " );

        ps.setString(1,date1);
        ps.setInt(2,userid);
        LOGGER.info(ps.toString());
        try {
            ResultSet rst = ps.executeQuery();
            rst.next();
            result = rst.getInt(1);
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        }finally {
            connectionb.close();
        }
        //int count = this.jdbcTemplate.queryForObject(sql,new Object[]{date, userid},Integer.class);

        /*if (count > 0) {
            result = count;
        }*/
        return result;


    }

    @Override
    public List<MovimientoCasaCambio> findFecReg(java.sql.Date format, int ncaja, int page, int limite, Connection connection) throws SQLException {
        PreparedStatement ps = null;
        List<MovimientoCasaCambio> movimientoCasaCambios = new ArrayList<>();
        MovimientoCasaCambio movimientoCasaCambio = null;
        ps = connection.prepareStatement("select mc.id_movimiento_casacambio,mc.id_caja,mc.idusuario,mc.id_tipo_operacion,mc.cantidad_cambiar," +
                "mc.cliente_recibe,mc.fisico,mc.orden_operacion,mc.id_operacion_padre,mc.id_moneda_destino,mc.id_moneda_origen,mc.nro_ticket," +
                "mc.num_docu,mc.fecha_reg,mc.fecha_default,mc.tipo_cambio,mc.idusuario," +
                "mc.estadoenv,mc.estadomnsag,mc.mnsagsunat," +
                "mc.linkticket,mc.linkpdf,mc.linkxml,mc.idvoucher,mc.id_tipo_documento,usu.login,tope.descripcion,timo.signo " +
                "from t_movimiento_casacambio mc " +
                "inner join t_usuario usu on usu.idusuario=mc.idusuario " +
                "inner join t_tipo_operacion tope on tope.id_tipo_operacion=mc.id_tipo_operacion " +
                "left join t_tipo_moneda timo on timo.id_tipo_moneda=mc.id_moneda_origen " +
                " where fecha_reg >= ? " +
                " and mc.id_tipo_operacion IN (10,11,15,14) AND estado_ticket like '%CONFORME%' and id_caja = ? " +
                " order by mc.id_movimiento_casacambio desc" +
                " OFFSET ? " +
                " LIMIT  ? " +
                ";");
            ps.setDate(1,format);
            ps.setInt(2,ncaja);
            ps.setInt(3,page);
            ps.setInt(4,limite);
        LOGGER.info(ps.toString());
        try {
            ResultSet rst = ps.executeQuery();
            while (rst.next()) {
                movimientoCasaCambio = new MovimientoCasaCambio();
                movimientoCasaCambio.setIdMovimientoCasacambio(rst.getInt("id_movimiento_casacambio"));
                movimientoCasaCambio.setIdCaja(rst.getInt("id_caja"));
                movimientoCasaCambio.setIdusuario(rst.getInt("idusuario"));
                movimientoCasaCambio.setIdTipoOperacion(rst.getInt("id_tipo_operacion"));
                movimientoCasaCambio.setCantidadCambiar(rst.getBigDecimal("cantidad_cambiar"));
                movimientoCasaCambio.setClienteRecibe(rst.getBigDecimal("cliente_recibe"));
                movimientoCasaCambio.setFisico(rst.getDouble("fisico"));
                movimientoCasaCambio.setIdOperacionPadre(rst.getInt("id_operacion_padre"));
                movimientoCasaCambio.setIdMonedaDestino(rst.getInt("id_moneda_destino"));
                movimientoCasaCambio.setIdMonedaOrigen(rst.getInt("id_moneda_origen"));
                movimientoCasaCambio.setNroTicket(rst.getString("nro_ticket"));
                movimientoCasaCambio.setNumDocu(rst.getString("num_docu"));
                movimientoCasaCambio.setFechaReg(rst.getDate("fecha_reg"));
                movimientoCasaCambio.setFechaDefault(rst.getDate("fecha_default"));
                movimientoCasaCambio.setTipoCambio(rst.getInt("tipo_cambio"));
                movimientoCasaCambio.setEstadoenv(rst.getInt("estadoenv"));
                movimientoCasaCambio.setEstadomnsag(rst.getInt("estadomnsag"));
                movimientoCasaCambio.setMnsagsunat(rst.getString("mnsagsunat"));
                movimientoCasaCambio.setLinkticket(rst.getString("linkticket"));
                movimientoCasaCambio.setLinkpdf(rst.getString("linkpdf"));
                movimientoCasaCambio.setLinkxml(rst.getString("linkxml"));

                movimientoCasaCambio.setTipoNroDocCli(rst.getInt("id_tipo_documento"));
                movimientoCasaCambios.add(movimientoCasaCambio);
            }
            }catch (Exception e){
                LOGGER.error(e.getMessage());
                e.printStackTrace();
            }finally {
                connection.close();
            }
        return movimientoCasaCambios;
        /*return jdbcTemplate.query( new RowMapper<MovimientoCasaCambio>() {

            @Override
            public MovimientoCasaCambio mapRow(ResultSet rs, int arg1) throws SQLException {
                return new MovimientoCasaCambio(rs.getInt("id_movimiento_casacambio"), rs.getInt("id_caja"),
                        rs.getInt("idusuario"),rs.getInt("id_tipo_operacion"),
                        rs.getBigDecimal("cantidad_cambiar"),rs.getDouble("fisico"),
                        rs.getBigDecimal("cliente_recibe"),
                        rs.getInt("id_operacion_padre"),rs.getInt("id_moneda_destino"),rs.getInt("id_moneda_origen"),
                        rs.getString("nro_ticket"),rs.getString("num_docu"),
                        rs.getDate("fecha_reg"),rs.getDate("fecha_default"),
                        rs.getDouble("tipo_cambio"),
                        rs.getInt("idusuario"),rs.getInt("estadoenv"),rs.getInt("estadomnsag"),
                        rs.getString("mnsagsunat"),
                        rs.getString("linkticket"),rs.getString("linkpdf"),rs.getString("linkxml"),
                        rs.getString("descripcion"),rs.getInt("idvoucher"),rs.getInt("id_tipo_documento"));
            }

        });*/
    }

    @Override
    public List<MovimientoCasaCambio> findNoSend(Date format) {

        return jdbcTemplate.query("select mc.id_movimiento_casacambio,mc.id_caja,mc.idusuario,mc.id_tipo_operacion,mc.cantidad_cambiar," +
                "mc.cliente_recibe,mc.fisico,mc.orden_operacion,mc.id_operacion_padre,mc.id_moneda_destino,mc.id_moneda_origen,mc.nro_ticket," +
                "mc.num_docu,mc.fecha_reg,mc.fecha_default,mc.tipo_cambio,mc.idusuario,mc.estadoenv,mc.estadomnsag,mc.mnsagsunat," +
                "mc.linkticket,mc.linkpdf,mc.linkxml,mc.idvoucher,mc.id_tipo_documento,usu.login,tope.descripcion,timo.signo " +
                "from t_movimiento_casacambio mc " +
                "inner join t_usuario usu on usu.idusuario=mc.idusuario " +
                "inner join t_tipo_operacion tope on tope.id_tipo_operacion=mc.id_tipo_operacion " +
                "left join t_tipo_moneda timo on timo.id_tipo_moneda=mc.id_moneda_origen " +
                " where fecha_reg >='"+format+"'" +
                " and mc.id_tipo_operacion IN(10,11,15,14) AND estado_ticket like '%CONFORME%' " +
                "and estadoenv = 0;", new RowMapper<MovimientoCasaCambio>() {

            @Override
            public MovimientoCasaCambio mapRow(ResultSet rs, int arg1) throws SQLException {
                return new MovimientoCasaCambio(rs.getInt("id_movimiento_casacambio"), rs.getInt("id_caja"),
                        rs.getInt("idusuario"),rs.getInt("id_tipo_operacion"),
                        rs.getBigDecimal("cantidad_cambiar"),rs.getDouble("fisico"),
                        rs.getBigDecimal("cliente_recibe"),
                        rs.getInt("id_operacion_padre"),rs.getInt("id_moneda_destino"),rs.getInt("id_moneda_origen"),
                        rs.getString("nro_ticket"),rs.getString("num_docu"),
                        rs.getDate("fecha_reg"),rs.getDate("fecha_default"),
                        rs.getDouble("tipo_cambio"),
                        rs.getInt("idusuario"),rs.getInt("estadoenv"),rs.getInt("estadomnsag"),
                        rs.getString("mnsagsunat"),
                        rs.getString("linkticket"),rs.getString("linkpdf"),rs.getString("linkxml"),
                        rs.getString("descripcion"),rs.getInt("idvoucher"),rs.getInt("id_tipo_documento"));
            }

        });
    }


    @Override
    public void save(MovimientoCasaCambio movimientoCasaCambio) {
        this.jdbcTemplate.update(
                "update t_movimiento_casacambio set estadoenv = 1, estadomnsag = 1, mnsagsunat = ? , linkticket = ? , " +
                        " linkpdf = ? , linkxml = ? , idvoucher = ?  " +
                        " where id_movimiento_casacambio = ?",
                movimientoCasaCambio.getMnsagsunat(), movimientoCasaCambio.getLinkticket(), movimientoCasaCambio.getLinkpdf(),
                movimientoCasaCambio.getLinkxml(),
                movimientoCasaCambio.getIdvoucher(), movimientoCasaCambio.getIdMovimientoCasacambio());
    }
}



