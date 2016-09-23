package com.example.nayerafzaalahmad.bestellungen;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

import com.epson.epos2.Epos2Exception;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;
import com.epson.epos2.printer.ReceiveListener;

public class MainActivity extends Activity implements ReceiveListener {
    static ArrayList<String> food =  null; //{"Tische", "KalteVorspeisen", "WarmeVorspeisen","EuroGerichte","Grillplatte","FischGerichte","Fischplatten","Nachspeisen","VomGrill","Kleinegaeste", "Getränke"};
    static ArrayList<String> Tische = null;//{"Tisch", "Tisch 1","Tisch 2","Tisch 3","Tisch 4","Tisch 5", "Tisch 6","Tisch 7","Tisch 8","Tisch 9", "Tisch 10","Tisch 11","Tisch 12","Tisch 13", "Tisch 14", "Tisch 15"};
    static ArrayList<String> KalteVorspeisen = null;//{"KalteVorspeisen","Oktapus Salat","Taramo Salat","Chtipati","Feta"};
    static ArrayList<String> WarmeVorspseisen = null;//{"WarmeVorspseisen","Saganaki","Saganaki (Scharfskäse)","Pitta","mit Knoblauch","Gefüllte Papprika"};
    static ArrayList<String> EuroGerichte = null;//{"EuroGerichte","Schnitzel"};
    static ArrayList<String> Grillplatte= null;//{"Grillplatte","Kronos-Platte- Gyros","Grüne Bohnen","Kolosos-Platte","Pirgos-Platte","Zaziki und Salat","Naxos-Platte Gyros, Bifteki, Muscheln", "Pommes, Reis, Zaziki"};
    static ArrayList<String> FischGerichte = null;//{"FischGerichte","Kalamaria vom Grill, mit Pita oder Resi oder Pommes"," Zaziki und Salat","Kalamaria mit Pita und Salat","Kalamaria mit Reis oder Pommes, Zaziki und Salat"};
    static ArrayList<String> Fischplatten = null;//{"Fischplatten","Ägäon-Platte 1 Person","Kalamaria, Muscheln, Seezunge, Scampi, Reis und Salat","Ägäon-Platte 2 Personen"};
    static ArrayList<String> Nachspeisen = null;//{"Nachspeisen","Galakturigo","Joghurt mit Honig"};
    static ArrayList<String> VomGrill = null;//{"VomGrill","Gyros mit Pita","Gyros-souvlaki mit Pita, Zaziki und Salat","Gyros-Kalamaria mit Pita"};
    static ArrayList<String> Kleinegaeste =null;//{"Kleinegaeste","Gyros Kalamaria mit Pita Zaziki und Salat", "Gyros mit Pita","Souvlaki mit Pita"};
    static ArrayList<String> Bestellt=null;
    public String path = "/storage/sdcard0/aaImack";
    private Context mContext = null;
    private Printer  mPrinter = null;
    private Context context;
    private EditText inputtext;
    private String wahl="";
    private ArrayList<String> AuswahlArray;
    private ArrayList<String> BestelltArray;

    //#############
    //Save and Load for the Application
    public static void Save(File file,  ArrayList<String> data)
    {
        FileOutputStream fos = null;
        try
        {
            fos = new FileOutputStream(file);
        }
        catch (FileNotFoundException e) {e.printStackTrace();}
        try
        {
            try
            {
                for (int i = 0; i<data.size(); i++)
                {
                    fos.write(data.get(i).getBytes());
                    if (i < data.size()-1)
                    {
                        fos.write("\n".getBytes());
                    }
                }
            }
            catch (IOException e) {e.printStackTrace();}
        }
        finally
        {
            try
            {
                fos.close();
            }
            catch (IOException e) {e.printStackTrace();}
        }
    }

    //###### public static  ArrayList<String> Load(File file)
   public static  ArrayList<String> Load(File file)
    {
        FileInputStream fis = null;
        try
        {
            fis = new FileInputStream(file);
        }
        catch (FileNotFoundException e) {e.printStackTrace();}
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        String test;
        int anzahl=0;
        try
        {
            while ((test=br.readLine()) != null)
            {
                anzahl++;
            }
        }
        catch (IOException e) {e.printStackTrace();}

        try
        {
            fis.getChannel().position(0);
        }
        catch (IOException e) {e.printStackTrace();}
        String[] array = new String[anzahl];
        String line;
        int i = 0;
        try
        {
            while((line=br.readLine())!=null)
            {
                array[i] = line;
                i++;
            }
        }
        catch (IOException e) {e.printStackTrace();}
        return new ArrayList<>(Arrays.asList(array));
    }

