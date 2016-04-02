package br.com.trainning.pdv_2016.domain.adapter;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.trainning.pdv_2016.R;
import br.com.trainning.pdv_2016.domain.model.ItemProduto;
import br.com.trainning.pdv_2016.domain.util.Base64Util;


/**
 * Created by elcio on 02/12/15.
 */
public class CustomArrayAdapter extends ArrayAdapter<ItemProduto> {
    protected LayoutInflater inflater;
    protected int layout;

    public CustomArrayAdapter(Activity activity, int resourceId, List<ItemProduto> objects){
        super(activity, resourceId, objects);
        layout = resourceId;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = inflater.inflate(layout, parent, false);
        TextView tv = (TextView)v.findViewById(R.id.item_label);
        ImageView foto = (ImageView)v.findViewById(R.id.fotoProduto);
        foto.setImageBitmap(Base64Util.decodeBase64(getItem(position).getFoto()));
        tv.setText(getItem(position).getDescricao()+" - "+getItem(position).getUnidade()+"\n Qtd: "+getItem(position).getQuantidade());
        return v;
    }
}