package com.example.yoo.appeb;

import android.widget.ListAdapter;

/**
 * Created by angelicabarreda on 23/09/16.
 */

public class catalogoGastos_list  {
    private String nombreRubroCG;
    private String conceptoCG;
    private String costoCG;
    private String idCG;
    private String idRubro;

    public  catalogoGastos_list (String nombreRubroCG, String conceptoCG, String costoCG ,String idCG, String idRubro) {
        this.nombreRubroCG = nombreRubroCG;
        this.conceptoCG = conceptoCG;
        this.costoCG = costoCG;
        this.idCG = idCG;
        this.idRubro = idRubro;
    }

    public String getNombreRubroCG() {
        return nombreRubroCG;
    }

    public void setNombreRubroCG(String nombreRubroCG) {
        this.nombreRubroCG= nombreRubroCG;
    }

    public String getConceptoCG() {
        return conceptoCG;
    }

    public void setConceptoCG(String conceptoCG) {
        this.conceptoCG = conceptoCG;
    }

    public String getCostoCG() {
        return costoCG;
    }

    public void setCostoCG(String costoCG) {
        this.costoCG= costoCG;
    }

    public String getIdCG() {
        return idCG;
    }

    public void setIdCG(String idGD) {
        this.idCG= idCG;
    }

    public String getidRubro() {
        return idRubro;
    }

    public void setidRubro(String idRubro) {
        this.idRubro= idRubro;
    }
}