    @Override

    protected void onCreate(Bundle savedInstanceState) {
try
{
    File dir = new File(path);
    dir.mkdirs();
}catch (Exception s){}
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button btback=(Button)findViewById(R.id.button2);
        btback.setVisibility(View.INVISIBLE);
        final Button savedata, loaddata, pr;
        savedata = (Button) findViewById(R.id.savedata);
        loaddata = (Button) findViewById(R.id.loaddata);
        pr = (Button) findViewById(R.id.pr);

        try
        {
            context = this;
            food = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.food)));
            Tische = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.Tische)));
            KalteVorspeisen= new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.KalteVorspeisen)));
            WarmeVorspseisen= new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.WarmeVorspseisen)));
            EuroGerichte= new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.EuroGerichte)));
            Grillplatte =  new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.Grillplatte)));
            FischGerichte= new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.FischGerichte)));
            Fischplatten= new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.Fischplatten)));
            Nachspeisen= new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.Nachspeisen)));
            VomGrill= new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.VomGrill)));
            Kleinegaeste = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.Kleinegaeste)));

        }catch (Exception ed) { }
        try
        {
            File food_file = new File(path +"/food.txt");
            food = Load(food_file);

            File Tische_file = new File(path +"/Tische.txt");
            Tische = Load(Tische_file);

            File KalteVorspeisen_file = new File(path +"/KalteVorspeisen.txt");
            KalteVorspeisen = Load(KalteVorspeisen_file);

            File WarmeVorspseisen_file = new File(path +"/WarmeVorspseisen.txt");
            WarmeVorspseisen = Load(WarmeVorspseisen_file);

            File EuroGerichte_file = new File(path +"/EuroGerichte.txt");
            EuroGerichte = Load(EuroGerichte_file);

            File Grillplatte_file = new File(path +"/Grillplatte.txt");
            Grillplatte = Load(Grillplatte_file);

            File FischGerichte_file = new File(path +"/FischGerichte.txt");
            FischGerichte = Load(FischGerichte_file);

            File Fischplatten_file = new File(path +"/Fischplatten.txt");
            Fischplatten = Load(Fischplatten_file);

            File Nachspeisen_file = new File(path +"/Nachspeisen.txt");
            Nachspeisen = Load(Nachspeisen_file);

            File VomGrill_file = new File(path +"/VomGrill.txt");
            VomGrill=Load(VomGrill_file);

            File kleinegaeste_file = new File(path +"/Kleinegaeste.txt");
            Kleinegaeste = Load(kleinegaeste_file);

            btback.callOnClick();
        }catch (Exception ex ){
            ex.printStackTrace();
            Toast.makeText(MainActivity.this, "Beim Laden der Daten ist ein Fehler Aufgetreten!", Toast.LENGTH_SHORT).show();
        }

        //AuswahlArray = new ArrayList<>(Arrays.asList(food));
        AuswahlArray = new ArrayList<>(food);
        wahl ="food";
        BestelltArray = new ArrayList<>();
        final ArrayAdapter AuswahlAdapterter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, AuswahlArray);
        final ArrayAdapter BestelltAdapter =new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,BestelltArray);
        final ListView BestelltListe = (ListView) findViewById(R.id.BestelltListe);
        BestelltListe.setAdapter(BestelltAdapter);
