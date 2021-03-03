package com.example.panel1;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PanelListAdapter extends BaseAdapter implements Filterable {


    private Context context;
    private  int layout;
    private ArrayList<panel> panelsList;  //in video nu sunt private
    ArrayList<panel> filterList;
    CustomFilter filter;



    public PanelListAdapter(Context context, int layout, ArrayList<panel> panelsList) {
        this.context = context;
        this.layout = layout;
        this.panelsList = panelsList;
        this.filterList=panelsList;
    }

    @Override
    public int getCount() {
        return panelsList.size();
    }

    @Override
    public Object getItem(int position) {
        return panelsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
        //indexofpanellist
    }



    private class ViewHolder{
        ImageView imageView;
        TextView txtnume, txtpromotie,txtclasa;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View row = view;
        ViewHolder holder = new ViewHolder();

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.txtnume = row.findViewById(R.id.txtnume);
            holder.txtpromotie = row.findViewById(R.id.txtpromotie);
            holder.txtclasa =  row.findViewById(R.id.txtclasa);
            holder.imageView = row.findViewById(R.id.imgPanel);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        panel panel = panelsList.get(position);

        holder.txtnume.setText(panel.getNume());
        holder.txtpromotie.setText(panel.getPromotie());

        byte[] panelImage = panel.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(panelImage, 0, panelImage.length);
        holder.imageView.setImageBitmap(bitmap);

        return row;
    }

    @Override
    public Filter getFilter() {
        if(filter==null)
        {
            filter=new CustomFilter();
        }
        return filter;
    }

    //INNER CLASS
    class CustomFilter extends Filter
    {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results=new FilterResults();

            if(constraint !=null && constraint.length()>0 ) {

                //CONSTRAINT TO UPPER
                constraint = constraint.toString().toUpperCase();

                ArrayList<panel> filters = new ArrayList<panel>();

                //FILTERING

                for (int i = 0; i < filterList.size(); i++) {
                    if (filterList.get(i).getClasa().contains(constraint) || filterList.get(i).getPromotie().contains(constraint) ) {
                        panel pas = new panel(filterList.get(i).getNume(),filterList.get(i).getPromotie(),filterList.get(i).getClasa(), filterList.get(i).getImage(),filterList.get(i).getId());

                        filters.add(pas);
                    }
                }
                results.count = filters.size();
                results.values = filterList;
            }
            else
            {
                results.count=filterList.size();
                results.values=filterList;
            }



            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            panelsList=(ArrayList<panel>) results.values;
            notifyDataSetChanged();


        }
    }


}
