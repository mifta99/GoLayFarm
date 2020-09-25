package mifta.project.id.golayfarm;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class adapterListPenjualan extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private LayoutInflater inflater;
    private ArrayList<HashMap<String, String>> data;
    ArrayList<HashMap<String, String>>tampil = new ArrayList<HashMap<String, String>>();
    //deklarasi pengambilan data
    public adapterListPenjualan (Context context, ArrayList<HashMap<String, String>> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    //pendeklarasian item layout
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_list_penjualan, null);
        MyHolder holder = new MyHolder(v);

        return holder;
    }

    //Penampilan data recycleriew
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MyHolder myHolder = (MyHolder) holder;

        HashMap<String, String> data_tampil = new HashMap<String, String>();
        data_tampil = data.get(position);

        myHolder.idjual.setText(data_tampil.get(koneksi.key_idHasil));
        myHolder.idkam.setText(data_tampil.get(koneksi.key_idKambing));
        myHolder.status.setText(data_tampil.get(koneksi.key_status));
        myHolder.berat.setText(data_tampil.get(koneksi.key_beratAkhir) + " Kg" );
        myHolder.harga.setText("Rp. "+data_tampil.get(koneksi.key_hargaJual));
        myHolder.tgl.setText(data_tampil.get(koneksi.key_tglKeluar));
        myHolder.sj.setText(data_tampil.get(koneksi.key_status_jual));

        myHolder.crd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), inputDataHasil.class);
                Integer id = Integer.valueOf(myHolder.idjual.getText().toString());
                String idKambing = myHolder.idkam.getText().toString();
                intent.putExtra("id_jual", id);
                intent.putExtra("id_kambing", idKambing);
                context.startActivity(intent);
            }
        });

        if (myHolder.status.getText().toString().equals("Target Panen Tercapai")){
            myHolder.color.setBackgroundColor(Color.parseColor("#32A336"));
        }else if (myHolder.status.getText().toString().equals("Target Panen Tidak Tercapai")){
            myHolder.color.setBackgroundColor(Color.parseColor("#FF0000"));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        TextView idjual, idkam, status, berat, harga, tgl, sj;
        CardView crd;
        LinearLayout color;
        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);

            color=(LinearLayout)itemView.findViewById(R.id.lay_warna);
            crd=(CardView)itemView.findViewById(R.id.crd_listJual);
            idjual = (TextView) itemView.findViewById(R.id.tvx_idJual);
            idkam = (TextView) itemView.findViewById(R.id.tvx_idKambingJ);
            status = (TextView) itemView.findViewById(R.id.tvx_statusJ);
            berat=(TextView)itemView.findViewById(R.id.tvx_beratAkhirJ);
            harga=(TextView)itemView.findViewById(R.id.tvx_hargaJual);
            tgl=(TextView)itemView.findViewById(R.id.tvx_tglJual);
            sj=(TextView)itemView.findViewById(R.id.tvx_statusJual);

        }

    }
}