//###### Hier werden die Zusätzlicvh eingegeben Daten zu dem Element hinzugefügt.
        final ListView Auswahlliste = (ListView) findViewById(R.id.Auswahlliste);
        Auswahlliste.setAdapter(AuswahlAdapterter);
        Auswahlliste.setOnItemLongClickListener
                (//setOnItemLongClickListener
                        new AdapterView.OnItemLongClickListener() {//OnItemClickListener

                            @Override
                            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {

                                final String str = parent.getItemAtPosition(position).toString();
                                if (inputtext.getText().length() > 0) {
                                    BestelltArray.add(parent.getItemAtPosition(position).toString() + " -> " + inputtext.getText());
                                    BestelltAdapter.notifyDataSetChanged();
                                }else
                                {
                                    LayoutInflater li = LayoutInflater.from(context);
                                    View dialodview = li.inflate(R.layout.dialog_main, null);
                                    AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(context);
                                    alertdialogbuilder.setView(dialodview);
                                    //final TextView Info = (TextView) findViewById(R.id.Info);
                                    //Info.setText("Wollen Sie wirklich ");

                                    // Setdialogmessage
                                    alertdialogbuilder.setCancelable(false).setPositiveButton("JA", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            if (  str.startsWith("Tische")  | str.startsWith("KalteVorspeisen") | str.startsWith("WarmeVorspseisen") | str.startsWith("EuroGerichte") | str.startsWith("Grillplatte") | str.startsWith("FischGerichte") | str.startsWith("Fischplatten") | str.startsWith("Nachspeisen") | str.startsWith("VomGrill") | str.startsWith("Kleinegaeste"))
                                            {
                                                Toast.makeText(MainActivity.this, "'"+str+"' Darf nicht gelöscht werden!", Toast.LENGTH_SHORT).show();
                                            }else
                                            {
                                                Toast.makeText(MainActivity.this, "'"+str+"' Wurde erfolgreich gelöscht !", Toast.LENGTH_SHORT).show();
                                                AuswahlArray.remove(str);
                                                AuswahlAdapterter.notifyDataSetChanged();
                                                switch (wahl)
                                                {
                                                    case "food":
                                                        food.remove(str);
                                                    case "Tische":
                                                        Tische.remove(str);
                                                        break;
                                                    case "KalteVorspeisen":
                                                        KalteVorspeisen.remove(str);
                                                        break;
                                                    case "WarmeVorspseisen":
                                                        WarmeVorspseisen.remove(str);
                                                        break;
                                                    case "EuroGerichte":
                                                        EuroGerichte.remove(str);
                                                        break;
                                                    case "Grillplatte":
                                                        Grillplatte.remove(str);
                                                        break;
                                                    case "FischGerichte":
                                                        FischGerichte.remove(str);
                                                        break;
                                                    case "Fischplatten":
                                                        Fischplatten.remove(str);
                                                        break;
                                                    case "Nachspeisen":
                                                        Nachspeisen.remove(str);
                                                        break;
                                                    case "VomGrill":
                                                        VomGrill.remove(str);
                                                        break;
                                                    case "Kleinegaeste":
                                                        Kleinegaeste.remove(str);
                                                        break;
                                                }
                                            }
                                        }
                                    }).setNegativeButton("NEIN", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(MainActivity.this,String.valueOf(str.compareTo("Tische")), Toast.LENGTH_SHORT).show();
                                            dialog.cancel();
                                        }
                                    });
                                    AlertDialog alertDialog = alertdialogbuilder.create();

                                    alertDialog.show();
                                }
                                return true;
                            }
                        }
                );
