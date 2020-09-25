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

public class adapterListPakan extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private LayoutInflater inflater;
    private ArrayList<HashMap<String, String>> data;
    ArrayList<HashMap<String, String>>tampil = new ArrayList<HashMap<String, String>>();
    //deklarasi pengambilan data
    public adapterListPakan (Context context, ArrayList<HashMap<String, String>> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    //pendeklarasian item layout
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_list_pakan, null);
        MyHolder holder = new MyHolder(v);

        return holder;
    }

    //Penampilan data recycleriew
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MyHolder myHolder = (MyHolder) holder;

        HashMap<String, String> data_tampil = new HashMap<String, String>();
        data_tampil = data.get(position);

        myHolder.idpakan.setText(data_tampil.get(koneksi.key_idPakan));
        myHolder.idkam.setText(data_tampil.get(koneksi.key_idKambing));
        myHolder.namaPakan.setText(data_tampil.get(koneksi.key_namaPakan));
        myHolder.kuantitas.setText(data_tampil.get(koneksi.key_qtyPakan) + " " + data_tampil.get(koneksi.key_satuanPakan));
        myHolder.hargaPakan.setText("Rp. "+data_tampil.get(koneksi.key_hargaPakan));
        myHolder.tgl.setText(data_tampil.get(koneksi.key_tglPakan));

        myHolder.crd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), inputDataPakan.class);
                Integer id = Integer.valueOf(myHolder.idpakan.getText().toString());
                intent.putExtra("id_pakan", id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        TextView idpakan, idkam, namaPakan, kuantitas, hargaPakan, tgl;
        CardView crd;
        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);

            crd=(CardView)itemView.findViewById(R.id.crd_listPakan);
            idpakan = (TextView) itemView.findViewById(R.id.tvx_idPakan);
            idkam = (TextView) itemView.findViewById(R.id.tvx_idKambingP);
            namaPakan = (TextView) itemView.findViewById(R.id.tvx_namaPakan);
            kuantitas=(TextView)itemView.findViewById(R.id.tvx_qtySatP);
            hargaPakan=(TextView)itemView.findViewById(R.id.tvx_hargaPakan);
            tgl=(TextView)itemView.findViewById(R.id.tvx_tglP);

        }

    }
}
