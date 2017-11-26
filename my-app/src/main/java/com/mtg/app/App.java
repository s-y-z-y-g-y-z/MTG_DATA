package com.mtg.app;

import com.mtg.http.HttpReq;
import io.magicthegathering.javasdk.api.CardAPI;
import io.magicthegathering.javasdk.resource.Card;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        int multverseId = 1;
        Card card = CardAPI.getCard(multverseId);
        Swing gui = new Swing();
        HttpReq request = new HttpReq();

        request.testIt();
        System.out.println();
        request.multverseIDSearch(multverseId);
        System.out.println( card.getImageUrl() );

        gui.main();
    }



}
