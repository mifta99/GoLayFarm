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

public class adapterListObat  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private LayoutInflater inflater;
    private ArrayList<HashMap<String, String>> data;
    ArrayList<HashMap<String, String>>tampil = new ArrayList<HashMap<String, String>>();
    //deklarasi pengambilan data
    public adapterListObat (Context context, ArrayList<HashMap<String, String>> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    //pendeklarasian item layout
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_list_obat, null);
        MyHolder holder = new MyHolder(v);

        return holder;
    }

    //Penampilan data recycleriew
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MyHolder myHolder = (MyHolder) holder;

        HashMap<String, String> data_tampil = new HashMap<String, String>();
        data_tampil = data.get(position);

        myHolder.idobat.setText(data_tampil.get(koneksi.key_idObat));
        myHolder.idkam.setText(data_tampil.get(koneksi.key_idKambing));
        myHolder.jenisObat.setText(data_tampil.get(koneksi.key_jenisObat));
        myHolder.kuantitas.setText(data_tampil.get(koneksi.key_qtyObat) + " " + data_tampil.get(koneksi.key_satuan));
        myHolder.hargaobat.setText("Rp. "+data_tampil.get(koneksi.key_hargaObat));
        myHolder.tgl.setText(data_tampil.get(koneksi.key_tglObat));

        myHolder.crd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), inputDataObat.class);
                Integer id = Integer.valueOf(myHolder.idobat.getText().toString());
                intent.putExtra("id_obat", id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

class MyHolder extends RecyclerView.ViewHolder {

    TextView idobat, idkam, jenisObat, kuantitas, hargaobat, tgl;
    CardView crd;
    // create constructor to get widget reference
    public MyHolder(View itemView) {
        super(itemView);

        crd=(CardView)itemView.findViewById(R.id.crd_listObat);
        idobat=(TextView)itemView.findViewById(R.id.tvx_idObat);
        idkam = (TextView) itemView.findViewById(R.id.tvx_idKambingO);
        jenisObat = (TextView) itemView.findViewById(R.id.tvx_jenisObatO);
        kuantitas=(TextView)itemView.findViewById(R.id.tvx_qtySatO);
        hargaobat=(TextView)itemView.findViewById(R.id.tvx_hargaObat);
        tgl=(TextView)itemView.findViewById(R.id.tvx_tglO);

    }

}
}
