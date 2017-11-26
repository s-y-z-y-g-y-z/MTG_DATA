package com.mtg.http;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.Certificate;
import java.io.*;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;

import jdk.nashorn.internal.parser.JSONParser;
import org.json.*;


// handles HttpRequests and the content it receives (JSON)
public class HttpReq {

    public void testIt(){

        String https_url = "https://api.scryfall.com";
        URL url;
        try {

            url = new URL(https_url);
            HttpsURLConnection con = (HttpsURLConnection)url.openConnection();

            //dumpl all cert info
            //print_https_cert(con);


            //dump all the content
           // print_content(con);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void print_https_cert(HttpsURLConnection con){

        if(con!=null){

            try {

                System.out.println("Response Code : " + con.getResponseCode());
                System.out.println("Cipher Suite : " + con.getCipherSuite());
                System.out.println("\n");

                Certificate[] certs = con.getServerCertificates();
                for(Certificate cert : certs){
                    System.out.println("Cert Type : " + cert.getType());
                    System.out.println("Cert Hash Code : " + cert.hashCode());
                    System.out.println("Cert Public Key Algorithm : "
                            + cert.getPublicKey().getAlgorithm());
                    System.out.println("Cert Public Key Format : "
                            + cert.getPublicKey().getFormat());
                    System.out.println("\n");
                }

            } catch (SSLPeerUnverifiedException e) {
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }

        }

    }

    private void print_content(HttpsURLConnection con){
        if(con!=null){

            try {

                System.out.println("****** Content of the URL ********");
                BufferedReader br =
                        new BufferedReader(
                                new InputStreamReader(con.getInputStream()));

                String input;

                while ((input = br.readLine()) != null){
                    System.out.println(input);

                }
                br.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void multverseIDSearch(int multverseID)
    {
        String https_url = "https://api.scryfall.com/cards/multiverse/" + multverseID;
        URL url;
        try {
            url = new URL(https_url);
            HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
            //JSONObject jo = con.getContentType();
            String price = con.getContentType();

            if (con != null)
            {
               // JSONParser jsonParser = new JSONParser();
                //jsonParser.parse(con);
                System.out.println(price);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
