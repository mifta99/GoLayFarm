package mifta.project.id.golayfarm;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class adapterListPenggemukan  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private LayoutInflater inflater;
    private ArrayList<HashMap<String, String>> data;
    ArrayList<HashMap<String, String>>tampil = new ArrayList<HashMap<String, String>>();
    //deklarasi pengambilan data
    public adapterListPenggemukan (Context context, ArrayList<HashMap<String, String>> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    //pendeklarasian item layout
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_list_penggemukan, null);
        MyHolder holder = new MyHolder(v);

        return holder;
    }

    //Penampilan data recycleriew
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MyHolder myHolder = (MyHolder) holder;

        HashMap<String, String> data_tampil = new HashMap<String, String>();
        data_tampil = data.get(position);

        myHolder.idgemuk.setText(data_tampil.get(koneksi.key_idGemuk));
        myHolder.idkam.setText(data_tampil.get(koneksi.key_idKambing));
        myHolder.berat.setText("Berat Sekarang : "+data_tampil.get(koneksi.key_berat)+" Kg");
        myHolder.tgl.setText(data_tampil.get(koneksi.key_tglTimbang));
        myHolder.kond.setText(data_tampil.get(koneksi.key_kondisi));

        myHolder.crd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), inputDataPenggemukan.class);
                Integer id = Integer.valueOf(myHolder.idgemuk.getText().toString());
                intent.putExtra("id_gemuk", id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        TextView idgemuk, idkam, berat, tgl,kond;
        CardView crd;
        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);

            crd=(CardView)itemView.findViewById(R.id.crd_listGemuk);
            idgemuk = (TextView) itemView.findViewById(R.id.tvx_idGemuk);
            idkam = (TextView) itemView.findViewById(R.id.tvx_idKambingG);
            berat = (TextView) itemView.findViewById(R.id.tvx_beratG);
            tgl=(TextView)itemView.findViewById(R.id.tvx_tglG);
            kond=(TextView)itemView.findViewById(R.id.tvx_kondisiG);

        }

    }
}