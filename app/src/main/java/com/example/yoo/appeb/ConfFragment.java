package com.example.yoo.appeb;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConfFragment extends Fragment {
    /*tvFI aqui esta el textview para mostrar la fecha inicial
    btnFI este es el boton de la fecha inicial

    tvFF aqui esta el textview para mostrar la fecha final
    btnFF este es el boton de la fecha final




     */
    ImageButton button;
    ImageButton buttonFF;
    EditText FI;
    EditText FF;
    String btnpres;
    Spinner spinner;
    SharedPreferences prefs;


    public ConfFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_conf, container, false);
    }

    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        final String date = new SimpleDateFormat("yyyy/MM/dd").format(new Date());

        prefs = getActivity().getSharedPreferences("MisPreferencias",getActivity().MODE_PRIVATE);


        button = (ImageButton)getView().findViewById(R.id.btnFI);
        buttonFF = (ImageButton)getView().findViewById(R.id.btnFF);
        FI = (EditText)getView().findViewById(R.id.editTextFI);
        FF = (EditText)getView().findViewById(R.id.editTextFF);

        String fechaInicio = prefs.getString("FI", "0");
        String fechaFin = prefs.getString("FF", "0");
        FI.setText(fechaInicio);
        FF.setText(fechaFin);

        List<tipos_graficas> items = new ArrayList<tipos_graficas>(3);
        items.add(new tipos_graficas(getString(R.string.gfbarras), R.drawable.ic_barras));
        items.add(new tipos_graficas(getString(R.string.gflineal), R.drawable.ic_lineal));
        items.add(new tipos_graficas(getString(R.string.gfpastel), R.drawable.ic_pastel));

        spinner = (Spinner) getView().findViewById(R.id.spinnerG);
        spinner.setAdapter(new tiposgraficasadapter(getActivity(),items));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                SharedPreferences.Editor editor = prefs.edit();
                if (position==0){
                    editor.putString("TipoGrafica", "Barras");
                } else if (position==1){
                    editor.putString("TipoGrafica", "Lineal");
                } else if (position==2){
                    editor.putString("TipoGrafica", "Pastel");
                }
                editor.commit();
                String tipoGrafica = prefs.getString("TipoGrafica", "0");
                //Toast.makeText(getContext()," TG: "+tipoGrafica,Toast.LENGTH_LONG ).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                //nothing
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
                btnpres="btnI";

            }
        });
        buttonFF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFFFragment = new SelectDateFragment();
                newFFFragment.show(getFragmentManager(), "DatePicker");
                btnpres="btnF";

            }
        });


    }

    public void getSystemService(String layoutInflaterService) {
    }

    public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, yy, mm, dd);
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm+1, dd);
        }
        public void populateSetDate(int year, int month, int day) {

            if(btnpres.equals("btnF")){
                FF.setText(day + "/" + month + "/" + year);
            }else {
                FI.setText(day + "/" + month + "/" + year);
            }

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("FI", FI.getText().toString());
            editor.putString("FF", FF.getText().toString());
            editor.commit();
            String fechaInicio = prefs.getString("FI", "0");
            String fechaFin = prefs.getString("FF", "0");
            String tipoGrafica = prefs.getString("TipoGrafica", "0");
            //Toast.makeText(getContext(),"FI: "+fechaInicio+" FF: "+fechaFin+" TG: "+tipoGrafica,Toast.LENGTH_LONG ).show();
        }

    }

}