//------- Hier werden die Zusätzliche eingegeben Daten zu dem Element hinzugefügt.
Auswahlliste.setOnItemClickListener
        (//setOnItemClickListener
                new AdapterView.OnItemClickListener() {//OnItemLongClickListener
                    @Override
                    // Um Elemente auszuwählen bzw Untermenü auszurufen
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {

                        if (String.valueOf(parent.getItemAtPosition(position)).startsWith("Tische"))
                        {wahl="Tische";
                            AuswahlArray.clear();
                            int size = 0;
                            size = Tische.size();
                            AuswahlArray.removeAll(AuswahlArray);
                            for (int i = 0; i < size; i++) {
                                AuswahlArray.add(i, Tische.get(i));
                                AuswahlAdapterter.notifyDataSetChanged();
                            }
                            btback.setVisibility(View.VISIBLE);

                            return;
                        }// Wenn Variable Speisen ausgewählt wurde:
                        if (String.valueOf(parent.getItemAtPosition(position)).startsWith("KalteVorspeisen"))
                        {wahl="KalteVorspeisen";
                            AuswahlArray.clear();
                            int size = 0;
                            size = KalteVorspeisen.size();
                            AuswahlArray.removeAll(AuswahlArray);
                            for (int i = 0; i < size; i++) {
                                AuswahlArray.add(i, KalteVorspeisen.get(i));
                                AuswahlAdapterter.notifyDataSetChanged();
                            }

                            btback.setVisibility(View.VISIBLE);
                            return;
                        }// Wenn Variable KalteVorspeisen ausgewählt wurde:
                        if (String.valueOf(parent.getItemAtPosition(position)).startsWith("WarmeVorspseisen" ))
                        {wahl="WarmeVorspseisen";
                            AuswahlArray.clear();
                            int size = 0;
                            size = WarmeVorspseisen.size();
                            AuswahlArray.removeAll(AuswahlArray);
                            for (int i = 0; i < size; i++) {
                                AuswahlArray.add(i, WarmeVorspseisen.get(i));
                                AuswahlAdapterter.notifyDataSetChanged();
                            }
                            btback.setVisibility(View.VISIBLE);
                            return;
                        }// Wenn WarmeSpeisen ausgewählt wurde:
                        if (String.valueOf(parent.getItemAtPosition(position)).startsWith("EuroGerichte"))
                        {wahl="EuroGerichte";
                            AuswahlArray.clear();
                            int size = 0;
                            size = EuroGerichte.size();
                            AuswahlArray.removeAll(AuswahlArray);
                            for (int i = 0; i < size; i++) {
                                AuswahlArray.add(i, EuroGerichte.get(i));
                                AuswahlAdapterter.notifyDataSetChanged();
                            }
                            btback.setVisibility(View.VISIBLE);
                            return;
                        }// Wenn Variable EuroGerichte ausgewählt wurde:
                        if (String.valueOf(parent.getItemAtPosition(position)).startsWith("Grillplatte"))
                        {   wahl="Grillplatte";
                            AuswahlArray.clear();
                            int size = 0;
                            size = Grillplatte.size();
                            AuswahlArray.removeAll(AuswahlArray);
                            for (int i = 0; i < size; i++) {
                                AuswahlArray.add(i, Grillplatte.get(i));
                                AuswahlAdapterter.notifyDataSetChanged();
                            }
                            btback.setVisibility(View.VISIBLE);
                            return;
                        }// Wenn Variable EuroGerichte ausgewählt wurde:
                        if (String.valueOf(parent.getItemAtPosition(position)).startsWith("FischGerichte"))
                        {wahl="FischGerichte";
                            AuswahlArray.clear();
                            int size = 0;
                            size = FischGerichte.size();
                            AuswahlArray.removeAll(AuswahlArray);
                            for (int i = 0; i < size; i++) {
                                AuswahlArray.add(i, FischGerichte.get(i));
                                AuswahlAdapterter.notifyDataSetChanged();
                            }
                            btback.setVisibility(View.VISIBLE);
                            return;
                        }// Wenn Variable EuroGerichte ausgewählt wurde:
                        if (String.valueOf(parent.getItemAtPosition(position)).startsWith("Fischplatten"))
                        {wahl="Fischplatten";
                            AuswahlArray.clear();
                            int size = 0;
                            size = Fischplatten.size();
                            AuswahlArray.removeAll(AuswahlArray);
                            for (int i = 0; i < size; i++) {
                                AuswahlArray.add(i, Fischplatten.get(i));
                                AuswahlAdapterter.notifyDataSetChanged();
                            }
                            btback.setVisibility(View.VISIBLE);
                            return;
                        }// Wenn Variable EuroGerichte ausgewählt wurde:
                        if (String.valueOf(parent.getItemAtPosition(position)).startsWith("Nachspeisen"))
                        {wahl="Nachspeisen";
                            AuswahlArray.clear();
                            int size = 0;
                            size = Nachspeisen.size();
                            AuswahlArray.removeAll(AuswahlArray);
                            for (int i = 0; i < size; i++) {
                                AuswahlArray.add(i, Nachspeisen.get(i));
                                AuswahlAdapterter.notifyDataSetChanged();
                            }
                            btback.setVisibility(View.VISIBLE);
                            return;
                        }// Wenn Variable EuroGerichte ausgewählt wurde:
                        if (String.valueOf(parent.getItemAtPosition(position)).startsWith("VomGrill"))
                        {wahl="VomGrill";
                            AuswahlArray.clear();
                            int size = 0;
                            size = VomGrill.size();
                            AuswahlArray.removeAll(AuswahlArray);
                            for (int i = 0; i < size; i++) {
                                AuswahlArray.add(i, VomGrill.get(i));
                                AuswahlAdapterter.notifyDataSetChanged();
                            }
                            btback.setVisibility(View.VISIBLE);
                            return;
                        }// Wenn Variable EuroGerichte ausgewählt wurde:
                        if (String.valueOf(parent.getItemAtPosition(position)).startsWith("Kleinegaeste"))
                        {wahl="Kleinegaeste";
                            AuswahlArray.clear();
                            int size = 0;
                            size = Kleinegaeste.size();
                            AuswahlArray.removeAll(AuswahlArray);
                            for (int i = 0; i < size; i++) {
                                AuswahlArray.add(i, Kleinegaeste.get(i));
                                AuswahlAdapterter.notifyDataSetChanged();
                            }
                            btback.setVisibility(View.VISIBLE);
                            return;
                        }// Wenn Variable EuroGerichte ausgewählt wurde:

                        BestelltArray.add(parent.getItemAtPosition(position).toString());
                        BestelltAdapter.notifyDataSetChanged();
                        if (parent.getItemAtPosition(position).toString().startsWith("Tisch "))
                        {
                            btback.performClick();
                        }
                        return;

                    }
                }
        );
