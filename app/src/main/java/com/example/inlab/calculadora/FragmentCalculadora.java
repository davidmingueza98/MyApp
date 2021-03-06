package com.example.inlab.calculadora;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCalculadora extends Fragment {
    Button button0,button1,button2,button3,button4,button5,button6,button7,button8,button9;
    Button button_add,button_sub,button_div,button_mult,button_del,button_eq,button_coma,button_pow,button_sqrt;

    FloatingActionButton call;

    //Intent intent = new Intent(Intent: ACTION_DIAL, Uri.parse(NUMERO_TELEFONO)).

    //control booleans

    boolean equal = false;
    boolean op = false;
    boolean number_put = false;
    Double result=0.0;
    boolean nan = false;

    boolean first_zero = false;
    boolean first = true;

    boolean negative = false;
    boolean allow_negative = true;
    boolean point = false;
    boolean allow_coma = false;

    boolean sqrt = false;

    boolean first_negative = true;

    Double d=0.0;
    String num2 = "";
    String operation = "";
    TextView textViewResult;
    TextView textViewOp;

    String last="";

    public FragmentCalculadora() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_calculadora, container, false);
        // Inflate the layout for this fragment

        setHasOptionsMenu(true);

        button0 = view.findViewById(R.id.button16);
        button1 = view.findViewById(R.id.button11);
        button2 = view.findViewById(R.id.button12);
        button3 = view.findViewById(R.id.button13);
        button4 = view.findViewById(R.id.button10);
        button5 = view.findViewById(R.id.button4);
        button6 = view.findViewById(R.id.button8);
        button7 = view.findViewById(R.id.button3);
        button8 = view.findViewById(R.id.button5);
        button9 = view.findViewById(R.id.button6);
        button_add = view.findViewById(R.id.button18);
        button_sub = view.findViewById(R.id.button14);
        button_div = view.findViewById(R.id.button7);
        button_mult = view.findViewById(R.id.button9);
        button_del = view.findViewById(R.id.button17);
        button_eq = view.findViewById(R.id.button21);
        button_coma = view.findViewById(R.id.button15);
        button_pow = view.findViewById(R.id.button23);
        button_sqrt = view.findViewById(R.id.button2);
        call = view.findViewById(R.id.floatingActionButton);

        textViewResult = view.findViewById(R.id.textViewResult);
        textViewOp = view.findViewById(R.id.textViewOp);



        // OnClick botón número
        View.OnClickListener appendNumber = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number_put = true;
                Button b = (Button) view; // Castear la vista del onClick a botón


                String number = b.getText().toString();
                if (first && number.equals("0")) first_zero = true;

                if(!first_zero || first) {
                    if (first) {
                        first = false;
                        allow_coma=true;
                    }
                    textViewOp.append(b.getText());
                    num2 += number; // Leer el texto de un botón

                    if(negative) {
                        num2 = "-" + num2;
                        negative = false;
                    }

                    d = performOperation();

                     if ((op || sqrt) && !nan) {
                        //int d2 = d.intValue();
                        //textViewResult.setText(String.valueOf(d2));
                        textViewResult.setText(String.format("%.6f", d));
                        allow_negative = false;
                        equal = true;
                    }
                }
            }
        };

        button0.setOnClickListener(appendNumber);
        button1.setOnClickListener(appendNumber);
        button2.setOnClickListener(appendNumber);
        button3.setOnClickListener(appendNumber);
        button4.setOnClickListener(appendNumber);
        button5.setOnClickListener(appendNumber);
        button6.setOnClickListener(appendNumber);
        button7.setOnClickListener(appendNumber);
        button8.setOnClickListener(appendNumber);
        button9.setOnClickListener(appendNumber);



        // OnClick operator button except sqrt
        View.OnClickListener appendOperation = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button b = (Button) view; // Castear la vista del onClick a botón
                String aux = b.getText().toString(); // Leer el texto de un botón
                allow_coma=false; //despues de este boton nunca va la coma
                point=false;
                //caso pongo un menos al principio
                if(!number_put && aux.equals("-") && !last.equals("-") && allow_negative){
                    num2 = "-";
                    textViewOp.append(b.getText());
                    first = true;
                }
                else if (number_put && !sqrt && (!op || (aux.equals("-") && !last.equals("-") && allow_negative))){
                    if (op && aux.equals("-") && allow_negative) {
                        negative = true;
                        textViewOp.append(b.getText());
                    }
                    else{
                        operation = aux; // Leer el texto de un botón
                        textViewOp.append(b.getText());
                        num2 = "";
                        result = d;
                        first = true;
                        first_zero = false;
                        op = true;
                    }
                }
                last = aux;
            }
        };

        button_add.setOnClickListener(appendOperation);
        button_sub.setOnClickListener(appendOperation);
        button_div.setOnClickListener(appendOperation);
        button_mult.setOnClickListener(appendOperation);
        button_pow.setOnClickListener(appendOperation);

        //Clear and equal
        View.OnClickListener appendClear = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button b = (Button) view; // Castear la vista del onClick a botón
                String what = b.getText().toString(); // Leer el texto de un botón
                if (what.equals("AC")){
                    textViewOp.setText("");
                    textViewResult.setText("");
                    num2 = "";
                    result=0.0;
                    d=0.0;
                    operation = "";
                    number_put = false;
                    point = false;
                    allow_coma=false;
                    allow_negative=true;
                    equal = false;
                    op=false;
                    nan = false;
                    first = true;
                    first_zero=false;
                    sqrt = false;
                    negative=false;
                    first_negative = true;
                    last = "";
                }
                else if (equal){
                    if(nan) {
                        String error = "Division by 0, press AC";
                        textViewResult.setText(error);
                    }
                    else {
                        textViewOp.setText(textViewResult.getText().toString());
                        number_put = true;
                        textViewResult.setText("");
                        equal = false;
                        op = false;
                        point = false;
                        sqrt = false;
                        first_negative = true;
                        allow_negative = true;
                        operation = "";

                        if (d == 0) first_zero = true;
                        num2 = String.valueOf(d.intValue());
                        result = 0.0;
                    }
                }
            }
        };

        button_del.setOnClickListener(appendClear);
        button_eq.setOnClickListener(appendClear);

        // Point button
        button_coma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                if(!point && number_put && allow_coma){
                    if (first_zero) first_zero=false;
                    if (nan) nan = false;
                    point = true;
                    allow_coma = false; //no pongas dos comas seguidas
                    textViewOp.append(".");
                    num2 += '.';
                }
            }
        });

        //Sqrt button
        button_sqrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                if (first && !op && !number_put && !last.equals("-")) {
                    Button b = (Button) view; // Castear la vista del onClick a botón
                    textViewOp.append(b.getText());
                    sqrt = true;
                }
            }
        });

        //Call button
        call.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String aux = textViewOp.getText().toString();
                if (number_put && !point && !op && !sqrt) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + aux));
                    startActivity(intent);
                }
                else Toast.makeText(getActivity(),"Incorrect number", Toast.LENGTH_LONG).show();

            }
        });



        return view;
    }

    private Double performOperation() {
        Double d = Double.parseDouble(this.num2);
        if (operation.equals("+")) {
            d += result;
        } else if (operation.equals("-")) {
            d = result - d;
        } else if (Objects.equals("/", operation)) {
            if (d==0) {
                nan = true;
                equal = true;
            }
            else d = result / d;
        } else if (Objects.equals(operation, "x")) {
            d *= result;
        } else if (Objects.equals(operation, "^")) {
            d = Math.pow(result,d);
        } else if (sqrt){
            d = Math.sqrt(d);
        }
        return d;
    }

    //para salvar el estado en un giro, que no se hace, pero habría que cambiarlo para que funcionase en un Fragment

    @Override
    public void onSaveInstanceState(Bundle outstate){
        super.onSaveInstanceState(outstate);
        outstate.putString("result", textViewOp.getText().toString());
        outstate.putString("result2", textViewResult.getText().toString());
        //Log.v("result", t.getText().toString());
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore last state for checked position.
            textViewOp.setText(savedInstanceState.getString("result"));
            textViewResult.setText(savedInstanceState.getString("result2"));
        }
    }

    private void showDialog(){
        Bundle b = new Bundle();
        MyDialog m = new MyDialog(); //para el dialog informativo
        b.putString("texto", "Accumulative calculator. Square roots can't combine with other operations together. To call " +
                "put a number on the screen and press the call button. Watch out with very long numbers");
        m.setArguments(b);
        m.show(getFragmentManager(),"hola"); //show del dialog
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.help) {
            showDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
