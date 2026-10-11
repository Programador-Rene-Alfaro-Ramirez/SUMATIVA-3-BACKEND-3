package com.duoc.bancoservice.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class TransaccionEvent implements Serializable {
    private String idTransaccion;
    private String cuentaId;
    private BigDecimal monto;
    private String tipoOperacion;

    public TransaccionEvent() {}

    public TransaccionEvent(String idTransaccion, String cuentaId, BigDecimal monto, String tipoOperacion) {
        this.idTransaccion = idTransaccion;
        this.cuentaId = cuentaId;
        this.monto = monto;
        this.tipoOperacion = tipoOperacion;
    }

    public String getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(String idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public String getCuentaId() {
        return cuentaId;
    }

    public void setCuentaId(String cuentaId) {
        this.cuentaId = cuentaId;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getTipoOperacion() {
        return tipoOperacion;
    }

    public void setTipoOperacion(String tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }
}