//######################
        inputtext = (EditText) findViewById(R.id.eingabe);
        Button btadd = (Button) findViewById(R.id.button);
        btadd.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {

                                         if (inputtext.getText().toString().isEmpty()){
                                             Toast.makeText(MainActivity.this, "Zum anlegen bitte einen Artikel angeben!", Toast.LENGTH_SHORT).show();
                                             return;
                                         }
                                         AuswahlArray.add(inputtext.getText().toString());
                                         AuswahlAdapterter.notifyDataSetChanged();
                                         switch (wahl)
                                         {
                                             case "food":
                                                 food.add(inputtext.getText().toString());
                                                 break;
                                             case "Tische":
                                                 Tische.add(inputtext.getText().toString());
                                                 break;
                                             case "KalteVorspeisen":
                                                 KalteVorspeisen.add(inputtext.getText().toString());
                                                 break;
                                             case "WarmeVorspseisen":
                                                 WarmeVorspseisen.add(inputtext.getText().toString());
                                                 break;
                                             case "EuroGerichte":
                                                 EuroGerichte.add(inputtext.getText().toString());
                                                 break;
                                             case "Grillplatte":
                                                 Grillplatte.add(inputtext.getText().toString());
                                                 break;
                                             case "FischGerichte":
                                                 FischGerichte.add(inputtext.getText().toString());
                                                 break;
                                             case "Fischplatten":
                                                 Fischplatten.add(inputtext.getText().toString());
                                                 break;
                                             case "Nachspeisen":
                                                 Nachspeisen.add(inputtext.getText().toString());
                                                 break;
                                             case "VomGrill":
                                                 VomGrill.add(inputtext.getText().toString());
                                                 break;
                                             case "Kleinegaeste":
                                                 Kleinegaeste.add(inputtext.getText().toString());
                                                 break;
                                         }


                                     }
                                 }
        );
        btback.setOnClickListener
                (new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          if (true) {
                                              AuswahlArray.clear();
                                              int size = 0;
                                              size = food.size();
                                              AuswahlArray.removeAll(AuswahlArray);
                                              for (int i = 0; i < size; i++) {
                                                  AuswahlArray.add(i, food.get(i));
                                                  AuswahlAdapterter.notifyDataSetChanged();
                                              }
                                              wahl="food";
                                              Toast.makeText(MainActivity.this, "Daten wurden Erfolgreich geladen !", Toast.LENGTH_SHORT).show();
                                              btback.setVisibility(View.INVISIBLE);
                                          }// Wenn Variable Speisen ausgewählt wurde:
                                      }
                                  }
                );
        assert savedata != null;
        savedata.setOnClickListener
                (new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                          //###############
                         try
                         {
                             File food_file=new File(path+"/food.txt");
                             Save(food_file, food);

                             File Tische_file= new File(path+"/Tische.txt");
                             Save(Tische_file,Tische);

                             File KalteVorspeisen_file= new File(path+"/KalteVorspeisen.txt");
                             Save(KalteVorspeisen_file,KalteVorspeisen);

                             File WarmeVorspseisen_file= new File(path+"/WarmeVorspseisen.txt");
                             Save(WarmeVorspseisen_file,WarmeVorspseisen);

                             File EuroGerichte_file= new File(path+"/EuroGerichte.txt");
                             Save(EuroGerichte_file,EuroGerichte);

                             File Grillplatte_file= new File(path+"/Grillplatte.txt");
                             Save(Grillplatte_file,Grillplatte);

                             File FischGerichte_file= new File(path+"/FischGerichte.txt");
                             Save(FischGerichte_file,FischGerichte);

                             File Fischplatten_file= new File(path+"/Fischplatten.txt");
                             Save(Fischplatten_file,Fischplatten);

                             File Nachspeisen_file= new File(path+"/Nachspeisen.txt");
                             Save(Nachspeisen_file,Nachspeisen);

                             File VomGrill_file= new File(path+"/VomGrill.txt");
                             Save(VomGrill_file,VomGrill);

                             File kleinegaeste_file= new File(path+"/Kleinegaeste.txt");
                             Save(kleinegaeste_file, Kleinegaeste);

                         }catch (Exception ex){ex.printStackTrace();
                             Toast.makeText(MainActivity.this, "Irgend etwas ist beim Speichern falsch gelaufen!", Toast.LENGTH_SHORT).show();}
                         //public static void Save(File file, String[] data)
                         //################
                     }
                 }
                );
        assert loaddata != null;
        loaddata.setOnClickListener
                (new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         //################
                         //public static String[] Load(File file)
                         Toast.makeText(MainActivity.this, "Daten werden geladen !", Toast.LENGTH_SHORT).show();
                         try
                         {
                             File food_file = new File(path +"/food.txt");
                             food = Load(food_file);

                             File Tische_file = new File(path +"/Tische.txt");
                             Tische = Load(Tische_file);

                             File KalteVorspeisen_file = new File(path +"/KalteVorspeisen.txt");
                             KalteVorspeisen = Load(KalteVorspeisen_file);

                             File WarmeVorspseisen_file = new File(path +"/WarmeVorspseisen.txt");
                             WarmeVorspseisen = Load(WarmeVorspseisen_file);

                             File EuroGerichte_file = new File(path +"/EuroGerichte.txt");
                             EuroGerichte = Load(EuroGerichte_file);

                             File Grillplatte_file = new File(path +"/Grillplatte.txt");
                             Grillplatte = Load(Grillplatte_file);

                             File FischGerichte_file = new File(path +"/FischGerichte.txt");
                             FischGerichte = Load(FischGerichte_file);

                             File Fischplatten_file = new File(path +"/Fischplatten.txt");
                             Fischplatten = Load(Fischplatten_file);

                             File Nachspeisen_file = new File(path +"/Nachspeisen.txt");
                             Nachspeisen = Load(Nachspeisen_file);

                             File VomGrill_file = new File(path +"/VomGrill.txt");
                             VomGrill=Load(VomGrill_file);

                             File kleinegaeste_file = new File(path +"/Kleinegaeste.txt");
                             Kleinegaeste = Load(kleinegaeste_file);

                             btback.callOnClick();
                         }catch (Exception ex ){
                             ex.printStackTrace();
                             Toast.makeText(MainActivity.this, "Beim Laden der Daten ist ein Fehler Aufgetreten!", Toast.LENGTH_SHORT).show();
                         }
                         //################
                     }
                 }
                );
