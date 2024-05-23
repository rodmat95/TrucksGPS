package com.rodmat95.trucksgps.Main.Dashboard.Notification.Adapters;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rodmat95.trucksgps.Main.Dashboard.Notification.Models.Notification;
import com.rodmat95.trucksgps.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private int resource;
    private ArrayList<Notification> notificationsList;

    public NotificationAdapter(ArrayList<Notification> notificationsList, int resource) {
        this.notificationsList = notificationsList;
        this.resource = resource;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(resource, viewGroup, false);

        return new NotificationAdapter.ViewHolder(view);
    }

    /*@Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder viewHolder, int index) {
        Notification notification = notificationsList.get(index);

        viewHolder.textViewUser.setText(notification.getUsuario());
        viewHolder.textViewAction.setText(notification.getAccion());
        viewHolder.textViewAttribute.setText(notification.getAtributo());
        viewHolder.textViewObjet.setText(notification.getObjeto());
        viewHolder.textViewDistinctiveValue.setText(notification.getDistintivoValor());
        viewHolder.textViewAttributeValuePrevious.setText(notification.getAtributoValorAnterior());
        viewHolder.textViewAttributeValueNew.setText(notification.getAtributoValorNuevo());
        viewHolder.textViewDate.setText(notification.getFecha());
    }*/

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int index) {
        Notification notification = notificationsList.get(index);
        String timeAgo = getTimeAgo(notification.getFecha());

        viewHolder.bindData(notification.getUsuario(),
                notification.getAccion(),
                notification.getAtributo(),
                notification.getObjeto(),
                notification.getDistintivoValor(),
                notification.getAtributoValorAnterior(),
                notification.getAtributoValorNuevo(),
                timeAgo);
    }

    private String getTimeAgo(String dateTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            Date date = sdf.parse(dateTime);
            long timeInMillis = date.getTime();
            long now = System.currentTimeMillis();
            CharSequence ago = DateUtils.getRelativeTimeSpanString(timeInMillis, now, DateUtils.MINUTE_IN_MILLIS);
            return ago.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    /*public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewUser, textViewAction, textViewAttribute, textViewObjet, textViewDistinctiveValue, textViewAttributeValuePrevious, textViewAttributeValueNew, textViewDate;
        public View view;

        public ViewHolder(View view) {
            super(view);

            this.view = view;
            this.textViewUser = (TextView) view.findViewById(R.id.textViewUser);
            this.textViewAction = (TextView) view.findViewById(R.id.textViewAction);
            this.textViewAttribute = (TextView) view.findViewById(R.id.textViewAttribute);
            this.textViewObjet = (TextView) view.findViewById(R.id.textViewObjet);
            this.textViewDistinctiveValue = (TextView) view.findViewById(R.id.textViewDistinctiveValue);
            this.textViewAttributeValuePrevious = (TextView) view.findViewById(R.id.textViewAttributeValuePrevious);
            this.textViewAttributeValueNew = (TextView) view.findViewById(R.id.textViewAttributeValueNew);
            this.textViewDate = (TextView) view.findViewById(R.id.textViewDate);
        }
    }*/

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewNotification;
        public View view;

        public ViewHolder(View view) {
            super(view);

            this.view = view;
            this.textViewNotification = (TextView) view.findViewById(R.id.textViewNotification);
        }

        public void bindData(String user, String action, String attribute, String object, String distinctiveValue, String attributeValuePrevious, String attributeValueNew, String timeAgo) {
            // Concatenar todos los textos en una sola cadena
            String notificationText = "<b>" + user + "</b> "
                    + action + " el "
                    + "<b>" + attribute + "</b> " + " del "
                    + object + " "
                    + "<b>" + distinctiveValue + "</b> " + ", de "
                    + attributeValuePrevious + " a "
                    + attributeValueNew + ". "
                    + "<font color=\"#9b9b9b\">" + timeAgo + "</font>";

            // Crear una instancia de SpannableString
            SpannableString spannableString = new SpannableString(Html.fromHtml(notificationText, Html.FROM_HTML_MODE_LEGACY));

            // Obtener el índice de inicio y fin del nombre de usuario
            int startIndex = notificationText.indexOf(user);
            int endIndex = startIndex + user.length();

            // Aplicar estilo de texto negrita al nombre de usuario
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            // Establecer el texto en el TextView
            textViewNotification.setText(spannableString);
        }
    }
}