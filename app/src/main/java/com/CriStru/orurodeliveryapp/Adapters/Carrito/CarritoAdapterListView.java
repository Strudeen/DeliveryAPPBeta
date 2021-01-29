package com.CriStru.orurodeliveryapp.Adapters.Carrito;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.CriStru.orurodeliveryapp.Adapters.Categorias.ItemClickSupport;
import com.CriStru.orurodeliveryapp.DataTransferInterface;
import com.CriStru.orurodeliveryapp.Models.Carrito;
import com.CriStru.orurodeliveryapp.R;
import com.bumptech.glide.Glide;
import com.orm.SugarContext;

import java.util.ArrayList;
import java.util.List;

public class CarritoAdapterListView  extends ArrayAdapter<Carrito> {


    DataTransferInterface dtInterface;
    private ArrayList<Carrito> carritoArrayList;
    private Context context;
    private int resource;
    public float precioTotal = 0f;


    public CarritoAdapterListView( Context context, ArrayList<Carrito> carrito, DataTransferInterface dataTransferInterface) {
        super(context, R.layout.carrito_card, carrito);
        this.carritoArrayList = carrito;
        this.context = context;
        this.dtInterface = dataTransferInterface;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {



        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.carrito_card, null);


        TextView tvNombre,tvPrecio,tvIdProducto,tvStock,tvMaxStock;
        ImageView imageViewProducto;
        ImageView btnMas,btnMenos,btnEliminar;
        List<Carrito> carritos;


        SugarContext.init(context);
        tvIdProducto=(TextView) view.findViewById(R.id.tvIdCarrito);
        tvNombre=(TextView) view.findViewById(R.id.tvNombreCarritoCard);
        tvPrecio=(TextView) view.findViewById(R.id.tvPrecioCarritoCard);
        tvStock=(TextView) view.findViewById(R.id.tvStockCarritoCard);
        imageViewProducto=(ImageView) view.findViewById(R.id.imageViewCarritoCard);
        tvMaxStock = (TextView) view.findViewById(R.id.tvMaxStockCarritoCard);
        btnMas = (ImageView) view.findViewById(R.id.incrementar_button);
        btnMenos = (ImageView) view.findViewById(R.id.decrementar_button);
        btnEliminar = (ImageView) view.findViewById(R.id.borrar_button);

        tvIdProducto.setText(carritoArrayList.get(position).getIdProducto());
        tvNombre.setText(carritoArrayList.get(position).getNombre());
        tvPrecio.setText(String.valueOf(carritoArrayList.get(position).getPrecio())+ ".Bs");
        tvStock.setText("Stock: " + String.valueOf(carritoArrayList.get(position).getStock()));
        tvMaxStock.setText("Cantidad Actual: "+ carritoArrayList.get(position).getMaxStock());
        Glide.with(context).load(carritoArrayList.get(position).getFotoUrl()).into(imageViewProducto);



        Log.d("StockMax",""+carritoArrayList.get(position).getMaxStock());
        Log.d("Precio", ""+ carritoArrayList.get(position).getPrecio());

       // dtInterface.onSetValues(precioTotal);



        Log.d("PrecioTotal4: ", ""+precioTotal);

        Carrito data = getItem(position);


        //TODO Agregar un progress bar para que no se bugee el precio total

        btnMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Carrito producto;
                producto = Carrito.findById(Carrito.class,data.getId());
                if (producto.getMaxStock()<producto.getStock()){
                    int stockActual=producto.getMaxStock();
                    producto.setMaxStock(stockActual+1);
                    producto.save();
                    List<Carrito> data = Carrito.listAll(Carrito.class);
                    update(data);
                }else
                    Toast.makeText(context, "No puedes agregar más productos", Toast.LENGTH_SHORT).show();
            }
        });

        btnMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Carrito producto;
                producto = Carrito.findById(Carrito.class, data.getId());
                if (producto.getMaxStock() - 1 >= 1) {
                    int stockActual = producto.getMaxStock();
                    producto.setMaxStock(stockActual - 1);
                    producto.save();
                    List<Carrito> data = Carrito.listAll(Carrito.class);
                    update(data);
                } else {
                    Toast.makeText(context, "La cantidad debe ser almenos 1", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Carrito carrito = Carrito.findById(Carrito.class, data.getId());
                carrito.delete();
                List<Carrito> data = Carrito.listAll(Carrito.class);
                update(data);
            }
        });
       sumaPrecioTotal();
        return view;
    }


    public void update (List<Carrito> lista){
        carritoArrayList.clear();
        precioTotal = 0 ;
        carritoArrayList.addAll(lista);
        notifyDataSetChanged();
    }

    public void sumaPrecioTotal(){
        List<Carrito> data = Carrito.listAll(Carrito.class);
        float preciototalf= 0.0f;
        for (int i = 0 ; i<data.size() ; i++){
            preciototalf += data.get(i).getPrecio()*data.get(i).getMaxStock();
        }
        dtInterface.onSetValues(precioTotal);
    }

}
