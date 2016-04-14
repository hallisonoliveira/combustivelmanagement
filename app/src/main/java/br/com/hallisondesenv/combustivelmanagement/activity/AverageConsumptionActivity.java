package br.com.hallisondesenv.combustivelmanagement.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.NumberFormat;

import br.com.hallisondesenv.combustivelmanagement.R;
import br.com.hallisondesenv.combustivelmanagement.dao.AverageConsumptionDao;
import br.com.hallisondesenv.combustivelmanagement.dao.VehicleDataDao;
import br.com.hallisondesenv.combustivelmanagement.model.AverageConsumption;
import br.com.hallisondesenv.combustivelmanagement.model.VehicleData;
import br.com.hallisondesenv.combustivelmanagement.util.MaskUtil;

/**
 * Created by Hallison on 07/04/2016.
 */
public class AverageConsumptionActivity extends AppCompatActivity {

    private static final String TAG = "AverageConsumption";

    private EditText edtDate;
    private EditText edtAmountLiters;
    private EditText edtPriceByLiter;
    private EditText edtKilometer;
    private FloatingActionButton fabSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_average_consumption);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tbr_newAverageConsumption);
        setSupportActionBar(toolbar);

        initializeComponents();

    }

    private void initializeComponents(){

        edtDate = (EditText) findViewById(R.id.edt_newAverageConsumption_date);
        edtAmountLiters = (EditText) findViewById(R.id.edt_newAverageConsumption_amountLiters);
        edtKilometer = (EditText) findViewById(R.id.edt_newAverageConsumption_kilometer);

        edtPriceByLiter = (EditText) findViewById(R.id.edt_newAverageConsumption_priceByLiter);
        edtPriceByLiter.addTextChangedListener(MaskUtil.insertMask(edtPriceByLiter));

        fabSave = (FloatingActionButton) findViewById(R.id.btn_save_averageComsumption);
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAverageConsumption();
            }
        });
    }

    private boolean isEmptyFields(String date, String amountLiters, String priceByLiter, String kilometer) {
        if (TextUtils.isEmpty(date)) {
            edtDate.requestFocus();
            edtDate.setError(getResources().getString(R.string.error_required_field));
            return true;
        } else if (TextUtils.isEmpty(amountLiters)) {
            edtAmountLiters.requestFocus();
            edtAmountLiters.setError(getResources().getString(R.string.error_required_field));
            return true;
        } else if (TextUtils.isEmpty(priceByLiter)) {
            edtPriceByLiter.requestFocus();
            edtPriceByLiter.setError(getResources().getString(R.string.error_required_field));
        } else if (TextUtils.isEmpty(kilometer)) {
            edtKilometer.requestFocus();
            edtKilometer.setError(getResources().getString(R.string.error_required_field));
        }

        return false;
    }

    private void saveAverageConsumption(){

        if(!isEmptyFields(
                edtDate.getText().toString(),
                edtAmountLiters.getText().toString(),
                edtPriceByLiter.getText().toString(),
                edtKilometer.getText().toString())){

            Float priceByLiter = MaskUtil.convertStringToFloat(edtPriceByLiter.getText().toString());

            AverageConsumption averageConsumption = new AverageConsumption(
                    new AverageConsumption().getNextKey(this),
                    edtDate.getText().toString(),
                    Float.parseFloat(edtAmountLiters.getText().toString()),
                    priceByLiter,
                    Float.parseFloat(edtKilometer.getText().toString())
            );

//            try{

                VehicleDataDao vehicleDataDao = new VehicleDataDao();
                vehicleDataDao.fillTank(this);

                AverageConsumptionDao averageConsumptionDao = new AverageConsumptionDao();
                averageConsumptionDao.save(averageConsumption, this);
                Toast.makeText(this, "Média de consumo cadastrada com sucesso.", Toast.LENGTH_SHORT).show();

                this.finish();

//            } catch (Exception e){
//                Toast.makeText(this, "Houve um erro ao salvar os dados.", Toast.LENGTH_SHORT).show();
//                Log.e(TAG, e.getMessage());
//            }

        }

    }

}
