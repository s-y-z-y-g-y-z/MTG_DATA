package com.mtg.http;

import java.net.MalformedURLException;
import java.net.URL;
import java.io.*;
import javax.net.ssl.HttpsURLConnection;
import org.json.*;


// handles HttpRequests and the content it receives (JSON)
//
public class HttpReq {
    //UNUSED function that was uses to test connection to Scryfall API
    public void testIt(){

        String https_url = "https://api.scryfall.com";
        URL url;
        try {

            url = new URL(https_url);
            HttpsURLConnection con = (HttpsURLConnection)url.openConnection();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //return JSONObject from url connection
    private JSONObject getJSONPrice(HttpsURLConnection con){
        String price = null;
        JSONObject json = null;
        if(con!=null){

            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String input;

                while ((input = br.readLine()) != null){
                    price = input;
                }

                json = new JSONObject(price);
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return json;
    }
    // requires integer key of card to search for card information from Scryfall API
    public String multverseIDInfo(int multverseID, String key)
    {
        String https_url = "https://api.scryfall.com/cards/multiverse/" + multverseID;
        URL url;
        String price = null;
        try {
            url = new URL(https_url);
            HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
            JSONObject jo = new JSONObject();

            if (con != null)
            {
                jo = getJSONPrice(con);
            }
            price = jo.getString(key);
            System.out.println("Card price = " + price);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return price;
    }
}
