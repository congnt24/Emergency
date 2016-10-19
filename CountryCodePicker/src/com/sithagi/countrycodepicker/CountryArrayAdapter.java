package com.sithagi.countrycodepicker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sithagi.countrycodepicker.R.drawable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class CountryArrayAdapter extends ArrayAdapter<Country> {
    private List<Country> countries;
    private List<Country> countries_all;
    private List<Country> suggestions;
    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((Country) resultValue).getName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint != null) {
                suggestions.clear();
                for (Country product : countries_all) {
                    if (product.getName().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        suggestions.add(product);
                    }
                }
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
            } else {
                filterResults.values = countries_all;
                filterResults.count = countries_all.size();
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            @SuppressWarnings("unchecked")
            ArrayList<Country> filteredList = (ArrayList<Country>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (Country c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };
    private LayoutInflater inflater;

    public CountryArrayAdapter(Context context, List<Country> countries) {
        super(context, 0, countries);
        this.countries = countries;
        countries_all = new ArrayList<>(countries);
        suggestions = new ArrayList<>();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return countries.size();
    }

    public Country getItem(int position) {
        return countries.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View cellView = convertView;
        Cell cell;
        Country country = countries.get(position);

        if (convertView == null) {
            cell = new Cell();
            cellView = inflater.inflate(R.layout.row, null);
            cell.textView = (TextView) cellView.findViewById(R.id.row_title);
            cell.imageView = (ImageView) cellView.findViewById(R.id.row_icon);
            cellView.setTag(cell);
        } else {
            cell = (Cell) cellView.getTag();
        }

        cell.textView.setText(country.getName());

        String drawableName = "flag_"
                + country.getCode().toLowerCase(Locale.ENGLISH);
        cell.imageView.setImageResource(getResId(drawableName));
        return cellView;
    }

    private int getResId(String drawableName) {

        try {
            Class<drawable> res = R.drawable.class;
            Field field = res.getField(drawableName);
            int drawableId = field.getInt(null);
            return drawableId;
        } catch (Exception e) {
            Log.e("CountryCodePicker", "Failure to get drawable id.", e);
        }
        return -1;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    public class Cell {
        public TextView textView;
        public ImageView imageView;
    }

}