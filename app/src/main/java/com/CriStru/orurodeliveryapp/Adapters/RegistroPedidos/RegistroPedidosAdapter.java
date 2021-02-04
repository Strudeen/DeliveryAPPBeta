package com.CriStru.orurodeliveryapp.Adapters.RegistroPedidos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.CriStru.orurodeliveryapp.Models.Pedido;
import com.CriStru.orurodeliveryapp.R;

import java.util.ArrayList;

public class RegistroPedidosAdapter extends ArrayAdapter<Pedido> {

    public RegistroPedidosAdapter(@NonNull Context context, int resource, ArrayList<Pedido> arrayPedido) {

        super(context, resource, arrayPedido);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {



        Pedido pedido = getItem(position);
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.registropedidos_item_card, parent, false);
            viewHolder.idregpedido = (TextView) convertView.findViewById(R.id.idregpedido);
            viewHolder.pedido_tv = (TextView) convertView.findViewById(R.id.tv_pedido);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.idregpedido.setText(pedido.getId());
        viewHolder.pedido_tv.setText(pedido.getDly());

        return convertView;
    }

    private static class ViewHolder {
        TextView idregpedido;
        TextView pedido_tv;
    }
}
