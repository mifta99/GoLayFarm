package mifta.project.id.golayfarm;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

public class adapterListKambing extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private LayoutInflater inflater;
    private ArrayList<HashMap<String, String>> data;
    ArrayList<HashMap<String, String>>tampil = new ArrayList<HashMap<String, String>>();

    //deklarasi pengambilan data
    public adapterListKambing (Context context, ArrayList<HashMap<String, String>> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    //pendeklarasian item layout
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_list_kambing, null);
        MyHolder holder = new MyHolder(v);

        return holder;
    }

    //Penampilan data recycleriew
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MyHolder myHolder = (MyHolder) holder;

        HashMap<String, String> data_tampil = new HashMap<String, String>();
        data_tampil = data.get(position);

        Glide.with(context)
                .load(data_tampil.get(koneksi.key_foto))
                .into(myHolder.foto);
        myHolder.id.setText(data_tampil.get(koneksi.key_id));
        myHolder.idkam.setText(data_tampil.get(koneksi.key_idKambing));
        myHolder.kelamin.setText(data_tampil.get(koneksi.key_kelamin));
        myHolder.brtawal.setText(data_tampil.get(koneksi.key_beratAwal) + " Kg");
        myHolder.hargabeli.setText("Rp. "+data_tampil.get(koneksi.key_hargaBeli));
        myHolder.tglmasuk.setText(data_tampil.get(koneksi.key_tgl_masuk));

        myHolder.crd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), inputDataKambing.class);
                Integer idKambingIntent = Integer.valueOf(myHolder.id.getText().toString());
                intent.putExtra("idKambing", idKambingIntent);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        TextView idkam, kelamin, brtawal, hargabeli, tglmasuk, id;
        ImageView foto;
        CardView crd;
        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);

            crd=(CardView)itemView.findViewById(R.id.crd_listKambing);
            foto = (ImageView) itemView.findViewById(R.id.imgx_foto);
            idkam = (TextView) itemView.findViewById(R.id.tvx_idKambing);
            id = (TextView) itemView.findViewById(R.id.tvx_id);
            kelamin = (TextView) itemView.findViewById(R.id.tvx_kelamin);
            brtawal=(TextView)itemView.findViewById(R.id.tvx_berat);
            hargabeli=(TextView)itemView.findViewById(R.id.tvx_hargaBeli);
            tglmasuk=(TextView)itemView.findViewById(R.id.tvx_tgl);

        }

    }
}