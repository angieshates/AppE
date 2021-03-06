package com.example.yoo.appeb;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.yoo.appeb.R.id.cantidad;


/**
 * A simple {@link Fragment} subclass.
 */
public class addGasto extends Fragment {
    String addURL = "http://webcolima.com/wsecomapping/addGasto.php";
    String getURL = "http://webcolima.com/wsecomapping/catalogogastos.php";

    Spinner spinner;
    //ArrayList<String> listaCatGastos= new ArrayList<String>();
    ArrayList<spnconcept> listaCatGastos = new ArrayList<spnconcept>();
    //ArrayAdapter<String> dataAdapter;
    spnconcept_adapter dataAdapter;
    RequestQueue requestQueueSend;
    RequestQueue requestQueueGetCatGastos;
    String user;
    EditText cantidadD;
    String costo;
    String idconcepto;
    EditText precioConcept;

    public addGasto() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences prefs = getActivity().getSharedPreferences("MisPreferencias",getActivity().MODE_PRIVATE);
        String usuario = prefs.getString("User", "0");
        user = usuario;
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_gasto, container, false);
        //text = (TextView) view.findViewById(R.id.Productos);
        super.onCreate(savedInstanceState);
        return view;
    }
   // @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        cantidadD = (EditText) getView().findViewById(R.id.cantidad);
        precioConcept= (EditText) getView().findViewById(R.id.pConcept);


        listaCatGastos.clear();
        requestQueueGetCatGastos= Volley.newRequestQueue(getContext());
        //listaA(la);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                getURL ,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)  {
                try {
                    JSONArray alumnos = response.getJSONArray("all");
                    for (int i = 0; i < alumnos.length(); i++) {

                        JSONObject alumno = alumnos.getJSONObject(i);
                        String idC = alumno.getString("id_concepto");
                        String nombre = alumno.getString("nombre");
                        String costo = alumno.getString("costo");
                        String usuario = alumno.getString("id_usuario");
                        if (usuario.equals(user)){
                            listaCatGastos.add(new spnconcept(idC,nombre,costo));

                        };
                        //listaA[i]=idaula;
                    }
                    dataAdapter.notifyDataSetChanged();

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.append(error.getMessage());

            }
        });

        requestQueueGetCatGastos.add(jsonObjectRequest);

        dataAdapter = new spnconcept_adapter(getActivity(), listaCatGastos);


        spinner = (Spinner)getView().findViewById(R.id.spinnerGastos);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                //String cost=listaCatGastos.get(position).getPreciosp();
                costo=listaCatGastos.get(position).getPreciosp();
                idconcepto = listaCatGastos.get(position).getIdconceptosp();
                precioConcept.setText(costo);
                //Toast.makeText(getActivity(),datos,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                //nothing
            }
        });


        Button addPP = (Button) getView().findViewById(R.id.button);
        addPP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewGasto();
            }
        });
    }


    public void addNewGasto(){


        requestQueueSend = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, addURL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(),"Gasto agregado con éxito",Toast.LENGTH_LONG ).show();
                cantidadD.setText("");
                GastoFragment fragment = new GastoFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction =
                        getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),error.toString(),Toast.LENGTH_LONG ).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                String text = spinner.getSelectedItem().toString();
                String[] gastos=text.split(",");

               // listaCatGastos.add(idC + "," + nombre+ "," +costo);
                String cant =cantidadD.getText().toString();

                //int tot = Integer.parseInt(cant)*Integer.parseInt(gastos[2]);
                int tot = Integer.parseInt(cant)*Integer.parseInt(costo);
                String date = new SimpleDateFormat("yyyy/MM/dd").format(new Date());

                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("id_usuario",user);
                parameters.put("cantidad", cant);
                //parameters.put("id_concepto", gastos[0]);
                parameters.put("id_concepto", idconcepto);
                parameters.put("fecha", date);
                parameters.put("total", String.valueOf(tot));




                //$fecha = $_POST["fecha"];

                return parameters;
            }
        };
        requestQueueSend.add(request);
    }

}