//###########################
        pr.setOnClickListener
                (new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         //################

                         //public static String[] Load(File file)

                         try {
                             if (!runPrintReceiptSequence()) {

                             }
                         } catch (Exception ex) {
                             ex.printStackTrace();
                             Toast.makeText(MainActivity.this, "Beim Drucken der Daten ist ein Fehler Aufgetreten!", Toast.LENGTH_SHORT).show();
                         }
                         //################
                     }
                 }
                );
        //##################
        BestelltListe.setOnItemClickListener
                (
                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Toast.makeText(MainActivity.this, String.valueOf(parent.getItemAtPosition(position)) + " wird gelöscht !", Toast.LENGTH_LONG).show();
                                BestelltArray.remove(position);
                                BestelltAdapter.notifyDataSetChanged();
                            }
                        }
                );
//######################ENDE
            }
    @Override
    protected void onStop() {super.onStop();}

    @Override
    protected void finalize() throws Throwable {super.finalize();}

    @Override
    protected void onDestroy() {
        Toast.makeText(MainActivity.this,"Bestellungen wird beendet",Toast.LENGTH_LONG).show();

        super.onDestroy();
    }
    //#############
    private boolean runPrintReceiptSequence() {
        if (!initializeObject()) {
            return false;
        }
        if (!createReceiptData()) {
            finalizeObject();
            return false;
        }
        if (!printData()) {
            finalizeObject();
            return false;
        }
        return true;
    }
    private boolean createReceiptData() {

        StringBuilder textData = new StringBuilder();


        if (mPrinter == null) {
            return false;
        }

        try {
            //BestelltArray.size();
            mPrinter.addTextAlign(Printer.ALIGN_CENTER);
            textData.append("------------------------------\n");
            mPrinter.addText(textData.toString());
            mPrinter.addTextAlign(Printer.ALIGN_LEFT);
            textData.delete(0, textData.length());

            if (BestelltArray.size()>0)
            {
                for(int i=0;i<BestelltArray.size();i++)
                {

                    if(BestelltArray.get(i).toString().startsWith("Tisch"))
                    {mPrinter.addTextAlign(Printer.ALIGN_CENTER);
                        mPrinter.addTextStyle(Printer.FALSE, Printer.TRUE, Printer.TRUE, Printer.PARAM_DEFAULT);
                        textData.append(BestelltArray.get(i).toString() + "\n");
                        textData.append("\n");
                        mPrinter.addTextSize(3, 3);
                        mPrinter.addText(textData.toString());
                        mPrinter.addTextSize(1, 1);
                        mPrinter.addTextAlign(Printer.ALIGN_LEFT);
                        mPrinter.addTextStyle(Printer.FALSE, Printer.FALSE, Printer.TRUE, Printer.PARAM_DEFAULT);
                        textData.delete(0, textData.length());
                    }else if (BestelltArray.get(i).toString().endsWith("scharf"))
                    {mPrinter.addTextAlign(Printer.ALIGN_RIGHT);
                        textData.append(BestelltArray.get(i).toString() + "\n");
                        mPrinter.addText(textData.toString());
                        mPrinter.addTextAlign(Printer.ALIGN_LEFT);
                        textData.delete(0, textData.length());
                    }else
                    {
                        textData.append(BestelltArray.get(i).toString()+"\n");
                        mPrinter.addText(textData.toString());
                        textData.delete(0, textData.length());
                    }


                }
            }else
            {
                Toast.makeText(this,"Sie müssen mindestens etwas auswählen.",Toast.LENGTH_LONG).show();
                return false;
            }
            mPrinter.addTextAlign(Printer.ALIGN_CENTER);
            textData.append("\n");
            textData.append("------------------------------\n");
            mPrinter.addText(textData.toString());
            textData.delete(0, textData.length());
            mPrinter.addTextAlign(Printer.ALIGN_LEFT);
            textData.append("TADERNE PIGOS\n");
            mPrinter.addText(textData.toString());
            textData.delete(0, textData.length());
            mPrinter.addTextAlign(Printer.ALIGN_RIGHT);
            textData.append("Inh.: Kalligas Georg\n");
            mPrinter.addText(textData.toString());
            textData.delete(0, textData.length());
            mPrinter.addTextAlign(Printer.ALIGN_LEFT);
            textData.append("Leonhard-von-Eck Str. 7\n");
            textData.append("93195 Wolgsegg\n");
            textData.append("Tel.: 09409-85 97 35\n");
            mPrinter.addText(textData.toString());
            textData.delete(0, textData.length());
            mPrinter.addTextAlign(Printer.ALIGN_RIGHT);
            textData.append("@ Nayer Afzaal\n");
            mPrinter.addText(textData.toString());
            textData.delete(0, textData.length());
            mPrinter.addCut(Printer.CUT_FEED);
        }
        catch (Exception e) {
            android.util.Log.i("Problem--->", "Probleme beim CreateReceipt()");
            return false;
        }
        textData = null;
        android.util.Log.i("ERFOLG--->","CreateReceipt hat funktioniert");
        return true;
    }

    //private boolean printData()
    private boolean printData()
    {
        if (mPrinter == null) {
            return false;
        }
        if (!connectPrinter()) {
            return false;
        }

        PrinterStatusInfo status = mPrinter.getStatus();

        dispPrinterWarnings(status);

        if (!isPrintable(status)) {
            Log.i("Connect "+status.toString(),context.toString());
            try {
                mPrinter.disconnect();
            }
            catch (Exception ex) {
                // Do nothing
            }
            return false;
        }
        try {
            mPrinter.sendData(Printer.PARAM_DEFAULT);
        }
        catch (Exception e) {
            try {
                mPrinter.disconnect();
            }
            catch (Exception ex) {
                // Do nothing
            }
            return false;
        }

        return true;
    }//private boolean printData()


//private boolean initializeObject()
    private boolean initializeObject()
    {
        try {
            mPrinter = new Printer(Printer.TM_T88, Printer.MODEL_ANK, context);
        }
        catch (Exception e) {
            android.util.Log.i("Probleme--->","Probleme beim Initialisieren");
            return false;
        }

        mPrinter.setReceiveEventListener(this);
        android.util.Log.i("ERFOLG--->", "Initialisieren InitializeObject() OK");
        return true;
    }//private boolean initializeObject()


    private void finalizeObject()
    {
        if (mPrinter == null) {
            return;
        }

        mPrinter.clearCommandBuffer();

        mPrinter.setReceiveEventListener(null);

        mPrinter = null;
    }
//private boolean connectPrinter()
    private boolean connectPrinter()
    {
        boolean isBeginTransaction = false;
        if (mPrinter == null) {
            return false;
        }
        try {
            android.util.Log.i("Wird versucht: ","Verbindung wird HErgestellt.");
            PrinterStatusInfo status =mPrinter.getStatus();
            status.getOnline();
            android.util.Log.i("PrinterStatus", mPrinter.getStatus().toString());
            mPrinter.connect("TCP:192.168.2.168", Printer.PARAM_DEFAULT);
        }
        catch (Exception e) {
            android.util.Log.i("Probleme--->", "Connect Printer hat nicht funktioniert");
            return false;
        }

        try {
            mPrinter.beginTransaction();
            isBeginTransaction = true;
        }
        catch (Exception e) {

        }

        if (isBeginTransaction == false) {
            try {
                mPrinter.disconnect();
            }
            catch (Epos2Exception e) {
                // Do nothing
                return false;
            }
        }
        android.util.Log.i("ERFOLG--->", "ConnectPrinter hat funktioniert!");
        return true;
    }//private boolean connectPrinter()


    private void disconnectPrinter() {
        if (mPrinter == null) {
            return;
        }

        try {
            mPrinter.endTransaction();
        }
        catch (final Exception e) {
            runOnUiThread(new Runnable() {
                @Override
                public synchronized void run() {

                }
            });
        }

        try {
            mPrinter.disconnect();
        }
        catch (final Exception e) {
            runOnUiThread(new Runnable() {
                @Override
                public synchronized void run() {

                }
            });
        }

        finalizeObject();
    }

    private boolean isPrintable(PrinterStatusInfo status) {
        if (status == null) {
            return false;
        }

        if (status.getConnection() == Printer.FALSE) {
            return false;
        }
        else if (status.getOnline() == Printer.FALSE) {
            return false;
        }
        else {
            //print available
        }

        return true;
    }

    private String makeErrorMessage(PrinterStatusInfo status) {
        String msg = "";

        if (status.getOnline() == Printer.FALSE) {
            //msg += getString(R.string.handlingmsg_err_offline);
        }
        if (status.getConnection() == Printer.FALSE) {
            //msg += getString(R.string.handlingmsg_err_no_response);
        }
        if (status.getCoverOpen() == Printer.TRUE) {
            //msg += getString(R.string.handlingmsg_err_cover_open);
        }
        if (status.getPaper() == Printer.PAPER_EMPTY) {
            //msg += getString(R.string.handlingmsg_err_receipt_end);
        }
        if (status.getPaperFeed() == Printer.TRUE || status.getPanelSwitch() == Printer.SWITCH_ON) {
            //msg += getString(R.string.handlingmsg_err_paper_feed);
        }
        if (status.getErrorStatus() == Printer.MECHANICAL_ERR || status.getErrorStatus() == Printer.AUTOCUTTER_ERR) {
            //msg += getString(R.string.handlingmsg_err_autocutter);
            //msg += getString(R.string.handlingmsg_err_need_recover);
        }
        if (status.getErrorStatus() == Printer.UNRECOVER_ERR) {
            //msg += getString(R.string.handlingmsg_err_unrecover);
        }
        if (status.getErrorStatus() == Printer.AUTORECOVER_ERR) {
            if (status.getAutoRecoverError() == Printer.HEAD_OVERHEAT) {
              //  msg += getString(R.string.handlingmsg_err_overheat);
                //msg += getString(R.string.handlingmsg_err_head);
            }
            if (status.getAutoRecoverError() == Printer.MOTOR_OVERHEAT) {
                //msg += getString(R.string.handlingmsg_err_overheat);
                //msg += getString(R.string.handlingmsg_err_motor);
            }
            if (status.getAutoRecoverError() == Printer.BATTERY_OVERHEAT) {
                //msg += getString(R.string.handlingmsg_err_overheat);
                //msg += getString(R.string.handlingmsg_err_battery);
            }
            if (status.getAutoRecoverError() == Printer.WRONG_PAPER) {
                //msg += getString(R.string.handlingmsg_err_wrong_paper);
            }
        }
        if (status.getBatteryLevel() == Printer.BATTERY_LEVEL_0) {
            //msg += getString(R.string.handlingmsg_err_battery_real_end);
        }

        return msg;
    }

    private void dispPrinterWarnings(PrinterStatusInfo status) {
        //EditText edtWarnings = (EditText)findViewById(R.id.edtWarnings);
        String warningsMsg = "";

        if (status == null) {
            return;
        }

        if (status.getPaper() == Printer.PAPER_NEAR_END) {
            //warningsMsg += getString(R.string.handlingmsg_warn_receipt_near_end);
        }

        if (status.getBatteryLevel() == Printer.BATTERY_LEVEL_1) {
            //warningsMsg += getString(R.string.handlingmsg_warn_battery_near_end);
        }

       // edtWarnings.setText(warningsMsg);
    }


    @Override
    public void onPtrReceive(final Printer printerObj, final int code, final PrinterStatusInfo status, final String printJobId) {
        runOnUiThread(new Runnable() {
            @Override
            public synchronized void run() {
                dispPrinterWarnings(status);
                Toast.makeText(context, "Die Bestellung wurde Erfolgreich an der Küche übertragen", Toast.LENGTH_LONG).show();


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        disconnectPrinter();

                    }
                }).start();
            }
        });
    }

}
